package edu.byu.cs.tweeter.server.dao.dynamo;

import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FollowsDAOInterface;

import java.util.List;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class DynamoFollowsDAO implements FollowsDAOInterface {

    @Inject
    public DynamoFollowsDAO() {}



    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @return the followees.
     */
    @Override
    public List<User> getFollowees(String alias) {
        // return 'limit' number of followees of 'alias' from 'follows' table


        return null;
    }

    @Override
    public List<User> getFollowers(String alias) {
        // return 'limit' number of followers of 'alias' from 'follows' table

        return null;
    }

    @Override
    public void follow(String alias){
        //assert request.getAuthToken() != null;
        assert alias != null;

        // TODO: increment followee count
        // update() count:
        //  ++ currentUser's following count
        //  ++ the followee's followers count

    }

    @Override
    public void unfollow(String alias){
        //assert request.getAuthToken() != null;
        assert alias != null;

        // TODO: decrement followee count
        // update() count:
        //  -- currentUser's following count
        //  -- the followee's followers count

    }

}
