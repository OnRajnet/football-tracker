package cz.uhk.rajneon1.footbaltracker.model;

import javax.persistence.*;

@Entity
public class PlayerPerformancePerMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    private FootballMatch footballMatch;
    @ManyToOne
    private Player player;
    private int steps;
    private double distance;
    private double maxSpeed;
    private double minSpeed;
    private double avgSpeed;

    public PlayerPerformancePerMatch() {
    }

    public PlayerPerformancePerMatch(Player player, int steps, double distance, double maxSpeed, double minSpeed, double avgSpeed) {
        this.player = player;
        this.steps = steps;
        this.distance = distance;
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        this.avgSpeed = avgSpeed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FootballMatch getFootballMatch() {
        return footballMatch;
    }

    public void setFootballMatch(FootballMatch footballMatch) {
        this.footballMatch = footballMatch;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }
}
