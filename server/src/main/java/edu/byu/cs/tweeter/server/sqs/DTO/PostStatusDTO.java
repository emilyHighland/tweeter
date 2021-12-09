package edu.byu.cs.tweeter.server.sqs.DTO;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusDTO {

    private String userAlias;
    private Status newStatus;
    private long UNIXmillis;

    private PostStatusDTO(){}

    public PostStatusDTO(String userAlias, Status newStatus, long UNIXmillis) {
        this.userAlias = userAlias;
        this.newStatus = newStatus;
        this.UNIXmillis = UNIXmillis;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public Status getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Status newStatus) {
        this.newStatus = newStatus;
    }

    public long getUNIXmillis() {
        return UNIXmillis;
    }

    public void setUNIXmillis(long UNIXmillis) {
        this.UNIXmillis = UNIXmillis;
    }
}
