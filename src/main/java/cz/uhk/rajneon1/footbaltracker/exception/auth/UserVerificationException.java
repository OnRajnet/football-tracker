package cz.uhk.rajneon1.footbaltracker.exception.auth;

import cz.uhk.rajneon1.footbaltracker.exception.CustomException;

public class UserVerificationException extends CustomException {

    public UserVerificationException(String message) {
        super(message);
    }

}
