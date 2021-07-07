package cz.uhk.rajneon1.footbaltracker.exception.auth;

import cz.uhk.rajneon1.footbaltracker.exception.CustomException;

public class MalformedAuthorizationHeaderException extends CustomException {

    public MalformedAuthorizationHeaderException(String message) {
        super(message);
    }

}
