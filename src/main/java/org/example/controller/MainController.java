package org.example.controller;

import java.util.ArrayList;
import java.util.Collections;

import org.example.controller.responses.SettingsResponses;
import org.example.model.PrimaryMap;
import org.example.model.User;
import org.example.model.UserDatabase;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainController implements SettingsResponses {
    private static MainController controller;
    private final UserDatabase userDatabase;
    
    private MainController() {
        userDatabase = UserDatabase.getInstance();
    }
    
    public static MainController getInstance() {
        return controller == null ? controller = new MainController() : controller;
    }



    // Settings

    public void setDifficultyDegree(int degree) {
        userDatabase.getLoggedInUser().getGameSettings().setDifficulty(degree);
    }

    public int getDifficultyDegree() {
        return userDatabase.getLoggedInUser().getGameSettings().getDifficulty().getDegree();
    }

    public int getNumberOfBalls() {
        return userDatabase.getLoggedInUser().getGameSettings().getBalls();
    }
    
    public void setNumberOfBalls(int balls) {
        userDatabase.getLoggedInUser().getGameSettings().setBalls(balls);
    }

    private boolean isNumberOfBallsValid(int balls) {
        return 8 <= balls && balls <= 32;
    }

    public void changeMute() {
        userDatabase.getLoggedInUser().getGameSettings().setMute();
    }

    public void changeColor() {
        userDatabase.getLoggedInUser().getGameSettings().setBlackAndWhite();
    }

    public boolean isMute() {
        return userDatabase.getLoggedInUser().getGameSettings().isMute();
        
    }

    public boolean isBlackAndWhite() {
        return userDatabase.getLoggedInUser().getGameSettings().isBlackAndWhite();
    }

    public void setKeyCode(Label left, Label right, Label shoot, Label freeze) {
        userDatabase.getLoggedInUser().getGameSettings().getKeyController().setKeys(
            KeyCode.getKeyCode(left.getText()), KeyCode.getKeyCode(right.getText()),
            KeyCode.getKeyCode(shoot.getText()), KeyCode.getKeyCode(freeze.getText())
        );
    }


    // Scoreboard

    public void updateScoreboard() {
        Collections.sort(userDatabase.getUsers(), (user1, user2) -> {
            int result = user2.getScore().getHighscore() - user1.getScore().getHighscore();
            if (result != 0) return result;
            return user1.getScore().getTime() - user2.getScore().getTime();
        });
    }

    public void showTopTen(VBox topTen) {
        updateScoreboard();
        int index = 1;
        fillHBox(topTen, 0, "Rank", null, "Username", "Time (sec)", "HighScore");
        for (User user : userDatabase.getUsers()) {
            fillHBox(topTen, index, String.valueOf(index), user.getImage(), user.getUsername(),
                    showTimeIfNeeded(user), String.valueOf(user.getScore().getHighscore()));
            colorLoggedInUser(topTen, index, user);
            if (++index > 10) break;
        }
    }

    public void showTopTenDegree(VBox topTen) {
        int index = 1;
        fillHBox(topTen, 0, "Rank", null, "Username", "HighScore", "Difficulty");
        for (User user : getTopTenDegree()) {
            fillHBox(topTen, index, String.valueOf(index), user.getImage(), user.getUsername(),
                    String.valueOf(user.getScore().getHighscore()), String.valueOf(user.getScore().getDifficulty().getDegree()));
            colorLoggedInUser(topTen, index, user);
            if (++index > 10) break;
        }
    }

    private ArrayList<User> getTopTenDegree() {
        final ArrayList<User> result = new ArrayList<>(userDatabase.getUsers());
        Collections.sort(result, (user1, user2) -> {
            int result1 = user2.getScore().getDifficulty().getDegree() - user1.getScore().getDifficulty().getDegree();
            if (result1 != 0) return result1;
            result1 = user2.getScore().getHighscore() - user1.getScore().getHighscore();
            if (result1 != 0) return result1;
            return user1.getScore().getTime() - user2.getScore().getTime();
        });
        return result;
    }

    private String showTimeIfNeeded(User user) {
        return userDatabase.haveEqualScore(user) ? String.valueOf(user.getScore().getTime()) : null;
    }

    private void colorLoggedInUser(VBox topTen, int index, User user) {
        HBox hBox = ((HBox)topTen.getChildren().get(index));
        int result = 160;
        if (user.equals(userDatabase.getLoggedInUser())) result += 40;
        hBox.setStyle("-fx-background-color: rgb(160, 160, " + result + ");");
    }

    private void fillHBox(VBox topTen, int index, String rank, Image image, String username, String time, String highScore) {
        HBox hBox = (HBox)topTen.getChildren().get(index);
        ObservableList<Node> children = hBox.getChildren();
        ((Label) children.get(0)).setText(rank);
        ((ImageView) children.get(1)).setImage(image);
        ((Label) children.get(2)).setText(username);
        ((Label) children.get(3)).setText(time);
        ((Label) children.get(4)).setText(highScore);
        if (index == 0) hBox.setStyle("-fx-background-color: gray;");
    }

    public void initVbox(VBox topTen) {
        topTen.setStyle("-fx-background: rgb(200, 200, 200);");
        topTen.getChildren().clear();
        for (int index = 0; index < 11; index++)
            topTen.getChildren().add(getHBoxHighScore(index));
    }

    private Label getRankLabel(int index) {
        Label rank = new Label();
        rank.setPrefHeight(50);
        rank.setPrefWidth(50);
        rank.setAlignment(Pos.CENTER);
        if (index == 1) rank.setStyle("-fx-background-color: gold;");
        else if (index == 2) rank.setStyle("-fx-background-color: silver;");
        else if (index == 3) rank.setStyle("-fx-background-color:rgb(250, 127, 50);");
        else rank.setStyle("-fx-background-color: gray;");
        return rank;
    }

    private ImageView getImageUser() {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        return imageView;
    }

    private Label getUsernameLabel() {
        Label username = new Label();
        username.setPrefWidth(140);
        return username;
    }

    private Label getTimerLabel() {
        Label timer = new Label();
        timer.setPrefWidth(100);
        timer.setAlignment(Pos.CENTER);
        return timer;
    }

    private Label getHighScoreLabel() {
        Label highScore = new Label();
        highScore.setPrefWidth(100);
        highScore.setAlignment(Pos.CENTER);
        return highScore;
    }

    private HBox getHBoxHighScore(int index) {
        HBox hbox = new HBox(getRankLabel(index), getImageUser(), getUsernameLabel(), getTimerLabel(), getHighScoreLabel());
        hbox.setPadding(new Insets(5, 10, 5, 10));
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: rgb(160, 160, 160);");
        return hbox;
    }
    
    
    public String checkBallsErrors(String balls) {
        if (balls.matches("\\d+") && isNumberOfBallsValid(Integer.parseInt(balls)))
            return null;
        return INVALID_BALLS;
    }
    
    public void getKeyCodes(Label left, Label right, Label shoot, Label freeze) {
        left.setText(userDatabase.getLoggedInUser().getGameSettings().getKeyController().getLeft().getName());
        right.setText(userDatabase.getLoggedInUser().getGameSettings().getKeyController().getRight().getName());
        shoot.setText(userDatabase.getLoggedInUser().getGameSettings().getKeyController().getShoot().getName());
        freeze.setText(userDatabase.getLoggedInUser().getGameSettings().getKeyController().getFreeze().getName());
    }
    
    public PrimaryMap getPrimaryMap() {
        return userDatabase.getLoggedInUser().getGameSettings().getPrimaryMap();
    }
    
    public void setPrimaryMap(String name) {
        userDatabase.getLoggedInUser().getGameSettings().setPrimaryMap(name);
    }
    
}
