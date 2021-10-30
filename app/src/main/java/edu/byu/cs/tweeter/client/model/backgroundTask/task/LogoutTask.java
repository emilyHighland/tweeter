package edu.byu.cs.tweeter.client.model.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthorizedTask {
    private static final String LOG_TAG = "LogoutTask";

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler, authToken);
    }

    @Override
    protected void runTask() throws IOException {

    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {

    }
}
