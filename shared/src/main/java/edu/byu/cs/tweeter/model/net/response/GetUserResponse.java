package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

import java.util.Objects;

public class GetUserResponse extends Response{

    User user;

    public GetUserResponse(String message) {
        super(false, message);
    }

    public GetUserResponse(User user){
        super(true, null);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetUserResponse response = (GetUserResponse) o;
        return Objects.equals(user, response.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
