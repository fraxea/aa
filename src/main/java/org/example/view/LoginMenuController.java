package org.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.controller.LoginController;

import java.io.IOException;

public class LoginMenuController {
    private final LoginController controller = LoginController.getInstance();
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label usernameError;
    @FXML
    private Label passwordError;
    @FXML
    private Button login;
    
    @FXML
    public void initialize() {
        addListeners();
    }
    
    private void addListeners() {
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            usernameError.setText(null);
        });
        
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordError.setText(null);
        });
    }
    
    public void login() throws IOException {
        String error;
        if ((error = controller.checkUsernameErrors(username.getText())) != null) {
            usernameError.setText(error);
            return;
        }
        if ((error = controller.checkPasswordErrors(username.getText(), password.getText())) != null) {
            passwordError.setText(error);
            return;
        }
        controller.login(username.getText());
        MainMenuController.enterMainMenu();
    }
    
    public void back() throws IOException {
        Alert alert = Main.getConfirmationAlert("Back to register menu?", "Are you sure you want to back to register menu?");
        if (alert.showAndWait().get().getButtonData() == ButtonBar.ButtonData.NO) {
            alert.close();
            return;
        }
        Main.goToMenu("registerMenu");
    }
    
}
