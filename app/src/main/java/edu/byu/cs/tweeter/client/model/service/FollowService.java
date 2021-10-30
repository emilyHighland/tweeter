package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.FollowTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.UnfollowTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.model.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

// Operations that relate to followers or followees
// run background tasks
public class FollowService {

    private Service service;

    public FollowService(){
        service = new Service();
    }

    /** FOLLOWING */
    public interface FollowingObserver extends PagedObserver<User> { }

    public void getFollowing(User targetUser, int pageLimit, User lastFollowee, FollowingObserver observer){
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                targetUser, pageLimit, lastFollowee, new GetFollowingHandler(observer));
        service.runTask(getFollowingTask);
    }


    /** FOLLOWERS */
    public interface FollowersObserver extends PagedObserver<User> { }

    public void getFollowers(User targetUser, int pageLimit, User lastFollower, FollowersObserver observer){
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                targetUser, pageLimit, lastFollower, new GetFollowersHandler(observer));
        service.runTask(getFollowersTask);
    }


    /** FOLLOWER COUNT */

    public void getFollowerCount(User selectedUser, ExecutorService executor,
                                 CountObserver observer){
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer));
        executor.execute(followersCountTask);
    }


    /** FOLLOWING COUNT */
//    public interface FollowingCountObserver extends CountObserver { }

    public void getFollowingCount(User selectedUser, ExecutorService executor,
                                  CountObserver observer){
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }

    /** IS FOLLOWER */

    public void getIsFollowerTask(User currentUser, User selectedUser,
                                  IsFollowerObserver observer){
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                currentUser, selectedUser, new IsFollowerHandler(observer));
        service.runTask(isFollowerTask);
    }


    /** FOLLOW */
    public interface FollowObserver extends SimpleNotificationObserver { }

    public void getFollowTask(User selectedUser, FollowService.FollowObserver observer){
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(observer));
        service.runTask(followTask);
    }


    /** UNFOLLOW */
    public interface UnfollowObserver extends SimpleNotificationObserver { }

    public void getUnfollowTask(User selectedUser, FollowService.UnfollowObserver observer){
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(observer));
        service.runTask(unfollowTask);
    }
}
