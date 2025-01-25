package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;

public class Chollet extends Star {

    public Chollet(AbsoluteCoords3D coords, Color color, Sound sound, int absoluteRadius) {
        super(coords, color, sound, absoluteRadius);
    }

    public Color getColor() {
        return Color.PINK;
    }

    public boolean isCholletStar() {
        return true;
    }
    
}
