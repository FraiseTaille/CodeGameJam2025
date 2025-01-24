package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private AudioDevice audio;

    public SoundManager(AudioDevice audio) {
        this.audio = audio;
    }

    public void playSound(int distance, Star star) {
        // TODO Calculate audio volume with the distance
        star.getSound().play(1.0f);
    }
}
