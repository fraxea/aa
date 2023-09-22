package org.example.model;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.example.controller.GameConstants;
import org.example.controller.GameController;

public class ShootBall extends Transition implements GameConstants {
    private final GameController controller;
    private final Circle ball;
    
    public ShootBall(Circle ball) {
        controller = GameController.getInstance();
        this.ball = ball;
        this.setCycleCount(Animation.INDEFINITE);
        this.setCycleDuration(Duration.millis(DURATION_MILLIS * 100));
    }
    
    private boolean isReached() {
        return controller.getDistance(ball, controller.getCircle()) <= LINE_LENGTH;
    }
    
    private boolean isOutOfBounds() {
        return  ball.getCenterY() <= CIRCLE_RADIUS ||
                ball.getCenterX() <= CIRCLE_RADIUS ||
                ball.getCenterX() >= CENTER_X * 2 - CIRCLE_RADIUS;
    }
    
    @Override
    protected void interpolate(double v) {
        ball.setCenterY(ball.getCenterY() - SPEED);
        ball.setCenterX(ball.getCenterX() - controller.getWind());
        if (isOutOfBounds()) {
            controller.defeat();
            this.stop();
        } else if (isReached()) {
            this.stop();
            controller.successfulShoot(ball);
        }
        
    }
}
