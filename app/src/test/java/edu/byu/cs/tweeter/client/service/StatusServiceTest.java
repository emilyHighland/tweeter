package edu.byu.cs.tweeter.client.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import util.FakeData;
import util.Pair;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class StatusServiceTest {

    private Cache mockCache;
    private StatusService.StoryObserver mockObserver;
    private StatusService statusServiceSpy;

    private User user;
    private int limit;
    private Status lastStatus;
    private List<Status> fakeStory;
    private CountDownLatch countDownLatch;


    @BeforeEach
    public void setup(){
        //** Mock UserService
        // Create mock dependencies
        mockCache = Mockito.mock(Cache.class);
        mockObserver = Mockito.mock(StatusService.StoryObserver.class);

        // Create a spy on the class you are testing
        statusServiceSpy = Mockito.spy(new StatusService());

        Pair<List<Status>, Boolean> fakePair = new FakeData().getPageOfStatus(null,10);
        fakeStory = fakePair.getFirst();

        user = new User("Allen","Anderson","@allen", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        limit = 10;
        lastStatus = null;

        // Set mock Cache
        Cache.setInstance(mockCache);
    }

    @Test
    public void getStory_Success() throws InterruptedException {

        Answer<Void> successAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // count down latch
                countDownLatch.countDown();
                return null;
            }
        };

        Mockito.doAnswer(successAnswer).when(mockObserver).handleSuccess(Mockito.anyList(),Mockito.anyBoolean());

        statusServiceSpy.getStory(user,limit,lastStatus,mockObserver);
        awaitCountDownLatch();
        Mockito.verify(mockObserver).handleSuccess(Mockito.anyList(), Mockito.anyBoolean());

    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }


}
