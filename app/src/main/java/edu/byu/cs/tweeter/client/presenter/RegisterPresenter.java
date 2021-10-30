package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class RegisterPresenter extends AuthenticatePresenter{

    private final UserService userService;

    public RegisterPresenter(AuthenticateView view){
        super(view);
        userService = new UserService();
    }

    public void register(String firstName, String lastName, String alias, String password, Bitmap image){

        String validationError = validateRegistration(firstName,lastName,alias,password,image);

        if (validationError == null) {
            getAuthenticateView().clearValidationError();
            view.displayInfoMessage("Registering...");

            userService.register(firstName, lastName, alias, password, bitmapToString(image),
                    new GetRegisterObserver());
        }
        else {
            getAuthenticateView().displayValidationError(validationError);
        }
    }

    public String validateRegistration(String firstName, String lastName, String alias,
                                     String password, Bitmap imageToUpload){
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
            return "Last Name cannot be empty.";
        }
        if (alias.length() == 0) {
            return "Alias cannot be empty.";
        }
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        if (imageToUpload == null) {
            return "Profile image must be uploaded.";
        }
        return null;
    }

    private String bitmapToString(Bitmap image){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();
         return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    private class GetRegisterObserver extends GetAuthenticateObserver implements UserService.RegisterObserver {}
}
