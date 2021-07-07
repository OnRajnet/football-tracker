package cz.uhk.rajneon1.footbaltracker.rest.dto.request;

import cz.uhk.rajneon1.footbaltracker.exception.resources.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class RequestBodyVerifier {

    public void verifyNewFootballMatch(NewFootballMatchDto matchDto) throws BadRequestException {
        if (matchDto.getPlayersLogins() == null || matchDto.getPlayersLogins().isEmpty() || matchDto.getStartTime() < 1 || matchDto.getEndTime() < 1) {
            throw new BadRequestException("New football match request body is not valid.");
        }
    }

    public void verifyChangePassword(ChangePasswordDto passwordDto) throws BadRequestException {
        if (passwordDto.getNewPassword().equals("") || passwordDto.getOldPassword().equals("") || passwordDto.getNewPassword() == null || passwordDto.getOldPassword() == null) {
            throw new BadRequestException("Changing password request body is not valid.");
        }
    }

}
