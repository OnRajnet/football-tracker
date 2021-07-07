package cz.uhk.rajneon1.footbaltracker.exception.auth;

import cz.uhk.rajneon1.footbaltracker.exception.CustomException;

public class AuthorizationHeaderMissingException extends CustomException {

    public AuthorizationHeaderMissingException(String message) {
        super(message);
    }

}
