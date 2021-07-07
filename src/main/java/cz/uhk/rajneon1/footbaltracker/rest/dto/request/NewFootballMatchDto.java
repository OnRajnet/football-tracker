package cz.uhk.rajneon1.footbaltracker.rest.dto.request;

import java.util.List;

public class NewFootballMatchDto {

    private List<String> playersLogins;
    private long startTime;
    private long endTime;

    public NewFootballMatchDto() {
    }

    public NewFootballMatchDto(List<String> playersLogins, long startTime, long endTime) {
        this.playersLogins = playersLogins;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public List<String> getPlayersLogins() {
        return playersLogins;
    }

    public void setPlayersLogins(List<String> playersLogins) {
        this.playersLogins = playersLogins;
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

}
