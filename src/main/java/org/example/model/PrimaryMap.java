package org.example.model;

import java.net.URL;

import org.example.view.Main;

import javafx.scene.image.Image;

public enum PrimaryMap {
    EASY ("Easy", 5),
    MEDIUM ("Medium", 6),
    HARD ("Hard", 7);

    private final String name;
    private final int defaultBalls;
    private final Image image;

    private PrimaryMap(String name, int defaultBalls) {
        this.name = name;
        this.defaultBalls = defaultBalls;
        URL url = Main.class.getResource("/images/icon.png");
        // URL url = Main.class.getResource("/images/maps/" + name + ".png");
        image = new Image(url.toExternalForm());
    }

    public String getName() {
        return name;
    }

    public int getDefaultBalls() {
        return defaultBalls;
    }

    public Image getImage() {
        return image;
    }

    public static PrimaryMap getPrimaryMapByName(String name) {
        for (PrimaryMap primaryMap : values())
            if (primaryMap.name.equals(name)) return primaryMap;
        return null;
    }

}
