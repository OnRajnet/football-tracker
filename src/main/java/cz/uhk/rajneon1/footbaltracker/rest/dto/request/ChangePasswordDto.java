package cz.uhk.rajneon1.footbaltracker.rest.dto.request;

public class ChangePasswordDto {

    private String newPassword;
    private String oldPassword;

    public ChangePasswordDto() {
    }

    public ChangePasswordDto(String newPassword, String oldPassword) {
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }
}
