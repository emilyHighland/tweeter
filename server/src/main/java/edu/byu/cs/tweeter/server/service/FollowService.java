package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.FollowsRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.server.dao.FollowsDAOInterface;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.util.FakeData;

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

    /** FOLLOWEES */
    public FollowingResponse getFollowees(FollowingRequest request) {
        try {
            // TODO: Generates dummy data. Replace with a real implementation.
            assert request.getLimit() > 0;
            assert request.getFollowerAlias() != null;

            FollowsDAOInterface dao = this.factory.getFollowsDAO();
            List<User> allFollowees = dao.getFollowees(request.getFollowerAlias());   // getDummyFollowees();
            List<User> responseFollowees = new ArrayList<>(request.getLimit());

            boolean hasMorePages = false;

            if(request.getLimit() > 0) {
                if (allFollowees != null) {
                    int followeesIndex = getFolloweesStartingIndex(request.getLastFolloweeAlias(), allFollowees);

                    for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                        responseFollowees.add(allFollowees.get(followeesIndex));
                    }

                    hasMorePages = followeesIndex < allFollowees.size();
                }
            }

            return new FollowingResponse(responseFollowees, hasMorePages);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
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
        try{
            // TODO: Generates dummy data. Replace with a real implementation.
            assert request.getLimit() > 0;
            assert request.getFolloweeAlias() != null;

            FollowsDAOInterface dao = this.factory.getFollowsDAO();
            List<User> allFollowers = dao.getFollowers(request.getFolloweeAlias()); // getDummyFollowees();
            List<User> responseFollowers = new ArrayList<>(request.getLimit());

            boolean hasMorePages = false;

            if(request.getLimit() > 0) {
                if (allFollowers != null) {
                    int followeesIndex = getFollowersStartingIndex(request.getLastFollowerAlias(), allFollowers);

                    for(int limitCounter = 0; followeesIndex < allFollowers.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                        responseFollowers.add(allFollowers.get(followeesIndex));
                    }

                    hasMorePages = followeesIndex < allFollowers.size();
                }
            }

            return new FollowersResponse(responseFollowers, hasMorePages);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
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


    /** FOLLOW & UNFOLLOW */
    public SimpleResponse follow(FollowsRequest request){
        try {
            FollowsDAOInterface dao = this.factory.getFollowsDAO();
            // check if authToken is valid
            request.getAuthToken();
            // check if already following??


            dao.follow(request.getUserAlias());

            return new SimpleResponse();
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
        }
    }

    public SimpleResponse unfollow(FollowsRequest request){
        try {
            FollowsDAOInterface dao = this.factory.getFollowsDAO();
            // check if authToken is valid
            request.getAuthToken();
            // check if already following??

            dao.unfollow(request.getUserAlias());

            return new SimpleResponse();
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
        }
    }


    /** COUNTS */
    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param follower the User whose count of how many following is desired.
     * @return said count.
     */
    public Integer getFolloweeCount(User follower) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert follower != null;
        return getDummyFollowees().size();
    }

    public Integer getFollowerCount(User followee) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert followee != null;
        // get count from follows table?
        return getDummyFollowers().size();
    }



    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return getFakeData().getFakeUsers();
    }

    List<User> getDummyFollowers() {
        return getFakeData().getFakeUsers();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }

}
