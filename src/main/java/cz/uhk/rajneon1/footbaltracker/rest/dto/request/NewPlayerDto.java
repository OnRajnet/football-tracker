package cz.uhk.rajneon1.footbaltracker.rest.dto.request;

public class NewPlayerDto {

    private String login;
    private String plainPass;

    public NewPlayerDto() {
    }

    public NewPlayerDto(String login, String plainPass) {
        this.login = login;
        this.plainPass = plainPass;
    }

    public String getLogin() {
        return login;
    }

    public String getPlainPass() {
        return plainPass;
    }
}
