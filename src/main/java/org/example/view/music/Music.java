package org.example.view.music;

import java.net.URL;

import org.example.view.Main;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public enum Music {
    LOGIN_MENU ("mainMenu/LoginMenu"),
    MAIN_MENU ("mainMenu/MainMenu"),
    TRACK1 ("gameMenu/track1"),
    TRACK2 ("gameMenu/track2"),
    TRACK3 ("gameMenu/track3"),
    DRAW ("endGame/Draw"),
    LOSER ("endGame/Loser"),
    WINNER ("endGame/Winner");

    private static final Music[] gameTracks = {TRACK1, TRACK2, TRACK3};

    private final MediaPlayer mediaPlayer;

    private Music(String musicFileName) {
        URL url = Main.class.getResource("/media/" + musicFileName + ".mp3");
        mediaPlayer = new MediaPlayer(new Media(url.toString()));
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
    }

    public static Music[] getGameTracks() {
        return gameTracks;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}
