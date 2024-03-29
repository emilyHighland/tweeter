package edu.byu.cs.tweeter.client.model.net;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.*;
import edu.byu.cs.tweeter.model.net.response.*;

import java.io.IOException;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://fk29xvj26f.execute-api.us-west-2.amazonaws.com/tweeter";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    public RegisterResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        RegisterResponse response = clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        LoginResponse response = clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the users that the user specified in the request is a follower. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the followers.
     */
    public FollowersResponse getFollowers(FollowersRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        FollowersResponse response = clientCommunicator.doPost(urlPath, request, null, FollowersResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the list of statuses of a user.
     */
    public StoryResponse getStory(StoryRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        StoryResponse response = clientCommunicator.doPost(urlPath, request, null, StoryResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the list of statuses of a user's followers
     */
    public FeedResponse getFeed(FeedRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        FeedResponse response = clientCommunicator.doPost(urlPath, request, null, FeedResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }


    public PostStatusResponse postStatus(PostStatusRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        PostStatusResponse response = clientCommunicator.doPost(urlPath, request, null, PostStatusResponse.class);
        return response;
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        IsFollowerResponse response = clientCommunicator.doPost(urlPath, request, null, IsFollowerResponse.class);
        return response;
    }


    public SimpleResponse follow(FollowsRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        SimpleResponse response = clientCommunicator.doPost(urlPath, request, null, SimpleResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public SimpleResponse unfollow(FollowsRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        SimpleResponse response = clientCommunicator.doPost(urlPath, request, null, SimpleResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        FollowingCountResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingCountResponse.class);

        return response;
    }

    public FollowerCountResponse getFollowerCount(FollowerCountRequest request, String urlPath)
        throws IOException, TweeterRemoteException {
        FollowerCountResponse response = clientCommunicator.doPost(urlPath, request, null, FollowerCountResponse.class);

        return response;
    }


    public GetUserResponse getUser(GetUserRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        GetUserResponse response = clientCommunicator.doPost(urlPath, request, null, GetUserResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }


    public SimpleResponse logout(LogoutRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        SimpleResponse response = clientCommunicator.doPost(urlPath, request, null, SimpleResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }
}