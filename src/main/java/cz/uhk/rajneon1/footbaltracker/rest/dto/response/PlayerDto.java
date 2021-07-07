package cz.uhk.rajneon1.footbaltracker.rest.dto.response;

import java.util.List;

public class PlayerDto {

    private String login;
    private List<PerformanceDto> performances;

    public PlayerDto() {
    }

    public PlayerDto(String login, List<PerformanceDto> performances) {
        this.login = login;
        this.performances = performances;
    }
}
