package org.example.model;

import javafx.scene.input.KeyCode;

public class KeyController {
    private KeyCode right;
    private KeyCode left;
    private KeyCode shoot;
    private KeyCode freeze;

    public KeyController() {
        right = KeyCode.RIGHT;
        left = KeyCode.LEFT;
        shoot = KeyCode.SPACE;
        freeze = KeyCode.TAB;
    }

    public KeyCode getRight() {
        return right;
    }

    public KeyCode getLeft() {
        return left;
    }

    public KeyCode getShoot() {
        return shoot;
    }

    public KeyCode getFreeze() {
        return freeze;
    }

    public void setKeys(KeyCode left, KeyCode right, KeyCode shoot, KeyCode freeze) {
        if (left != null) this.left = left;
        if (right != null) this.right = right;
        if (shoot != null) this.shoot = shoot;
        if (freeze != null) this.left = freeze;
    }
    
}
