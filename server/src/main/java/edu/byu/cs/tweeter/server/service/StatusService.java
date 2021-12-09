package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.JsonSerializer;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.FeedDAOInterface;
import edu.byu.cs.tweeter.server.dao.FollowsDAOInterface;
import edu.byu.cs.tweeter.server.dao.StoryDAOInterface;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactoryInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatusService {

    private final DAOFactoryInterface factory;

    @Inject
    public StatusService(DAOFactoryInterface factory){
        this.factory = factory;
    }


    /** STORY */
    public StoryResponse getStory(StoryRequest request) {

        // check authorization
        AuthTokenDAOInterface adao = this.factory.getAuthTokenDAO();
        String dbAuthToken = adao.getAuthToken(request.getAuthToken().getToken());
        if (!request.getAuthToken().getToken().equals(dbAuthToken))
            throw new RuntimeException("[AuthError] unauthenticated request");

        try {
            long lastTimestamp = statusDateToUnix(request.getLastStatus());
            StoryDAOInterface sdao = this.factory.getStoryDAO();
            List<Status> allStatuses = sdao.getStory(request.getTargetAlias(), request.getLimit(), request.getLastStatus(), lastTimestamp);
            List<Status> responseStory = new ArrayList<>();

            boolean hasMorePages = false;

            if (request.getLimit() > 0) {
                if (allStatuses != null) {
                    int statusIndex = getStoryStartingIndex(request.getLastStatus(), allStatuses);

                    for (int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                        responseStory.add(allStatuses.get(statusIndex));
                    }

                    hasMorePages = statusIndex < allStatuses.size();
                }
            }
            return new StoryResponse(responseStory, hasMorePages);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(String.format("[BadRequest] - unable to get %s's story " + e.getMessage(), request.getTargetAlias()));
        }
    }

    private int getStoryStartingIndex(Status lastStatus, List<Status> allStatuses){
        int statusIndex = 0;

        if (lastStatus != null){
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                    break;
                }
            }
        }
        return statusIndex;
    }

    private long statusDateToUnix(Status lastStatus) {
        if (lastStatus != null) {

            try {
                String date = lastStatus.getDate();

                System.out.println("lastStatus date: " + date);
//
//        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

                Date tempDate = new SimpleDateFormat("MMM d yyyy h:mm aaa").parse(date);
                long lastStatusTimestamp = tempDate.getTime();

                System.out.println("lastTimestamp: " + lastStatusTimestamp);

                return lastStatusTimestamp;
            } catch (ParseException e){
                e.printStackTrace();
                throw new RuntimeException("[BadRequest] - unable to parse last status date");
            }
        }
        return 0;
    }


    /** FEED */
    public FeedResponse getFeed(FeedRequest request){

        // check authorization
        AuthTokenDAOInterface adao = this.factory.getAuthTokenDAO();
        String dbAuthToken = adao.getAuthToken(request.getAuthToken().getToken());
        if (!request.getAuthToken().getToken().equals(dbAuthToken))
            throw new RuntimeException("[AuthError] unauthenticated request");

        try {
            long lastTimestamp = statusDateToUnix(request.getLastStatus());
            FeedDAOInterface fdao = this.factory.getFeedDAO();
            List<Status> allStatuses = fdao.getFeed(request.getTargetAlias(), request.getLimit(), request.getLastStatus(), lastTimestamp);
            List<Status> responseFeed = new ArrayList<>();

            boolean hasMorePages = false;

            if (request.getLimit() > 0) {
                if (allStatuses != null) {
                    int statusIndex = getFeedStartingIndex(request.getLastStatus(), allStatuses);

                    for (int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                        responseFeed.add(allStatuses.get(statusIndex));
                    }

                    hasMorePages = statusIndex < allStatuses.size();
                }
            }
            return new FeedResponse(responseFeed, hasMorePages);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to get feed " + e.getMessage());
        }
    }

    private int getFeedStartingIndex(Status lastStatus, List<Status> allStatuses){
        int statusIndex = 0;

        if (lastStatus != null){
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                    break;
                }
            }
        }
        return statusIndex;
    }


    /** POST STATUS */
    public PostStatusResponse postStatus(PostStatusRequest request, long newTimestamp){

        // check authorization
//        AuthTokenDAOInterface adao = this.factory.getAuthTokenDAO();
//        String dbAuthToken = adao.getAuthToken(request.getAuthToken().getToken());
//        if (!request.getAuthToken().getToken().equals(dbAuthToken))
//            throw new RuntimeException("[AuthError] unauthenticated request");

        try {
            // add to story
            StoryDAOInterface sdao = this.factory.getStoryDAO();
            sdao.addStatus(request.getAlias(), request.getStatus(), newTimestamp);

            System.out.println("ADDED POST TO STORY");

            return new PostStatusResponse(true);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to post status " + e.getMessage());
        }
    }

    public void feedBatchWrite(List<String> followers, Status status, long UNIXmillis){
        try {
            FeedDAOInterface fdao = this.factory.getFeedDAO();
            fdao.feedBatchWrite(followers, JsonSerializer.serialize(status), UNIXmillis);
            System.out.println("ADDED FEED BATCH");
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to batch write status to followers feeds " + e.getMessage());
        }
    }
}
