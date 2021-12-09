package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.FollowsDAOInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactoryInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    private final DAOFactoryInterface factory;

    @Inject
    public FollowService(DAOFactoryInterface factory){
        this.factory = factory;
    }

    /** FOLLOWING */
    public FollowingResponse getFollowees(FollowingRequest request) {

        // check authorization
        AuthTokenDAOInterface adao = this.factory.getAuthTokenDAO();
        String dbAuthToken = adao.getAuthToken(request.getAuthToken().getToken());
        if (!request.getAuthToken().getToken().equals(dbAuthToken))
            throw new RuntimeException("[AuthError] unauthenticated request");

        try {
            FollowsDAOInterface dao = this.factory.getFollowsDAO();
            List<User> allFollowees = dao.getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
            List<User> responseFollowees = new ArrayList<>(request.getLimit());

            boolean hasMorePages = false;

            if (request.getLimit() > 0) {
                if (allFollowees != null) {
                    int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);

                    for (int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                        responseFollowees.add(allFollowees.get(followeesIndex));
                    }

                    hasMorePages = followeesIndex < allFollowees.size();
                }
            }
            return new FollowingResponse(responseFollowees, hasMorePages);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to get following " + e.getMessage());
        }
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {
        int followeesIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }
        return followeesIndex;
    }


    /** FOLLOWERS */
    public FollowersResponse getFollowers(FollowersRequest request){

        // check authorization
        AuthTokenDAOInterface adao = this.factory.getAuthTokenDAO();
        String dbAuthToken = adao.getAuthToken(request.getAuthToken().getToken());
        if (!request.getAuthToken().getToken().equals(dbAuthToken))
            throw new RuntimeException("[AuthError] unauthenticated request");

        try {
            FollowsDAOInterface dao = this.factory.getFollowsDAO();
            List<User> allFollowers = dao.getFollowers(request.getFolloweeAlias(), request.getLimit(), request.getLastFollowerAlias());
            List<User> responseFollowers = new ArrayList<>(request.getLimit());

            boolean hasMorePages = false;

            if (request.getLimit() > 0) {
                if (allFollowers != null) {
                    int followeesIndex = getFollowersStartingIndex(request.getLastFollowerAlias(), allFollowers);

                    for (int limitCounter = 0; followeesIndex < allFollowers.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                        responseFollowers.add(allFollowers.get(followeesIndex));
                    }

                    hasMorePages = followeesIndex < allFollowers.size();
                }
            }
            return new FollowersResponse(responseFollowers, hasMorePages);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to get followers " + e.getMessage());
        }
    }

    private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers) {
        int followersIndex = 0;
        if(lastFollowerAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowers.size(); i++) {
                if(lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followersIndex = i + 1;
                    break;
                }
            }
        }
        return followersIndex;
    }


    /** FOLLOW */
    public SimpleResponse follow(FollowsRequest request){

        // check authorization
        AuthTokenDAOInterface adao = this.factory.getAuthTokenDAO();
        String dbAuthToken = adao.getAuthToken(request.getAuthToken().getToken());
        if (!request.getAuthToken().getToken().equals(dbAuthToken))
            throw new RuntimeException("[AuthError] unauthenticated request");

        try {
            FollowsDAOInterface fdao = this.factory.getFollowsDAO();
            // already following?
            if (fdao.find(request.getFollowerAlias(), request.getFolloweeAlias())) {
                throw new RuntimeException("already following");
            }

            // add follows
            fdao.add(request.getFolloweeAlias(), request.getFollowerAlias(), request.getFolloweeName(), request.getFollowerName(),
                    request.getFolloweeImage(), request.getFollowerImage());

            // update counts:
            //  ++ the followee's followers count
            UserDAOInterface udao = this.factory.getUserDAO();
            int followerCount = udao.getFollowerCount(request.getFolloweeAlias());
            udao.updateUser(request.getFolloweeAlias(),"follower_count", followerCount+1);
            //  ++ currentUser's following count
            int followingCount = udao.getFollowingCount(request.getFollowerAlias());
            udao.updateUser(request.getFollowerAlias(),"followee_count", followingCount+1);

            return new SimpleResponse();

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to follow " + request.getFolloweeAlias() + " " + e.getMessage());
        }
    }

    /** UNFOLLOW */
    public SimpleResponse unfollow(FollowsRequest request){

        // check authorization
        AuthTokenDAOInterface adao = this.factory.getAuthTokenDAO();
        String dbAuthToken = adao.getAuthToken(request.getAuthToken().getToken());
        if (!request.getAuthToken().getToken().equals(dbAuthToken))
            throw new RuntimeException("[AuthError] unauthenticated request");

        try {
            FollowsDAOInterface fdao = this.factory.getFollowsDAO();
            // not following?
            if (!fdao.find(request.getFollowerAlias(), request.getFolloweeAlias())) {
                throw new RuntimeException("can't unfollow when not following");
            }

            // update counts:
            //  -- the followee's followers count
            UserDAOInterface udao = this.factory.getUserDAO();
            int followerCount = udao.getFollowerCount(request.getFolloweeAlias());
            udao.updateUser(request.getFolloweeAlias(),"follower_count", followerCount-1);
            //  -- currentUser's following count
            int followingCount = udao.getFollowingCount(request.getFollowerAlias());
            udao.updateUser(request.getFollowerAlias(),"followee_count", followingCount-1);

            // delete follows relationship
            fdao.delete(request.getFollowerAlias(),request.getFolloweeAlias());

            return new SimpleResponse();

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to unfollow " + request.getFolloweeAlias() + " " + e.getMessage());
        }
    }

    /** IS FOLLOWER */
    public IsFollowerResponse isFollower(IsFollowerRequest isFollowerRequest){
        try {
            FollowsDAOInterface fdao = this.factory.getFollowsDAO();
            if (fdao.find(isFollowerRequest.getFollowerAlias(), isFollowerRequest.getFolloweeAlias())) {
                return new IsFollowerResponse(true, true);
            } else {
                return new IsFollowerResponse(true, false);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to determine follow relationship " + e.getMessage());
        }
    }

    public List<String> getFollowersBatches(String alias){
        try {
            // get list of follower aliases
            FollowsDAOInterface followsdao = this.factory.getFollowsDAO();
            return followsdao.getAllFollowerAliases(alias);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to get batch of followers " + e.getMessage());
        }
    }
}
