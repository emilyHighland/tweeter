//package edu.byu.cs.tweeter.server.service;
//
//import edu.byu.cs.tweeter.model.domain.AuthToken;
//import edu.byu.cs.tweeter.model.domain.Status;
//import edu.byu.cs.tweeter.model.domain.User;
//import edu.byu.cs.tweeter.model.net.request.StoryRequest;
//import edu.byu.cs.tweeter.model.net.response.StoryResponse;
//import edu.byu.cs.tweeter.server.dao.dynamo.DynamoStoryDAO;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class StatusServiceTest {
//
//    private StoryRequest request;
//    private StoryResponse expectedResponse;
//    private DynamoStoryDAO mockStoryDAO;
//    private StatusService statusServiceSpy;
//
//    @BeforeEach
//    public void setup() {
//        AuthToken authToken = new AuthToken();
//
//        User currentUser = new User("FirstName", "LastName", null);
//
//        // (String post, User user, String datetime, List<String> urls, List<String> mentions)
//        List<String> urls;
//        List<String> mentions;
//
//        Status resultStatus1 = new Status("hi",currentUser,"NOV 1 2021 9:00 am", null, null);
//        Status resultStatus2 = new Status(":)",currentUser,"NOV 2 2021 8:00 am", null, null);
//        Status resultStatus3 = new Status("hello",currentUser,"NOV 3 2021 7:00 am", null, null);
//
//        // Setup a request object to use in the tests
//        request = new StoryRequest(authToken, currentUser.getAlias(), 3, null);
//
//        // Setup a mock FollowDAO that will return known responses
//        expectedResponse = new StoryResponse(Arrays.asList(resultStatus1, resultStatus2, resultStatus3), false);
//        mockStoryDAO = Mockito.mock(DynamoStoryDAO.class);
//        Mockito.when(mockStoryDAO.getStory(request)).thenReturn(expectedResponse);
//
//        statusServiceSpy = Mockito.spy(StatusService.class);
//        Mockito.when(statusServiceSpy.getStoryDAO()).thenReturn(mockStoryDAO);
//    }
//
//    /**
//     * Verify that the {@link StatusService#getStory(StoryRequest)}
//     * method returns the same result as the {@link DynamoStoryDAO} class.
//     */
//    @Test
//    public void testGetStory_Success() {
//        StoryResponse response = statusServiceSpy.getStory(request);
//        Assertions.assertEquals(expectedResponse, response);
//    }
//}
