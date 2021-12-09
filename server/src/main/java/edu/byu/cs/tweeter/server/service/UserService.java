package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.*;
import edu.byu.cs.tweeter.model.net.response.*;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import edu.byu.cs.tweeter.server.dao.factories.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.util.Pair;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class UserService {
    private final DAOFactoryInterface factory;

    @Inject
    public UserService(DAOFactoryInterface factory){
        this.factory = factory;
    }

    /** REGISTER */
    public RegisterResponse register(RegisterRequest request){
        try {
            // upload image to S3 getting back image url
            String url = this.factory.getImageDAO().uploadImage(request.getUsername(), request.getImage());

            // salt and hash password before uploading
            String saltAndHashedPassword = new SHA1Hashing().getNewPassword(request.getPassword());

            // check if user is not already a user?
            this.factory.getUserDAO().addUser(request.getUsername(), request.getFirstName(), request.getLastName(), url,
                    saltAndHashedPassword, 0, 0);

            User user = new User(request.getFirstName(), request.getLastName(), url);
            AuthToken authToken = new AuthToken();

            // add authToken
            this.factory.getAuthTokenDAO().addAuthToken(authToken, request.getUsername());

            return new RegisterResponse(user, authToken);

        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to register " + e.getMessage());
        }
    }


    /** LOGIN */
    public LoginResponse login(LoginRequest request){
        try {
            Pair<User, String> UserPassword = this.factory.getUserDAO().getUserByID(request.getUsername());

            System.out.println("GOT USER BY ID");

            boolean match = false;
            try {
                match = new SHA1Hashing().validatePassword(request.getPassword(), UserPassword.getSecond());
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            if (!match) {
                throw new RuntimeException("[BadRequest] - invalid password");
            }

            // add authToken to table
            AuthToken authToken = new AuthToken();
            this.factory.getAuthTokenDAO().addAuthToken(authToken, request.getUsername());

            return new LoginResponse(UserPassword.getFirst(), authToken);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to login" + e.getMessage());
        }
    }


    /** LOGOUT */
    public SimpleResponse logout(LogoutRequest request){
        try {
            this.factory.getAuthTokenDAO().deleteAuthToken(request.getAuthToken());

            System.out.println("DELETED AUTHTOKEN!");

            return new SimpleResponse();

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to logout");
        }
    }


    /** GET USER */
    public GetUserResponse getUser(GetUserRequest request){
        // authorized?
        AuthTokenDAOInterface adao = this.factory.getAuthTokenDAO();
        String dbAuthToken = adao.getAuthToken(request.getAuthToken().getToken());
        if (!request.getAuthToken().getToken().equals(dbAuthToken))
            throw new RuntimeException("[AuthError] unauthenticated request");

        try {
            Pair<User, String> UserPass = this.factory.getUserDAO().getUserByID(request.getAlias());

            if (UserPass.getFirst() == null) {
                throw new RuntimeException(String.format("[BadRequest] requested user %s does not exist", request.getAlias()));
            }

            return new GetUserResponse(UserPass.getFirst());

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to get user " + e.getMessage());
        }
    }


    /** COUNTS */
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request){
        try {
            UserDAOInterface udao = this.factory.getUserDAO();
            return new FollowingCountResponse(udao.getFollowingCount(request.getAlias()));
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to get following count " + e.getMessage());
        }
    }

    public FollowerCountResponse getFollowerCount(FollowerCountRequest request){
        try {
            UserDAOInterface udao = this.factory.getUserDAO();
            return new FollowerCountResponse(udao.getFollowerCount(request.getAlias()));
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("[BadRequest] - unable to get follower count " + e.getMessage());
        }
    }


    /** S&H */
    public static class SHA1Hashing {
        public String getNewPassword(String registerPassword) {
            // Store this in database
            String generatedSecuredPasswordHash = null;
            try {
                generatedSecuredPasswordHash = generateStrongPasswordHash(registerPassword);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            System.out.println("Generated Password: " + generatedSecuredPasswordHash);

            return generatedSecuredPasswordHash;
        }

        private boolean validatePassword(String originalPassword, String storedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
            String[] parts = storedPassword.split(":");
            Integer iterations = Integer.parseInt(parts[0]);
            byte[] salt = fromHex(parts[1]);
            byte[] hash = fromHex(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for (int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        }

        private byte[] fromHex(String hex){
            byte[] bytes = new byte[hex.length() / 2];
            for(int i = 0; i<bytes.length ;i++) {
                bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
            }
            return bytes;
        }

        private String generateStrongPasswordHash(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
            int iterations = 1000;
            char[] chars = password.toCharArray();
            byte[] salt = getSalt();

            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        }

        private static byte[] getSalt() throws NoSuchAlgorithmException {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return salt;
        }

        private String toHex(byte[] array) {
            BigInteger bi = new BigInteger(1, array);
            String hex = bi.toString(16);
            int paddingLength = (array.length * 2) - hex.length();
            if(paddingLength > 0) {
                return String.format("%0"  +paddingLength + "d", 0) + hex;
            } else{
                return hex;
            }
        }
    }
}
