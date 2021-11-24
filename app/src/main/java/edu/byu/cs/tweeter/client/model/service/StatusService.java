package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.GetFeedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.GetStoryTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.task.PostStatusTask;
import edu.byu.cs.tweeter.client.model.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    private Service service;

    public StatusService(){
        service = new Service();
    }


    // STATUS
    public interface PostStatusObserver extends SimpleNotificationObserver { }

    public void postStatus(String post, AuthToken authToken, String alias, PostStatusObserver observer){

        PostStatusTask statusTask = new PostStatusTask(post, authToken,
                alias, new PostStatusHandler(observer));
        service.runTask(statusTask);
    }

    // STORY
    public interface StoryObserver extends PagedObserver<Status> { }

    public void getStory(User targetUser, int PAGE_SIZE, Status lastStatus,
                         StoryObserver observer){
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                targetUser, PAGE_SIZE, lastStatus, new GetStoryHandler(observer));
        service.runTask(getStoryTask);
    }


    // FEED
    public interface FeedObserver extends PagedObserver<Status> { }

    public void getFeed(User targetUser, int PAGE_SIZE, Status lastStatus,
                               StatusService.FeedObserver observer){
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                targetUser, PAGE_SIZE, lastStatus, new GetFeedHandler(observer));
        service.runTask(getFeedTask);
    }
}
