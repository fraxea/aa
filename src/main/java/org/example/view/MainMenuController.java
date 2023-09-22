package org.example.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.example.controller.GameController;
import org.example.controller.MainController;
import org.example.controller.ProfileController;
import org.example.view.music.Music;

public class MainMenuController {
    private final MainController controller = MainController.getInstance();
    @FXML
    private BorderPane root;
    @FXML
    private Label username;
    @FXML
    private ImageView photo;
    
    public static void enterMainMenu() throws IOException {
        MainController.getInstance().updateScoreboard();
        Music.LOGIN_MENU.getMediaPlayer().stop();
        Music.MAIN_MENU.getMediaPlayer().play();
        Music.MAIN_MENU.getMediaPlayer().setMute(MainController.getInstance().isMute());
        Main.goToMenu("mainMenu");
    }
    
    public static void blackAndWhite(Node node) {
        if (!MainController.getInstance().isBlackAndWhite()) node.setEffect(null);
        else {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-1);
            node.setEffect(colorAdjust);
        }
    }
    
    public static void restart() throws IOException {
        GameController.killInstance();
        Main.goToMenu("gameMenu");
        Main.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event ->
                GameController.getInstance().keyPressed(event.getCode()));
    }

    @FXML
    public void initialize() {
        MainMenuController.blackAndWhite(root);
        addListeners();
        photo.setImage(ProfileController.getInstance().getUserImage());
        username.setText(ProfileController.getInstance().getUsername());
    }
    
    private void addListeners() {
    
    }
    
    
    public void goProfileMenu() throws IOException {
        Main.goToMenu("profileMenu");
    }
    
    public void startGame() throws IOException {
        Music.MAIN_MENU.getMediaPlayer().stop();
        GameController.killInstance();
        MainMenuController.restart();
    }
    
    public void showScoreboard() throws IOException {
        Main.goToMenu("scoreBoard");
    }
    
    public void settingMenu() throws IOException {
        Main.goToMenu("settingMenu");
    }
    
}