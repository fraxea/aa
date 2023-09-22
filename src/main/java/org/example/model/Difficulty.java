package org.example.model;

public enum Difficulty {
    EASY (1),
    MEDIUM (2),
    HARD (3);

    private final int degree;
    private final double rotationSpeed;
    private final double windSpeed;
    private final int freezeTime;


    private Difficulty(int degree) {
        this.degree = degree;
        this.rotationSpeed = 1 + 0.2 * degree;
        this.windSpeed = 0.9 + 0.3 * degree;
        this.freezeTime = 9 - 2 * degree;
    }

    public int getDegree() {
        return degree;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getFreezeTime() {
        return freezeTime;
    }

    public static Difficulty getDifficultyByDegree(int degree) {
        for (Difficulty difficulty : values())
            if (difficulty.degree == degree) return difficulty;
        return null;
    }

}
