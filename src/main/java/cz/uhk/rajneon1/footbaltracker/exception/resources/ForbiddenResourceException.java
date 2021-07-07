package cz.uhk.rajneon1.footbaltracker.exception.resources;

import cz.uhk.rajneon1.footbaltracker.exception.CustomException;

public class ForbiddenResourceException extends CustomException {

    public ForbiddenResourceException(String message) {
        super(message);
    }

}
