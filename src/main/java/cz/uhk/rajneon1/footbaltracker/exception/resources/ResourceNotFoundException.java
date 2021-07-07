package cz.uhk.rajneon1.footbaltracker.exception.resources;

import cz.uhk.rajneon1.footbaltracker.exception.CustomException;

public class ResourceNotFoundException extends CustomException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
