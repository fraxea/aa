package org.example.controller;

import javafx.scene.image.Image;
import org.example.controller.responses.FieldResponses;
import org.example.controller.responses.RegisterResponses;
import org.example.model.ImageDatabase;
import org.example.model.UserDatabase;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Objects;

public class ProfileController implements RegisterResponses, FieldResponses {
    private static ProfileController controller;
    private final UserDatabase userDatabase;
    private final ImageDatabase imageDatabase;
    private final RegisterController registerController;
    private final LoginController loginController;
    
    private ProfileController() {
        userDatabase = UserDatabase.getInstance();
        imageDatabase = ImageDatabase.getInstance();
        registerController = RegisterController.getInstance();
        loginController = LoginController.getInstance();
    }
    
    public static ProfileController getInstance() {
        return controller == null ? controller = new ProfileController() : controller;
    }
    
    
    public String checkPasswordErrors(String password) {
        String result = registerController.checkPasswordErrors(password);
        return result == EMPTY_FIELD ? null : result;
    }
    
    public String checkUsernameErrors(String username) {
        String result;
        if ((result = registerController.checkFieldErrors(username)) != null) return result;
        if (userDatabase.getUserByUsername(username) != null &&
                !Objects.equals(userDatabase.getLoggedInUser().getUsername(), username)) return USERNAME_EXIST;
        return "\"" + username + "\" is OK!";
    }
    
    public String getUsername() {
        return userDatabase.getLoggedInUser().getUsername();
    }
    
    public String checkOldPasswordErrors(String password) {
        if (userDatabase.getLoggedInUser().isGuest()) return null;
        return loginController.checkPasswordErrors(getUsername(), password);
    }
    
    public void updateUserData(String username, String password, String imagePath) {
        userDatabase.getLoggedInUser().setUsername(username);
        userDatabase.getLoggedInUser().setPassword(password);
        userDatabase.getLoggedInUser().setImageName(imagePath);
    }
    
    public Image getUserImage() {
        return userDatabase.getLoggedInUser().getImage();
    }
    
    public boolean isFilePicture(File selectedFile) {
        String fileName = selectedFile.getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) extension = fileName.substring(i+1);
        else return false;
        return extension.equals("png");
    }
    
    public String addFile(File selectedFile) throws MalformedURLException {
        return imageDatabase.addImage(selectedFile);
    }
}
