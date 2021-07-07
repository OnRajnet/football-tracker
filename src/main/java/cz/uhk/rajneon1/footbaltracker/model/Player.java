package cz.uhk.rajneon1.footbaltracker.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Player extends User {

    private String refreshToken;
    @OneToMany(mappedBy = "player")
    private List<PlayerPerformancePerMatch> performances = new ArrayList<>();

    public Player() {
    }

    public Player(String login, String password) {
        super(login, password, UserRole.PLAYER);
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public List<PlayerPerformancePerMatch> getPerformances() {
        return performances;
    }

    public void setPerformances(List<PlayerPerformancePerMatch> performances) {
        this.performances = performances;
    }

    public void addPerformance(PlayerPerformancePerMatch performance) {
        performances.add(performance);
    }
}
