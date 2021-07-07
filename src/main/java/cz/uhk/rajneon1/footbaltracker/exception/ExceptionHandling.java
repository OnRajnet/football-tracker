package cz.uhk.rajneon1.footbaltracker.exception;

import cz.uhk.rajneon1.footbaltracker.exception.auth.AuthorizationHeaderMissingException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.MalformedAuthorizationHeaderException;
import cz.uhk.rajneon1.footbaltracker.exception.auth.UserVerificationException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.BadRequestException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.ForbiddenResourceException;
import cz.uhk.rajneon1.footbaltracker.exception.resources.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity handleExceptions(CustomException e) {
        if (e instanceof AuthorizationHeaderMissingException || e instanceof MalformedAuthorizationHeaderException ||
        e instanceof UserVerificationException) {
            return ResponseEntity.status(401).body(e.getMessage());
        } else if (e instanceof BadRequestException) {
            return ResponseEntity.status(400).body(e.getMessage());
        } else if (e instanceof ForbiddenResourceException) {
            return ResponseEntity.status(403).body(e.getMessage());
        } else if (e instanceof ResourceNotFoundException) {
            return ResponseEntity.status(404).body(e.getMessage());
        } else {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
