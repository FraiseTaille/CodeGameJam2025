package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private static float maxVolume = 1f;
    private static Music music = Gdx.audio.newMusic(Gdx.files.internal("eq9nomel-16.wav"));
    private static Music musicWMel = Gdx.audio.newMusic(Gdx.files.internal("eq9mel-16.wav"));
    private static Music musicTroll = Gdx.audio.newMusic(Gdx.files.internal("ACLong.mp3"));
    private AudioDevice audio;

    public SoundManager(AudioDevice audio) {
        this.audio = audio;
        music.setVolume(.5f);
        musicWMel.setVolume(0f);
        musicWMel.setLooping(true);
        music.setLooping(true);
        music.play();
        musicWMel.play();
    }

    public void playSound(long distance, Star star) {
        if (star.isVisitable()) {
            float vol = calculateVolumeWithDistance(distance);
            System.out.printf("%f (dist %d) playing\n", vol, distance);
            music.setVolume(1 - vol);
            musicWMel.setVolume(vol);
        } else if (star.isCholletStar() && distance < 100) {
            musicTroll.setVolume(0.9f);
            music.setVolume(0.1f);
            if (!musicTroll.isPlaying()) {
                musicTroll.play();
            }
        }

    }

    private float calculateVolumeWithDistance(long distance) {
        return maxVolume / (float) Math.pow(distance, 2);
    }
}
