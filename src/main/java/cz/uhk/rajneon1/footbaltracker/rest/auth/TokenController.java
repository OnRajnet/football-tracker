package cz.uhk.rajneon1.footbaltracker.rest.auth;

import cz.uhk.rajneon1.footbaltracker.exception.auth.AuthorizationHeaderMissingException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.MalformedAuthorizationHeaderException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.UserVerificationException;
import cz.uhk.rajneon1.footbaltracker.security.TokenHandler;
import cz.uhk.rajneon1.footbaltracker.security.UserVerifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@RestController
public class TokenController {

    private TokenHandler tokenHandler;
    private UserVerifier userVerifier;

    public TokenController(TokenHandler tokenHandler, UserVerifier userVerifier) {
        this.tokenHandler = tokenHandler;
        this.userVerifier = userVerifier;
    }

    @PostMapping("/api/auth/token")
    public ResponseEntity<String> getToken(HttpServletRequest request) throws AuthorizationHeaderMissingException,
            MalformedAuthorizationHeaderException, UserVerificationException {

        if (authHeader == null) {
            throw new AuthorizationHeaderMissingException("Authorization header is missing.");
        } else if (!authHeader.startsWith("Basic ")) {
            throw new MalformedAuthorizationHeaderException("Malformed authorization header.");
        }
        authHeader = authHeader.substring(6);
        authHeader = new String(Base64.getDecoder().decode(authHeader));
        String username;
        String password;
        String[] creds = authHeader.split(":", 2);
        try {
            username = creds[0];
            password = creds[1];
        } catch (IndexOutOfBoundsException e) {
            throw new MalformedAuthorizationHeaderException("Malformed authorization header.");
        }
        if (!userVerifier.verifyUser(username, password)) {
            throw new UserVerificationException("Incorrect password or nonexistent user.");
        }
        String token = tokenHandler.generateTokenFromUsername(username);
        return ResponseEntity.ok(token);
    }
}