package cz.uhk.rajneon1.footbaltracker.rest.dto.request;

public class GoogleOAuthTokenDto {

    private String token;

    public GoogleOAuthTokenDto(String token) {
        this.token = token;
    }

    public GoogleOAuthTokenDto() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
