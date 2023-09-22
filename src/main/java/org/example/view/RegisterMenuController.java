package org.example.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.example.controller.ProfileController;
import org.example.controller.RegisterController;
import org.example.view.images.Avatar;
import org.example.view.music.Music;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class RegisterMenuController {
    private final RegisterController controller = RegisterController.getInstance();
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label usernameError;
    @FXML
    private Label passwordError;
    @FXML
    private Button register;
    @FXML
    private ImageView imagePreview;
    @FXML
    private GridPane avatarList;
    @FXML
    private Label fileError;

    public static void returnRegisterMenu() throws IOException {
        if (RegisterController.getInstance().isGuest()) {
            Alert alert = Main.getConfirmationAlert("Sure to quit game?!", "You are still guest. If you quit, you lost your account forever!!!");
            if (alert.showAndWait().get().getButtonData() == ButtonData.NO) {
                alert.close();
                return;
            }
            RegisterController.getInstance().deleteAccount();
        }
        Music.MAIN_MENU.getMediaPlayer().stop();
        Music.LOGIN_MENU.getMediaPlayer().play();
        Main.goToMenu("registerMenu");
    }
    
    @FXML
    public void initialize() {
        addListeners();
        addAvatars();
    }
    
    private void addAvatars() {
        int index = 0;
        Avatar avatar;
        while ((avatar = Avatar.getAvatarByIndex(index)) != null) {
            ImageView imageView = new ImageView(avatar.getImage());
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            imageView.setId(avatar.getName());
            imageView.setOnMouseClicked(event -> {
                String id = imageView.getId();
                imagePreview.setImage(Avatar.getAvatarByName(id).getImage());
                imagePreview.setId(id);
            });
            avatarList.add(imageView, index % ProfileMenuController.MAX_IN_ROW, index / ProfileMenuController.MAX_IN_ROW);
            index++;
        }
    }

    private void addListeners() {
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            usernameError.setText(controller.checkUsernameErrors(newValue));
            changeErrorLabels(usernameError);
            registerButtonActivation();
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordError.setText(controller.checkPasswordErrors(newValue));
            changeErrorLabels(passwordError);
            registerButtonActivation();
        });
    }

    private void changeErrorLabels(Label label) {
        label.getStyleClass().clear();
        if (label.getText().matches(".*is OK!"))
            label.getStyleClass().add("ok-label");
        else label.getStyleClass().add("have-error-label");
    }

    private void registerButtonActivation() {
        register.setDisable(!usernameError.getText().matches(".*is OK!")
                || (!passwordError.getText().matches(".*is OK!")));
    }
    
    public void register() throws IOException {
        controller.register(username.getText(), password.getText(), imagePreview.getId());
        MainMenuController.enterMainMenu();
    }

    public void guest() throws IOException {
        Alert alert = Main.getConfirmationAlert("Enter as Guest?","Are you sure you want to enter as guest?");
        if (alert.showAndWait().get().getButtonData() == ButtonData.NO) {
            alert.close();
            return;
        }
        controller.guest();
        MainMenuController.enterMainMenu();
    }
    
    public void exit() {
        Alert alert = Main.getConfirmationAlert("Exit aa?","Are you sure you want to exit aa?");
        if (alert.showAndWait().get().getButtonData() == ButtonData.NO) {
            alert.close();
            return;
        }
        controller.exit();
        Platform.exit();
    }

    public void goLoginPage() throws IOException {
        Main.goToMenu("loginMenu");
    }
    
    public void randomAvatar() {
        Avatar avatar = Avatar.getRandomAvatar();
        imagePreview.setImage(avatar.getImage());
        imagePreview.setId(avatar.getName());
    }
    
    public void chooseFile() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(Main.getStage());
        if (!ProfileController.getInstance().isFilePicture(selectedFile)) fileError.setText("Format not supported!");
        else {
            String url = ProfileController.getInstance().addFile(selectedFile);
            imagePreview.setImage(new Image(url));
            imagePreview.setId(url);
        }
    }

}
