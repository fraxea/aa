package org.example.view.images;

import java.net.URL;
import java.util.Random;

import org.example.view.Main;

import javafx.scene.image.Image;

public enum Avatar {
    BEA ("Bea"),
    BELLE ("Belle"),
    BIBI ("Bibi"),
    BONNIE ("Bonnie"),
    CARL ("Carl"),
    COLLETE ("Colette"),
    FANG ("Fang"),
    JESSIE ("Jessie"),
    POCO ("Poco"),
    SURGE ("Surge");

    private final String name;
    private final Image image;

    private Avatar(String name) {
        this.name = name;
        URL url = Main.class.getResource("/images/avatars/" + name + ".png");
        image = new Image(url.toExternalForm());
    }

    public static Avatar getRandomAvatar() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    public static Avatar getAvatarByName(String name) {
        for (Avatar avatar : values())
            if (avatar.name.equals(name)) return avatar;
        return null;
    }

    public static Avatar getAvatarByIndex(int index) {
        if (index < values().length) return values()[index];
        return null;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

}
