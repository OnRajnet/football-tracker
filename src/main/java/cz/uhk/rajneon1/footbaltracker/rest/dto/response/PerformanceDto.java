package cz.uhk.rajneon1.footbaltracker.rest.dto.response;

import cz.uhk.rajneon1.footbaltracker.model.PlayerPerformancePerMatch;

public class PerformanceDto {

    private int footballMatchId;
    private String player;
    private int steps;
    private double distance;
    private double maxSpeed;
    private double minSpeed;
    private double avgSpeed;

    public PerformanceDto() {
    }

    public PerformanceDto(PlayerPerformancePerMatch performance) {
        footballMatchId = performance.getFootballMatch().getId();
        player = performance.getPlayer().getLogin();
        steps = performance.getSteps();
        distance = performance.getDistance();
        maxSpeed = performance.getMaxSpeed();
        minSpeed = performance.getMinSpeed();
        avgSpeed = performance.getAvgSpeed();
    }
}
