package org.example.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import org.example.controller.MainController;
import org.example.view.music.Music;

import java.io.IOException;

public class SettingMenuController {
    private final MainController controller = MainController.getInstance();
    @FXML
    private ChoiceBox<String> primaryMap;
    @FXML
    private Label freezeLabel;
    @FXML
    private Label shootLabel;
    @FXML
    private Label rightLabel;
    @FXML
    private Label leftLabel;
    @FXML
    private Button apply;
    @FXML
    private BorderPane root;
    @FXML
    private TextField left;
    @FXML
    private TextField right;
    @FXML
    private TextField shoot;
    @FXML
    private TextField freeze;
    @FXML
    private Label ballsError;
    @FXML
    private Button muteButton;
    @FXML
    private TextField balls;
    @FXML
    private ChoiceBox<String> difficulty;
    
    @FXML
    public void initialize() {
        initKeyCodes();
        initBalls();
        initDifficulty();
        initPrimaryMap();
        addListeners();
        setMuteText();
        MainMenuController.blackAndWhite(root);
    }
    
    private void initKeyCodes() {
        controller.getKeyCodes(leftLabel, rightLabel, shootLabel, freezeLabel);
    }
    
    private void initDifficulty() {
        difficulty.setValue(String.valueOf(controller.getDifficultyDegree()));
    }
    
    private void initPrimaryMap() {
        primaryMap.setValue(controller.getPrimaryMap().getName());
    }
    
    public void back() throws IOException {
        Main.goToMenu("mainMenu");
    }
    
    public void changeColor() {
        controller.changeColor();
        MainMenuController.blackAndWhite(root);
    }
    
    private void addListeners() {
        balls.textProperty().addListener((observable, oldValue, newValue) -> {
            ballsError.setText(controller.checkBallsErrors(newValue));
            applyButtonActivation();
        });
        left.setOnKeyPressed(ke -> leftLabel.setText(ke.getCode().getName()));
        right.setOnKeyPressed(ke -> rightLabel.setText(ke.getCode().getName()));
        shoot.setOnKeyPressed(ke -> shootLabel.setText(ke.getCode().getName()));
        freeze.setOnKeyPressed(ke -> freezeLabel.setText(ke.getCode().getName()));
    }

    private void applyButtonActivation() {
        apply.setDisable(ballsError.getText() != null);
    }
    
    private void initBalls() {
        int numberOfBalls = controller.getNumberOfBalls();
        balls.setText(String.valueOf(numberOfBalls));
    }
    
    private void setMuteText() {
        if (controller.isMute()) muteButton.setText("unmute");
        else muteButton.setText("mute");
    }
    
    public void mute() {
        MediaPlayer mediaPlayer = Music.MAIN_MENU.getMediaPlayer();
        mediaPlayer.setMute(!mediaPlayer.isMute());
        controller.changeMute();
        setMuteText();
    }

    public void apply() {
        controller.setNumberOfBalls(Integer.parseInt(balls.getText()));
        controller.setDifficultyDegree(Integer.parseInt(difficulty.getValue()));
        controller.setPrimaryMap(primaryMap.getValue());
        controller.setKeyCode(leftLabel, rightLabel, shootLabel, freezeLabel);
    }
}
