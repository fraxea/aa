package org.example.view.music;

import java.net.URL;

import org.example.view.Main;

import javafx.scene.media.AudioClip;

public enum Soundtrack {
    GOAL ("Goal"),
    NOW_LOADING ("NowLoading"),
    OPPONENT_GOAL ("OpponentGoal"),
    FREEZE ("freeze"),
    SHOOT ("shoot"),
    BUTTON_CLICK ("ButtonClick");

    private final AudioClip audioClip;

    private Soundtrack(String musicFileName) {
        URL url = Main.class.getResource("/media/soundTracks/" + musicFileName + ".mp3");
        audioClip = new AudioClip(url.toExternalForm());
    }

    public AudioClip getAudioClip() {
        return audioClip;
    }

}
