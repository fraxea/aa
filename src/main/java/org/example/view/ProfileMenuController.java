package org.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.example.controller.ProfileController;
import org.example.controller.RegisterController;
import org.example.view.images.Avatar;
import org.example.view.music.Music;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class ProfileMenuController {
    public static final int MAX_IN_ROW = 5;
    private final ProfileController controller = ProfileController.getInstance();
    @FXML
    private BorderPane root;
    @FXML
    private Label fileError;
    @FXML
    private Button apply;
    @FXML
    private ImageView imagePreview;
    @FXML
    private GridPane avatarList;
    @FXML
    private Label passwordError;
    @FXML
    private Label usernameError;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    
    @FXML
    public void initialize() {
        MainMenuController.blackAndWhite(root);
        addListeners();
        addAvatars();
        username.setText(controller.getUsername());
        imagePreview.setImage(controller.getUserImage());
    }
    
    private void addListeners() {
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            usernameError.setText(controller.checkUsernameErrors(newValue));
            changeErrorLabels(usernameError);
            applyButtonActivation();
        });
    
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordError.setText(controller.checkPasswordErrors(newValue));
            changeErrorLabels(passwordError);
            applyButtonActivation();
        });
    }
    
    private void applyButtonActivation() {
        apply.setDisable(!usernameError.getText().matches(".*is OK!")
                || (passwordError.getText() != null && !passwordError.getText().matches(".*is OK!")));
    }

    private void changeErrorLabels(Label label) {
        label.getStyleClass().clear();
        if (label.getText() == null) return;
        if (label.getText().matches(".*is OK!"))
            label.getStyleClass().add("ok-label");
        else label.getStyleClass().add("have-error-label");
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
                apply.setDisable(false);
            });
            avatarList.add(imageView, index % MAX_IN_ROW, index / MAX_IN_ROW);
            index++;
        }
    }
    
    public void randomAvatar() {
        imagePreview.setImage(Avatar.getRandomAvatar().getImage());
    }
    
    public void applyChanges() throws IOException {
        controller.updateUserData(username.getText(), password.getText(), imagePreview.getId());
        back();
    }
    
    public void back() throws IOException {
        Main.goToMenu("mainMenu");
    }
    
    public void chooseFile() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(Main.getStage());
        if (!controller.isFilePicture(selectedFile)) fileError.setText("Format not supported!");
        else {
            String url = controller.addFile(selectedFile);
            imagePreview.setImage(new Image(url));
            imagePreview.setId(url);
            apply.setDisable(false);
        }
    }
    
    public void logout() throws IOException {
        Alert alert = Main.getConfirmationAlert("Logout?", "Are you sure you want to back to Register Menu?");
        if (alert.showAndWait().get().getButtonData() == ButtonBar.ButtonData.NO) {
            alert.close();
            return;
        }
        RegisterMenuController.returnRegisterMenu();
    }
    
    
    public void deleteAccount() throws IOException {
        Alert alert = Main.getConfirmationAlert("Delete account?", "Are you sure you want to delete your account? You lost your data forever!");
        if (alert.showAndWait().get().getButtonData() == ButtonBar.ButtonData.NO) {
            alert.close();
            return;
        }
        RegisterController.getInstance().deleteAccount();
        Music.MAIN_MENU.getMediaPlayer().stop();
        Music.LOGIN_MENU.getMediaPlayer().play();
        Main.goToMenu("registerMenu");
    }
}
