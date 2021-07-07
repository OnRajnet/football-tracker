package cz.uhk.rajneon1.footbaltracker.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class FootballMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToMany(mappedBy = "footballMatch")
    private List<PlayerPerformancePerMatch> playersPerformances;
    @ManyToOne
    private Trainer trainer;

    private long startTime;
    private long endTime;

    private int totalSteps;
    private double avgSteps;
    private double totalDistance;
    private double avgDistance;
    private double avgSpeed;

    public FootballMatch() {

    }

    public FootballMatch(List<PlayerPerformancePerMatch> playersPerformances, Trainer trainer, long startTime, long endTime) {
        this.playersPerformances = playersPerformances;
        this.trainer = trainer;
        this.startTime = startTime;
        this.endTime = endTime;

        totalSteps = playersPerformances.stream().mapToInt(PlayerPerformancePerMatch::getSteps).sum();
        avgSteps = totalSteps / (double) playersPerformances.size();
        totalDistance = playersPerformances.stream().mapToDouble(PlayerPerformancePerMatch::getDistance).sum();
        avgDistance = totalDistance / (double) playersPerformances.size();
        avgSpeed = playersPerformances.stream().mapToDouble(PlayerPerformancePerMatch::getAvgSpeed).sum() / (double) playersPerformances.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<PlayerPerformancePerMatch> getPlayersPerformances() {
        return playersPerformances;
    }

    public void setPlayersPerformances(List<PlayerPerformancePerMatch> playersPerformances) {
        this.playersPerformances = playersPerformances;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public double getAvgSteps() {
        return avgSteps;
    }

    public void setAvgSteps(double avgSteps) {
        this.avgSteps = avgSteps;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getAvgDistance() {
        return avgDistance;
    }

    public void setAvgDistance(double avgDistance) {
        this.avgDistance = avgDistance;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }
}
