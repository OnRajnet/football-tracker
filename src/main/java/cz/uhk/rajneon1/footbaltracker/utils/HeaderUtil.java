package cz.uhk.rajneon1.footbaltracker.utils;

import cz.uhk.rajneon1.footbaltracker.exception.auth.AuthorizationHeaderMissingException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.MalformedAuthorizationHeaderException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class HeaderUtil {

    public String getTokenFromAuthHeader(HttpServletRequest request) throws MalformedAuthorizationHeaderException,
            AuthorizationHeaderMissingException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            if (authHeader.startsWith("Bearer")) {
                return authHeader.replace("Bearer ", "");
            } else {
                throw new MalformedAuthorizationHeaderException("Authorization token is invalid. Missing 'Bearer' token.");
            }
        } else {
            throw new AuthorizationHeaderMissingException("Authorization header is missing.");
        }
    }

}
