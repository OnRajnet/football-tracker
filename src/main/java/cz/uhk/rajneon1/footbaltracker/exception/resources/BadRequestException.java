package cz.uhk.rajneon1.footbaltracker.exception.resources;

import cz.uhk.rajneon1.footbaltracker.exception.CustomException;

public class BadRequestException extends CustomException {

    public BadRequestException(String message) {
        super(message);
    }

}
