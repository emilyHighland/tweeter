package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.SimpleResponse;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.util.FakeData;
import edu.byu.cs.tweeter.server.util.Pair;
import edu.byu.cs.tweeter.server.util.SaltedSHAHashing;

// need API gateway to know which type of responses out of lambda are good/bad
// SO catch, then prefix error/exception message with regex:
// 400 errors are request errors.... prefix "[Bad Request]" ....happen in service layer. invalid request or missing property you need.
// try/catch the daos server errors "[Server Error]"
public class UserService {

    private final DAOFactoryInterface factory;

    @Inject
    public UserService(DAOFactoryInterface factory){
        this.factory = factory;
    }

    /** REGISTER */
    public RegisterResponse register(RegisterRequest request){

        // upload image to S3 getting back image url
        String url = this.factory.getImageDAO().uploadImage(request.getUsername(), request.getImage());

        // salt and hash password before uploading
        Pair<String,String> saltAndHashed = new SaltedSHAHashing().getSecurePassword(request.getPassword());

        try {
            // check if user is not already a user?
            this.factory.getUserDAO().addUser(request.getUsername(), request.getFirstName(), request.getLastName(), url,
                    saltAndHashed.getFirst(), saltAndHashed.getSecond(),
                    0, 0);

            User user = new User(request.getFirstName(), request.getLastName(), url);
            AuthToken authToken = new AuthToken();

            // add authToken
            this.factory.getAuthTokenDAO().addAuthToken(authToken, request.getUsername());

            return new RegisterResponse(user, authToken);

        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest]");
        }
    }


    /** LOGIN */
    public LoginResponse login(LoginRequest request) {
//        try {
            // authenticate user to login
            Pair<User,String> UserAndPassword = this.factory.getUserDAO().getUserByID(request.getUsername());

            if (!authenticatePassword(request.getPassword(), UserAndPassword.getSecond())){
                throw new RuntimeException("[BadRequest] - invalid password");
            }

            // add authToken to table
            AuthToken authToken = new AuthToken();
            this.factory.getAuthTokenDAO().addAuthToken(authToken, request.getUsername());

            return new LoginResponse(UserAndPassword.getFirst(), authToken);

//        } catch(Exception e){
//            e.printStackTrace();
//            throw new RuntimeException("[BadRequest]");
//        }
    }


    /** LOGOUT */
    public SimpleResponse logout(LogoutRequest request){
        try {
            this.factory.getAuthTokenDAO().deleteAuthToken(request.getAuthToken());
            return new SimpleResponse();
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to logout");
        }
    }


    /** GET USER */
    public GetUserResponse getUser(GetUserRequest request){
        assert request.getAlias() != null;
        assert request.getAuthToken() != null;

        try {
            if (!isValidAuthToken(request.getAlias(), request.getAuthToken()))
                throw new RuntimeException("[AuthError] unauthenticated request");

            Pair<User,String> userPassword = this.factory.getUserDAO().getUserByID(request.getAlias());

            if (userPassword.getFirst() == null) {
                throw new RuntimeException(String.format("[BadRequest] requested user %s does not exist", request.getAlias()));
            }

            return new GetUserResponse(userPassword.getFirst());
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private boolean authenticatePassword(String suppliedPassword, String securePassword) {
        // salt & hash request password to see if it's the same as queried_result_password + salt
        String regeneratedPasswordToVerify = new SaltedSHAHashing().getSecurePassword(suppliedPassword, this.factory.getUserDAO().getUsersSalt());
        System.out.println("Regenerated Password: " + regeneratedPasswordToVerify);

        if (securePassword.equals(regeneratedPasswordToVerify)){
            System.out.println("Passwords are the same: " + securePassword.equals(regeneratedPasswordToVerify));
            return true;
        }
        return false;
    }

    private boolean isValidAuthToken(String alias, AuthToken currentAuthToken){
        String dbAuthToken = this.factory.getAuthTokenDAO().getAuthToken(alias);
        return dbAuthToken.equals(currentAuthToken.getToken());
    }





    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }
}
