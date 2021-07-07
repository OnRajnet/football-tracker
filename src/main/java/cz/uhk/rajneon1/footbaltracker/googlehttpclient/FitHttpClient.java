package cz.uhk.rajneon1.footbaltracker.googlehttpclient;

import cz.uhk.rajneon1.footbaltracker.model.Player;
import cz.uhk.rajneon1.footbaltracker.model.PlayerPerformancePerMatch;
import cz.uhk.rajneon1.footbaltracker.security.GoogleOauthTokenHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class FitHttpClient {

    private GoogleOauthTokenHandler tokenHandler;
    private String clientId;
    private String clientSecret;

    public FitHttpClient(Environment environment, GoogleOauthTokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
        clientId = environment.getProperty("google.api.client.id");
        clientSecret = environment.getProperty("google.api.client.secret");
    }

    public String retrieveRefreshToken(String authCode) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRefresh = new HttpPost("https://www.googleapis.com/oauth2/v4/token");
        postRefresh.getParams().setParameter("code", URLEncoder.encode(authCode, "UTF-8"));
        postRefresh.getParams().setParameter("client_id", URLEncoder.encode(clientId, "UTF-8"));
        postRefresh.getParams().setParameter("client_secret", URLEncoder.encode(clientSecret, "UTF-8"));
        postRefresh.getParams().setParameter("redirect_uri", URLEncoder.encode("http://localhost:30600/handleGoogleAuthToken", "UTF-8"));
        postRefresh.getParams().setParameter("grant_type", URLEncoder.encode("authorization_code", "UTF-8"));
        postRefresh.setHeader("Content-Type", "application/x-www-form-urlencoded");
        String response = EntityUtils.toString(httpClient.execute(postRefresh).getEntity());
        return new JSONObject(response).getString("refresh_token");
    }

    public String buildUserConsentRedirect(String redirectHost) throws UnsupportedEncodingException {
        return "https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=" + URLEncoder.encode(redirectHost + "/handleGoogleAuthToken", "UTF-8") +
                "&prompt=consent&response_type=code&client_id=" + URLEncoder.encode(clientId, "UTF-8") + "&scope=" +
                URLEncoder.encode("https://www.googleapis.com/auth/fitness.activity.read", "UTF-8") + "&access_type=offline";
    }

    public PlayerPerformancePerMatch getPlayerPerformance(Player player, long from, long to) throws IOException,
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String authToken = getAuthTokenFromRefreshToken(httpClient, player);
        long duration = to - from;
        int steps = getSteps(httpClient, authToken, from, to, duration);
        double distance = getDistance(httpClient, authToken, from, to, duration);
        PlayerSpeed speed = getSpeed(httpClient, authToken, from, to, duration);
        httpClient.close();
        return new PlayerPerformancePerMatch(player, steps, distance, speed.getMax(), speed.getMin(), speed.getAvg());
    }

    private String getAuthTokenFromRefreshToken(CloseableHttpClient httpClient, Player player) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        String refreshToken = tokenHandler.retrieveRefreshToken(player.getLogin());
        HttpPost postRefresh = new HttpPost("https://www.googleapis.com/oauth2/v4/token");
        postRefresh.getParams().setParameter("client_id", URLEncoder.encode(clientId, "UTF-8"));
        postRefresh.getParams().setParameter("client_secret", URLEncoder.encode(clientSecret, "UTF-8"));
        postRefresh.getParams().setParameter("refresh_token", URLEncoder.encode(refreshToken, "UTF-8"));
        postRefresh.getParams().setParameter("grant_type", URLEncoder.encode("refresh_token", "UTF-8"));
        postRefresh.setHeader("Content-Type", "application/x-www-form-urlencoded");
        String response = EntityUtils.toString(httpClient.execute(postRefresh).getEntity());
        return new JSONObject(response).getString("access_token");
    }

    private int getSteps(CloseableHttpClient httpClient, String authToken, long from, long to, long duration) throws IOException {
        HttpPost postSteps = new HttpPost("https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate");
        String requestBody =
                "{\n" +
                        "  \"aggregateBy\": [{\n" +
                        "    \"dataTypeName\": \"com.google.step_count.delta\",\n" +
                        "    \"dataSourceId\": \"derived:com.google.step_count.delta:com.google.android.gms:estimated_steps\"\n" +
                        "  }],\n" +
                        "  \"bucketByTime\": { \"durationMillis\": " + duration + " },\n" +
                        "  \"startTimeMillis\": " + from + ",\n" +
                        "  \"endTimeMillis\": " + to + "\n" +
                        "}";
        postSteps.setEntity(new StringEntity(requestBody));
        postSteps.setHeader("Authorization", "Bearer " + authToken);
        String response = EntityUtils.toString(httpClient.execute(postSteps).getEntity());
        return new JSONObject(response).getJSONArray("dataset").getJSONObject(0).getJSONArray("point")
                .getJSONObject(0).getJSONArray("value").getJSONObject(0).getInt("intVal");
    }

    private double getDistance(CloseableHttpClient httpClient, String authToken, long from, long to, long duration) throws IOException {
        HttpPost postDistance = new HttpPost("https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate");
        String requestBody =
                "{\n" +
                        "  \"aggregateBy\": [{\n" +
                        "    \"dataTypeName\": \"com.google.step_distance.delta\",\n" +
                        "    \"dataSourceId\": \"derived:com.google.distance.delta:com.google.android.gms:merge_distance_delta\"\n" +
                        "  }],\n" +
                        "  \"bucketByTime\": { \"durationMillis\": " + duration + " },\n" +
                        "  \"startTimeMillis\": " + from + ",\n" +
                        "  \"endTimeMillis\": " + to + "\n" +
                        "}";
        postDistance.setEntity(new StringEntity(requestBody));
        postDistance.setHeader("Authorization", "Bearer " + authToken);
        String response = EntityUtils.toString(httpClient.execute(postDistance).getEntity());
        return new JSONObject(response).getJSONArray("dataset").getJSONObject(0).getJSONArray("point")
                .getJSONObject(0).getJSONArray("value").getJSONObject(0).getDouble("fpVal");
    }

    private PlayerSpeed getSpeed(CloseableHttpClient httpClient, String authToken, long from, long to, long duration) throws IOException {
        HttpPost postDistance = new HttpPost("https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate");
        String requestBody =
                "{\n" +
                        "  \"aggregateBy\": [{\n" +
                        "    \"dataTypeName\": \"com.google.speed\",\n" +
                        "    \"dataSourceId\": \"derived:com.google.speed:com.google.android.gms:merge_speed\"\n" +
                        "  }],\n" +
                        "  \"bucketByTime\": { \"durationMillis\": " + duration + " },\n" +
                        "  \"startTimeMillis\": " + from + ",\n" +
                        "  \"endTimeMillis\": " + to + "\n" +
                        "}";
        postDistance.setEntity(new StringEntity(requestBody));
        postDistance.setHeader("Authorization", "Bearer " + authToken);
        String response = EntityUtils.toString(httpClient.execute(postDistance).getEntity());
        JSONArray speeds = new JSONObject(response).getJSONArray("dataset").getJSONObject(0)
                .getJSONArray("point").getJSONObject(0).getJSONArray("value");
        double avg = speeds.getJSONObject(0).getDouble("fpVal");
        double max = speeds.getJSONObject(1).getDouble("fpVal");
        double min = speeds.getJSONObject(2).getDouble("fpVal");
        return new PlayerSpeed(max, min, avg);
    }

    private class PlayerSpeed {
        private double max;
        private double min;
        private double avg;

        public PlayerSpeed(double max, double min, double avg) {
            this.max = max;
            this.min = min;
            this.avg = avg;
        }

        public double getMax() {
            return max;
        }

        public double getMin() {
            return min;
        }

        public double getAvg() {
            return avg;
        }
    }

}
