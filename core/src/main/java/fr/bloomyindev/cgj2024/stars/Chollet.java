package fr.bloomyindev.cgj2024.stars;

import com.badlogic.gdx.graphics.Color;

import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;

public class Chollet extends Star {
    private final Color color;

    public Chollet(AbsoluteCoords3D coords, int absoluteRadius) {
        super(coords, absoluteRadius, false, false, false, true);
        this.color = Color.PINK;
    }

    public Color getColor() {
        return color;
    }

}
