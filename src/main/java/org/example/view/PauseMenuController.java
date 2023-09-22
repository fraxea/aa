package org.example.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import org.example.controller.GameController;
import org.example.view.music.Music;

import java.io.IOException;

public class PauseMenuController {
    private Scene gameScene;
    private final GameController controller = GameController.getInstance();
    private final ToggleGroup music = new ToggleGroup();
    
    @FXML
    private RadioButton mute;
    
    @FXML
    private RadioButton track1;
    
    @FXML
    private RadioButton track2;
    
    @FXML
    private RadioButton track3;
    
    @FXML
    public void initialize() {
        gameScene = Main.getScene();
        addTracks();
        addListeners();
    }
    
    private void addListeners() {
        music.selectedToggleProperty().addListener((observable, oldValue, newValue)
                -> controller.switchMusic(music.getToggles().indexOf(newValue) - 1));
    }
    
    private void addTracks() {
        mute.setToggleGroup(music);
        track1.setToggleGroup(music);
        track2.setToggleGroup(music);
        track3.setToggleGroup(music);
        music.selectToggle(music.getToggles().get(controller.getCurrentMusic() + 1));
    }
    
    
    public void guide() {
        String result = controller.getGuide();
        Main.getConfirmationAlert("Keys", result).showAndWait();
    }
    
    public void resume() {
        Main.getStage().setScene(gameScene);
        controller.playMusic();
    }
    
    public void restart() throws IOException {
        MainMenuController.restart();
    }
    
    public void quit() throws IOException {
        GameController.killInstance();
        Main.goToMenu("mainMenu");
        controller.stopMusic();
        Music.MAIN_MENU.getMediaPlayer().play();
    }
    
}
