package org.example.controller;

import org.example.controller.responses.FieldResponses;
import org.example.controller.responses.LoginResponses;
import org.example.model.UserDatabase;

public class LoginController implements FieldResponses, LoginResponses {
    private static LoginController controller;
    private final UserDatabase userDatabase;
    private final RegisterController registerController;
    
    private LoginController() {
        userDatabase = UserDatabase.getInstance();
        registerController = RegisterController.getInstance();
    }
    
    public static LoginController getInstance() {
        return controller == null ? controller = new LoginController() : controller;
    }

    public String checkUsernameErrors(String username) {
        String result;
        if ((result = registerController.checkFieldErrors(username)) != null) return result;
        return userDatabase.getUserByUsername(username) == null ? USERNAME_NOT_EXIST : null;
    }
    
    public String checkPasswordErrors(String username, String password) {
        String result;
        if ((result = registerController.checkFieldErrors(username)) != null) return result;
        if (!userDatabase.getUserByUsername(username).isPasswordCorrect(password))
            return INCORRECT_PASSWORD;
        return null;
    }

    public void login(String username) {
        userDatabase.setLoggedInUser(userDatabase.getUserByUsername(username));
    }

}
