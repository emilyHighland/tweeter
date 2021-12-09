package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Handler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import util.Pair;

import java.io.IOException;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends AuthenticationTask {
    private static final String LOG_TAG = "RegisterTask";

    private String firstName;
    private String lastName;
    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private String image;

    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(messageHandler, username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest(firstName, lastName, username, password, image);
        RegisterResponse response = SF.register(request, "/register");
        return new Pair<>(response.getUser(), response.getAuthToken());
    }
}
