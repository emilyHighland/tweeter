package edu.byu.cs.tweeter.client.model.net;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.io.IOException;


public class ServerFacadeUnitTest {

    private RegisterRequest fakeRegisterRequest;
    private RegisterResponse fakeRegisterResponse;
    private Cache mockCache;
    private AuthToken authToken;
    private User user;

    private ServerFacade serverFacadeSpy;
    private TestCase Assertions;

    @BeforeEach
    public void setup(){
        // Create a spy on the class you are testing
        serverFacadeSpy = Mockito.spy(new ServerFacade());
        mockCache = Mockito.mock(Cache.class);

        authToken = new AuthToken();
        user = new User("emily","high","","");

        Mockito.when(mockCache.getCurrUserAuthToken()).thenReturn(authToken);
        Mockito.when(mockCache.getCurrUser()).thenReturn(user);
    }

    @Test
    public void register_Success() throws IOException, TweeterRemoteException {

        fakeRegisterRequest = new RegisterRequest("em", "high", "@em", "pass", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        RegisterResponse testResponse = serverFacadeSpy.register(fakeRegisterRequest,"/register");

        User fakeUser = new User("Allen","Anderson","@allen", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        fakeRegisterResponse = new RegisterResponse(fakeUser,authToken);


        Assertions.assertEquals(fakeRegisterResponse,testResponse);
    }


    @Test
    public void getFollowers_Success(){

    }


    @Test
    public void getFollowsCount_Success(){

    }

}
