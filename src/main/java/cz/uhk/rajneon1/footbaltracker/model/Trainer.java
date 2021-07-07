package cz.uhk.rajneon1.footbaltracker.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Trainer extends User {

    @OneToMany(mappedBy = "trainer")
    private List<FootballMatch> footballMatches = new ArrayList<>();

    public Trainer() {
    }

    public Trainer(String login, String password) {
        super(login, password, UserRole.TRAINER);
    }

    public List<FootballMatch> getFootballMatches() {
        return footballMatches;
    }

    public void setFootballMatches(List<FootballMatch> footballMatches) {
        this.footballMatches = footballMatches;
    }

    public void addFootballMatch(FootballMatch match) {
        footballMatches.add(match);
    }
}
