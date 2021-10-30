package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.net.MalformedURLException;
import java.text.ParseException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

// testing: assume everything around it works correctly,
// so can check if what we're testing actually works correctly
public class MainPresenterUnitTest {

    private final String TEST_POST = "hello world..";

    private MainPresenter.MainView mockView;
    private UserService mockUserService;
    private StatusService mockStatusService;
    private Cache mockCache;

    private MainPresenter mainPresenterSpy;

    private AuthToken authToken;
    private User user;


    @BeforeEach
    public void setup(){
        //** Mock UserService
        // Create mock dependencies
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockUserService = Mockito.mock(UserService.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);

        // Create a spy on the class you are testing
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

        // use mock objects
        Mockito.when(mainPresenterSpy.getUserService()).thenReturn(mockUserService);
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

        authToken = new AuthToken();
        user = new User("emily","high","","");

        Mockito.when(mockCache.getCurrUserAuthToken()).thenReturn(authToken);
        Mockito.when(mockCache.getCurrUser()).thenReturn(user);

        // Set mock Cache
        Cache.setInstance(mockCache);
    }

    //** LOGOUT TESTING **//
    @Test
    public void testLogout_Success(){
        // answer grabs whatever parameter is sent to UserService.logout and calls handleSuccess on it
        // bypassing the background thread
        Answer<Void> successAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                UserService.LogoutObserver observer =  invocation.getArgumentAt(0,
                        UserService.LogoutObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        // get any parameter when mockUserService calls logout
        Mockito.doAnswer(successAnswer).when(mockUserService).logout(Mockito.any());

        mainPresenterSpy.logout();

        // confirm that code interacted with those mock dependencies in the correct way
        // verify that the mockView called these methods
        Mockito.verify(mockView).displayInfoMessage("Logging Out...");
        Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).logoutUser();

        Mockito.verify(mockCache).clearCache();
    }

    @Test
    public void testLogout_Fail(){
        // assuming my userService calls handleFailure and passes "my failure string",
        // what does my presenter do with that?
        // I want to make sure my presenter does the right thing
        Answer<Void> failureAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                UserService.LogoutObserver observer =  invocation.getArgumentAt(0,
                        UserService.LogoutObserver.class);
                observer.handleFailure("my failure string");
                return null;
            }
        };

        Mockito.doAnswer(failureAnswer).when(mockUserService).logout(Mockito.any());

        mainPresenterSpy.logout();

        // confirm that code interacted with those mock dependencies in the correct way
        // verify that the mockView called these methods
        Mockito.verify(mockView).displayInfoMessage("Logging Out...");
        Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).displayErrorMessage("my failure string");
        Mockito.verify(mockView, Mockito.times(0)).logoutUser();

        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
    }

    //** POST STATUS TESTING **//
    @Test
    public void testPostStatus_Success() throws ParseException, MalformedURLException {

        Answer<Void> successAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostStatusObserver observer = invocation.getArgumentAt(3,
                        StatusService.PostStatusObserver.class);
                checkParams(invocation);
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(successAnswer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any());

        mainPresenterSpy.postStatus(TEST_POST);

        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).displayInfoMessage("Successfully Posted!");
    }

    @Test
    public void testPostStatus_Fail(){
        Answer<Void> failedAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                StatusService.PostStatusObserver observer = invocation.getArgumentAt(3,
                        StatusService.PostStatusObserver.class);
                checkParams(invocation);
                observer.handleFailure("my failure message");
                return null;
            }
        };

        Mockito.doAnswer(failedAnswer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any());

        mainPresenterSpy.postStatus(TEST_POST);

        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).clearInfoMessage();
        Mockito.verify(mockView).displayErrorMessage("my failure message");
    }

    private void checkParams(InvocationOnMock invocation){
        AuthToken fakeAuthToken = invocation.getArgumentAt(1, AuthToken.class);
        String fakeAlias = invocation.getArgumentAt(2, String.class);
        Assertions.assertEquals(fakeAuthToken, authToken);
        Assertions.assertEquals(fakeAlias, user.alias);
    }
}
