package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private AudioDevice audio;
    private static float maxVolume = 1f;
    public SoundManager(AudioDevice audio) {
        this.audio = audio;
    }

    public void playSound(int distance, Star star) {
        // TODO Calculate audio volume with the distance
        if (star.getSound() != null) {
            star.getSound().play(calculateVolumeWithDistance(distance));
        }
    }

    private float calculateVolumeWithDistance(int distance) {
        return maxVolume / (float) Math.pow(distance, 2);
    }
}
