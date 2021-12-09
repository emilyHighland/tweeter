package edu.byu.cs.tweeter.server.sqs.DTO;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

public class UpdateFeedDTO {

    private String userAlias;
    private List<String> followers;
    private Status status;
    private long UNIXmillis;

    private UpdateFeedDTO(){}

    public UpdateFeedDTO(String userAlias, List<String> followers, Status status, long UNIXmillis) {
        this.userAlias = userAlias;
        this.followers = followers;
        this.status = status;
        this.UNIXmillis = UNIXmillis;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getUNIXmillis() {
        return UNIXmillis;
    }

    public void setUNIXmillis(long UNIXmillis) {
        this.UNIXmillis = UNIXmillis;
    }
}
