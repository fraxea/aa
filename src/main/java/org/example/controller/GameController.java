package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import org.example.model.KeyController;
import org.example.model.ShootBall;
import org.example.model.UserDatabase;
import org.example.view.Main;
import org.example.view.music.Music;
import org.example.view.music.Soundtrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameController implements GameConstants {
    private static GameController controller;
    private final UserDatabase userDatabase;
    private final ArrayList<Timeline> timelines;
    private final Timeline timeline;
    private final Timeline timer;
    private final Rotate rotate;
    private final Circle circle;
    private final Circle[] defaultBalls;
    private final Line[] defaultLines;
    private final Circle[] balls;
    private final ArrayList<Circle> connectedBalls;
    private final ArrayList<Line> lines;
    private Music currentMusic;
    private int currentBallNumber;
    private double wind;
    private Pane pane;
    private Label ballsLabel;
    private Label scoreLabel;
    private Label time;
    private Label windLabel;
    private ProgressBar freeze;
    private int seconds;
    private int phase;
    
    
    
    private GameController() {
        userDatabase = UserDatabase.getInstance();
        int numberOfBalls = userDatabase.getLoggedInUser().getGameSettings().getPrimaryMap().getDefaultBalls();
        timeline = new Timeline();
        timeline.setCycleCount(-1);
        timer = new Timeline();
        rotate = new Rotate(userDatabase.getLoggedInUser().getGameSettings().getDifficulty().getRotationSpeed(),
                CENTER_X, CENTER_Y);
        circle = new Circle(CENTER_X, CENTER_Y, CIRCLE_RADIUS);
        defaultBalls = new Circle[numberOfBalls];
        defaultLines = new Line[numberOfBalls];
        connectedBalls = new ArrayList<>();
        timelines = new ArrayList<>();
        timelines.add(timer);
        timelines.add(timeline);
        lines = new ArrayList<>();
        initDefaultBalls();
        balls = new Circle[userDatabase.getLoggedInUser().getGameSettings().getBalls()];
        currentBallNumber = 0;
        initBalls();
        currentMusic = Music.getGameTracks()[0];
        wind = seconds = 0;
        phase = 1;
    }
    
    public static GameController getInstance() {
        return controller == null ? controller = new GameController() : controller;
    }
    
    public static void killInstance() {
        controller = null;
    }
    
    public Circle getCircle() {
        return circle;
    }
    
    public double getWind() {
        return wind;
    }
    
    public void setWindLabel(Label windLabel) {
        this.windLabel = windLabel;
    }
    
    public void setBallsLabel(Label ballsLabel) {
        this.ballsLabel = ballsLabel;
        setBallsLabel();
    }
    
    public void setTimeLabel(Label time) {
        this.time = time;
        setTimeLabel();
    }
    
    public void setScoreLabel(Label scoreLabel) {
        this.scoreLabel = scoreLabel;
        setScoreLabel();
    }
    
    public void setFreeze(ProgressBar freeze) {
        this.freeze = freeze;
    }
    
    public void endGame(String name) throws IOException {
        GameController.controller = null;
        Main.goToMenu(name);
        Music.MAIN_MENU.getMediaPlayer().play();
        Music.MAIN_MENU.getMediaPlayer().setMute(MainController.getInstance().isMute());
    }
    
    private void initBalls() {
        for (int i = 0; i < balls.length; i++) {
            double yCoordinate = CENTER_Y + LINE_LENGTH + DISTANCE * (i + 1);
            balls[i] = new Circle(CENTER_X, yCoordinate, BALLS_RADIUS, Color.WHITE);
        }
    }
    
    private void initDefaultBalls() {
        for (int i = 0; i < defaultBalls.length; i++) {
            double xCoordinate = CENTER_X + LINE_LENGTH * Math.cos(i * 2 * Math.PI / defaultBalls.length);
            double yCoordinate = CENTER_Y + LINE_LENGTH * Math.sin(i * 2 * Math.PI / defaultBalls.length);
            defaultBalls[i] = new Circle(xCoordinate, yCoordinate, BALLS_RADIUS);
            defaultLines[i] = new Line(CENTER_X, CENTER_Y, xCoordinate, yCoordinate);
        }
        connectedBalls.addAll(List.of(defaultBalls));
        lines.addAll(List.of(defaultLines));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(DURATION_MILLIS), rotateDefaultBalls));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(DURATION_MILLIS), checkCollisions));
        timeline.play();
    }
    
    EventHandler<ActionEvent> checkCollisions = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (int first = 0; first < connectedBalls.size(); first++)
                for (int second = first + 1; second < connectedBalls.size(); second++)
                    if (getDistance(first, second) < minDistance()) {
                        defeat();
                        return;
                    }
            if (lines.size() == balls.length + defaultBalls.length) victory();
        }
    };
    
    private void activateFreeze() {
        Timeline freezeTimer = new Timeline();
        rotate.setAngle(rotate.getAngle() / 3);
        Soundtrack.FREEZE.getAudioClip().play();
        freezeTimer.getKeyFrames().add(new KeyFrame(Duration.millis(DURATION_MILLIS), event -> {
            freeze.setProgress(freeze.getProgress() - 0.01 /
                    userDatabase.getLoggedInUser().getGameSettings().getDifficulty().getFreezeTime());
            if (freeze.getProgress() <= 0.0) {
                rotate.setAngle(rotate.getAngle() * 3);
                freezeTimer.stop();
            }
        }));
        freezeTimer.setCycleCount(-1);
        freezeTimer.play();
    }
    
    private void activatePhase2() {
        if (phase == 1) phase = 2;
        else return;
        Timeline timeline = new Timeline();
        timelines.add(timeline);
        Random random = new Random();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(4), event -> {
            if (random.nextInt(2) == 0)
                rotate.setAngle(-1 * rotate.getAngle());
        }));
        timeline.setCycleCount(-1);
        timeline.play();
        Timeline timeline2 = new Timeline();
        timelines.add(timeline2);
        timeline2.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(DURATION_MILLIS), event2 -> {
                for (Circle ball : connectedBalls)
                    ball.setRadius(BALLS_RADIUS + BALLS_RADIUS * 0.05 / 100);
            }));
            timeline1.setOnFinished(event1 -> {
                for (Circle ball : connectedBalls)
                    ball.setRadius(BALLS_RADIUS);
            });
            timeline1.setCycleCount(-1);
            timeline1.play();
        }));
        timeline2.setCycleCount(-1);
        timeline2.play();
    }
    
    private void activatePhase3() {
        if (phase == 2) phase = 3;
        else return;
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            for (Circle ball : connectedBalls)
                ball.setVisible(!ball.isVisible());
            for (Line line : lines)
                line.setVisible(!line.isVisible());
        }));
        timelines.add(timeline);
        timeline.setCycleCount(-1);
        timeline.play();
    }
    
    private void activatePhase4() {
        if (phase == 3) phase = 4;
        else return;
        Random random = new Random();
        double boundWind = userDatabase.getLoggedInUser().getGameSettings().getDifficulty().getWindSpeed();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            wind = random.nextDouble(-boundWind, boundWind + 1);
            windLabel.setText("Wind: " + wind);
        }));
        timelines.add(timeline);
        timeline.setCycleCount(-1);
        timeline.play();
        Main.getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(userDatabase.getLoggedInUser().getGameSettings().getKeyController().getLeft()))
                move(-1);
            else if (event.getCode().equals(userDatabase.getLoggedInUser().getGameSettings().getKeyController().getRight()))
                move(1);
        });
    }
    
    private void move(int direction) {
        for (int index = currentBallNumber; index < balls.length; index++) {
            double xCoordinateGun = balls[index].getCenterX() + SPEED * direction;
            if (xCoordinateGun > CENTER_X * 2 - DISTANCE || xCoordinateGun < DISTANCE) return;
            balls[index].setCenterX(xCoordinateGun);
        }
    }
    
    private double minDistance() {
        return connectedBalls.get(0).getRadius() * 2;
    }
    
    private double getDistance(int first, int second) {
        return getDistance(connectedBalls.get(first), connectedBalls.get(second));
    }
    
    public double getDistance(Circle circle1, Circle circle2) {
        return Math.sqrt(
                Math.pow(circle1.getCenterX() - circle2.getCenterX(), 2) +
                Math.pow(circle1.getCenterY() - circle2.getCenterY(), 2)
        );
    }
    
    EventHandler<ActionEvent> rotateDefaultBalls = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            for (int i = 0; i < defaultBalls.length; i++) {
                Circle ball = defaultBalls[i];
                Point2D point2D = rotate.transform(new Point2D(ball.getCenterX(), ball.getCenterY()));
                ball.setCenterX(point2D.getX());
                ball.setCenterY(point2D.getY());
                defaultLines[i].setEndX(point2D.getX());
                defaultLines[i].setEndY(point2D.getY());
            }
        }
    };
    
    public void addBalls(Pane pane) {
        this.pane = pane;
        pane.getChildren().addAll(defaultBalls);
        pane.getChildren().addAll(defaultLines);
        pane.getChildren().addAll(balls);
        pane.getChildren().add(circle);
    }
    
    public void pauseMusic() {
        if (currentMusic != null)
            currentMusic.getMediaPlayer().pause();
    }

    public void switchMusic(int index) {
        if (index < 0) {
            if (currentMusic != null)
                currentMusic.getMediaPlayer().stop();
            currentMusic = null;
            return;
        }
        if (currentMusic == null)
            currentMusic = Music.getGameTracks()[index];
        else if (currentMusic != Music.getGameTracks()[index]) {
            currentMusic.getMediaPlayer().stop();
            currentMusic = Music.getGameTracks()[index];
        }
    }

    public void playMusic() {
        if (currentMusic != null)
            currentMusic.getMediaPlayer().play();
    }
    
    public int getCurrentMusic() {
        return Arrays.asList(Music.getGameTracks()).indexOf(currentMusic);
    }
    
    public void stopMusic() {
        if (currentMusic != null)
            currentMusic.getMediaPlayer().stop();
    }

    private void shootBall() {
        ShootBall shootBall = new ShootBall(balls[currentBallNumber]);
        shootBall.play();
        Soundtrack.SHOOT.getAudioClip().play();
        currentBallNumber++;
        double ratio = (double)currentBallNumber / balls.length;
        if (ratio >= 0.75)
            activatePhase4();
        else if (ratio >= 0.5)
            activatePhase3();
        else if (ratio >= 0.25)
            activatePhase2();
        freeze.setProgress(freeze.getProgress() + FREEZE_TURN);
        for (int index = currentBallNumber; index < balls.length; index++)
            balls[index].setCenterY(balls[index].getCenterY() - DISTANCE);
    }
    
    public void successfulShoot(Circle ball) {
        Line line = new Line(CENTER_X, CENTER_Y, ball.getCenterX(), ball.getCenterY());
        pane.getChildren().add(line);
        connectedBalls.add(ball);
        lines.add(line);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(DURATION_MILLIS), event -> {
            Point2D point2D = rotate.transform(new Point2D(ball.getCenterX(), ball.getCenterY()));
            ball.setCenterX(point2D.getX());
            ball.setCenterY(point2D.getY());
            line.setEndX(point2D.getX());
            line.setEndY(point2D.getY());
        }));
    }
    
    private void fallBall(Circle circle, Line line) {
        double dx = circle.getCenterX() - CENTER_X;
        double endX = circle.getCenterX() - dx / END_GAME_FRAME;
        circle.setCenterX(endX);
        line.setEndX(endX);
        circle.setCenterY(circle.getCenterY() + FALLING_SPEED);
        line.setEndY(line.getEndY() + FALLING_SPEED);
    }
    
    private void flyBall(Circle circle, Line line) {
        double dx = circle.getCenterX() - CENTER_X;
        double endX = circle.getCenterX() + dx / END_GAME_FRAME * FALLING_SPEED;
        circle.setCenterX(endX);
        line.setEndX(endX);
        double dy = circle.getCenterY() - CENTER_Y;
        double endY = circle.getCenterY() + dy / END_GAME_FRAME * FALLING_SPEED;
        circle.setCenterY(endY);
        line.setEndY(endY);
    }
    
    private void stopTheTime() {
        for (Timeline timeline : timelines) {
            timeline.stop();
            timeline.getKeyFrames().clear();
        }
    }
    
    public void defeat() {
        stopMusic();
        Soundtrack.OPPONENT_GOAL.getAudioClip().play();
        stopTheTime();
        Main.getScene().getRoot().getStylesheets().clear();
        Main.getScene().getRoot().setStyle("-fx-background-color: red;");
        Timeline defeat = new Timeline(new KeyFrame(Duration.millis(DURATION_MILLIS), event -> {
            for (int index = 0; index < lines.size(); index++)
                fallBall(connectedBalls.get(index), lines.get(index));
        }));
        defeat.setCycleCount(END_GAME_FRAME);
        defeat.setOnFinished(event -> {
            Music.LOSER.getMediaPlayer().play();
            Alert alert = Main.getConfirmationAlert("Defeat", showScore());
            alert.show();
            alert.setOnHidden(event2 -> {
                Music.LOSER.getMediaPlayer().stop();
                try {
                    endGame("scoreBoard");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        defeat.setDelay(Duration.millis(DELAY));
        defeat.play();
    }
    
    private void victory() {
        stopMusic();
        Soundtrack.GOAL.getAudioClip().play();
        stopTheTime();
        Main.getScene().getRoot().getStylesheets().clear();
        Main.getScene().getRoot().setStyle("-fx-background-color: green;");
        Timeline defeat = new Timeline(new KeyFrame(Duration.millis(DURATION_MILLIS), event -> {
            for (int index = 0; index < lines.size(); index++)
                flyBall(connectedBalls.get(index), lines.get(index));
        }));
        defeat.setCycleCount(END_GAME_FRAME);
        defeat.setOnFinished(event -> {
            Music.WINNER.getMediaPlayer().play();
            Alert alert = Main.getConfirmationAlert("Victory", showScore());
            alert.show();
            alert.setOnHidden(event2 -> {
                Music.WINNER.getMediaPlayer().stop();
                try {
                    endGame("ScoreBoard");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        defeat.setDelay(Duration.millis(DELAY));
        defeat.play();
    }
    
    private int getScore() {
        int score = currentBallNumber == balls.length ? 100 : 0;
        score += currentBallNumber * 2 + userDatabase.getLoggedInUser().getGameSettings().getScore();
        return score;
    }
    
    public String showScore() {
        int score = getScore();
        String highScore = "Highest Score: ";
        if (userDatabase.getLoggedInUser().getScore().getHighscore() < score) {
            highScore += "New High Score!!!";
            userDatabase.getLoggedInUser().getScore().setHighscore(score);
            userDatabase.getLoggedInUser().getScore().setDifficulty(userDatabase
                    .getLoggedInUser().getGameSettings().getDifficulty());
            userDatabase.getLoggedInUser().getScore().setTime(seconds);
        } else {
            highScore += userDatabase.getLoggedInUser().getScore().getHighscore();
        }
        String spendTime = "Time spent: " + String.format("%02d:%02d", seconds / 60, seconds % 60);
        return "Score: " + score + "\n" + highScore + "\n" + spendTime;
    }
    
    
    public String getGuide() {
        KeyController keyController = userDatabase.getLoggedInUser().getGameSettings().getKeyController();
        return    "Left: " + keyController.getLeft().getName()
                + "\nRight: " + keyController.getRight().getName()
                + "\nShoot: " + keyController.getShoot().getName()
                + "\nFreeze: " + keyController.getFreeze().getName();
    }
    
    
    public void keyPressed(KeyCode code) {
        KeyController keyController = userDatabase.getLoggedInUser().getGameSettings().getKeyController();
        if (code.equals(keyController.getShoot())) {
            shootBall();
            setBallsLabel();
            setScoreLabel();
        } else if (code.equals(keyController.getFreeze())) {
            if (freeze.getProgress() >= 1.0) activateFreeze();
        }
    }
    
    private void setBallsLabel() {
        String result = currentBallNumber + " / " + balls.length;
        ballsLabel.setText(result);
        double ratio = (double)currentBallNumber / balls.length;
        if (ratio > 0.66)
            ballsLabel.setStyle("-fx-text-fill: green;");
        else if (ratio > 0.33)
            ballsLabel.setStyle("-fx-text-fill: yellow;");
        else
            ballsLabel.setStyle("-fx-text-fill: red;");
    }
    
    private void setScoreLabel() {
        scoreLabel.setText(String.valueOf(getScore()));
    }
    
    private void setTimeLabel() {
        timer.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            time.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            if (seconds == TIME_SECONDS) defeat();
            seconds++;
        }));
        timer.setCycleCount(-1);
        timer.play();
    }
    
}
