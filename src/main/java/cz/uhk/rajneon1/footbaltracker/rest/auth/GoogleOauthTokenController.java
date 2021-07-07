package cz.uhk.rajneon1.footbaltracker.rest.auth;

import cz.uhk.rajneon1.footbaltracker.dao.UserRepository;
import cz.uhk.rajneon1.footbaltracker.exception.auth.AuthorizationHeaderMissingException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.MalformedAuthorizationHeaderException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.UserVerificationException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.ForbiddenResourceException;
import cz.uhk.rajneon1.footbaltracker.googlehttpclient.FitHttpClient;
import cz.uhk.rajneon1.footbaltracker.model.User;
import cz.uhk.rajneon1.footbaltracker.model.UserRole;
import cz.uhk.rajneon1.footbaltracker.rest.dto.request.GoogleOAuthTokenDto;
import cz.uhk.rajneon1.footbaltracker.security.GoogleOauthTokenHandler;
import cz.uhk.rajneon1.footbaltracker.security.RequestVerifier;
import cz.uhk.rajneon1.footbaltracker.security.ResourceVerifier;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class GoogleOauthTokenController {

    private UserRepository userRepository;
    private FitHttpClient fitHttpClient;
    private GoogleOauthTokenHandler tokenHandler;
    private RequestVerifier requestVerifier;
    private ResourceVerifier resourceVerifier;
    private Environment environment;

    public GoogleOauthTokenController(UserRepository userRepository, FitHttpClient fitHttpClient,
                                      GoogleOauthTokenHandler tokenHandler, RequestVerifier requestVerifier,
                                      ResourceVerifier resourceVerifier, Environment environment) {
        this.userRepository = userRepository;
        this.fitHttpClient = fitHttpClient;
        this.tokenHandler = tokenHandler;
        this.requestVerifier = requestVerifier;
        this.resourceVerifier = resourceVerifier;
        this.environment = environment;
    }

    @PostMapping("/api/auth/handleGoogleAuthToken/{login}")
    public void saveGoogleOAuthToken(@PathVariable("login") String login, @RequestBody GoogleOAuthTokenDto dto) throws IOException,
            InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        tokenHandler.saveRefreshToken(login, fitHttpClient.retrieveRefreshToken(dto.getToken()));
    }

    @PostMapping("/api/player/{login}/consent")
    public void getPlayerConsent(@PathVariable("login") String login, HttpServletRequest request, HttpServletResponse response) throws ForbiddenResourceException,
            UserVerificationException, MalformedAuthorizationHeaderException, AuthorizationHeaderMissingException, IOException {
        User requestingUser = requestVerifier.verifyRequest(request, UserRole.PLAYER);
        resourceVerifier.verifyResource(requestingUser, login);
        String protocol = environment.getProperty("server.ssl.key-store") != null ? "https" : "http";
        int port = Integer.parseInt(environment.getProperty("server.port"));
        response.sendRedirect(fitHttpClient.buildUserConsentRedirect(protocol + "://localhost:" + port));
    }

}
