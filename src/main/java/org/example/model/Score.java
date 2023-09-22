package org.example.model;

public class Score {
    private int highscore;
    private int time;
    private Difficulty difficulty;

    public Score() {
        highscore = 0;
        time = 180;
        difficulty = Difficulty.EASY;
    }

    public int getHighscore() {
        return highscore;
    }

    public int getTime() {
        return time;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

}
