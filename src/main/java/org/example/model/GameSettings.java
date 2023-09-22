package org.example.model;

import java.util.Arrays;

public class GameSettings {
    private final KeyController keyController;
    private Difficulty difficulty;
    private PrimaryMap primaryMap;
    private int balls;
    private boolean mute;
    private boolean blackAndWhite;

    public GameSettings() {
        keyController = new KeyController();
        difficulty = Difficulty.MEDIUM;
        primaryMap = PrimaryMap.MEDIUM;
        balls = 12;
        mute = false;
        blackAndWhite = false;
    }

    public KeyController getKeyController() {
        return keyController;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public PrimaryMap getPrimaryMap() {
        return primaryMap;
    }

    public int getBalls() {
        return balls;
    }

    public boolean isMute() {
        return mute;
    }

    public boolean isBlackAndWhite() {
        return blackAndWhite;
    }

    public void setDifficulty(int degree) {
        this.difficulty = Difficulty.getDifficultyByDegree(degree);
    }

    public void setPrimaryMap(String name) {
        this.primaryMap = PrimaryMap.getPrimaryMapByName(name);
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public void setMute() {
        this.mute = !mute;
    }

    public void setBlackAndWhite() {
        this.blackAndWhite = !blackAndWhite;
    }
    
    
    public int getScore() {
        double result = Math.pow(primaryMap.getDefaultBalls(), 2);
        result += Math.pow(difficulty.getDegree() + 5, 2);
        return (int) result;
    }
}
