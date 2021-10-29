package edu.byu.cs.tweeter.client.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.service.backgroundTask.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.service.backgroundTask.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.service.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.client.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.service.observer.SimpleNotificationObserver;
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
