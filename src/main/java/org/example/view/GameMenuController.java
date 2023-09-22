package org.example.view;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import org.example.controller.GameController;

import java.io.IOException;

public class GameMenuController {
    private final GameController controller = GameController.getInstance();
    @FXML
    private Label windLabel;
    @FXML
    private ProgressBar freeze;
    @FXML
    private Label time;
    @FXML
    private Label score;
    @FXML
    private Label ballsLabel;
    @FXML
    private Pane pane;
    
    @FXML
    public void initialize() {
        controller.addBalls(pane);
        controller.setBallsLabel(ballsLabel);
        controller.setScoreLabel(score);
        controller.setTimeLabel(time);
        controller.setFreeze(freeze);
        controller.setWindLabel(windLabel);
        controller.playMusic();
    }
    
    
    public void pause() throws IOException {
        controller.pauseMusic();
        Main.goToMenu("pauseMenu");
    }
}
