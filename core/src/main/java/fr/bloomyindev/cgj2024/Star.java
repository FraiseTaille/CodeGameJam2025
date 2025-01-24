package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class Star {
    private Sound sound;
    private Color color;
    private float x;
    private float y;

    public Sound getSound() {
        return sound;
    }

    public Color getColor() {
        return color;
    }

    public float[] getCoordinates() {
        return new float[]{x, y};
    }
}
