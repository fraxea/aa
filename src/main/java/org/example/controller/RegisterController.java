package org.example.controller;

import org.example.controller.responses.FieldResponses;
import org.example.controller.responses.RegisterResponses;
import org.example.model.ImageDatabase;
import org.example.model.User;
import org.example.model.UserDatabase;

public class RegisterController implements FieldResponses, RegisterResponses {
    private static RegisterController controller;
    private final UserDatabase userDatabase;
    
    private RegisterController() {
        userDatabase = UserDatabase.getInstance();
    }
    
    public static RegisterController getInstance() {
        return controller == null ? controller = new RegisterController() : controller;
    }
    
    public void guest() {
        int number = 1;
        String username = "Guest";
        while (userDatabase.getUserByUsername(username + number) != null)
            number++;
        register(username + number, null, "imagePreview");
    }

    public void register(String username, String password, String imageName) {
        User user = new User(username, password, imageName);
        userDatabase.getUsers().add(user);
        userDatabase.setLoggedInUser(user);
    }
    
    protected String checkFieldErrors(String field) {
        if (field.isBlank()) return EMPTY_FIELD;
        if (field.length() < 4) return SHORT_FIELD;
        if (field.length() > 14) return LONG_FIELD;
        return null;
    }

    public String checkUsernameErrors(String username) {
        String result;
        if ((result = checkFieldErrors(username)) != null) return result;
        if (userDatabase.getUserByUsername(username) != null) return USERNAME_EXIST;
        return "\"" + username + "\" is OK!";
    }

    public String checkPasswordErrors(String password) {
        String result;
        if ((result = checkFieldErrors(password)) != null) return result;
        return isPasswordStrong(password) ? STRONG_PASSWORD : WEAK_PASSWORD;
    }
    
    public void exit() {
        userDatabase.writeDataInFile();
        ImageDatabase.getInstance().writeDataInFile();
    }

    private boolean isPasswordStrong(String password) {
        return password.matches(".*[A-Z].*")
            && password.matches(".*[a-z].*")
            && password.matches(".*[0-9].*");
    }

    public boolean isGuest() {
        return userDatabase.getLoggedInUser().isGuest();
    }

    public void deleteAccount() {
        userDatabase.getUsers().remove(userDatabase.getLoggedInUser());
    }
}
