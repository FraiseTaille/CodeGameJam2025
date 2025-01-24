package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;

public class Star {
    private Sound sound;
    private Color color;
    private AbsoluteCoords3D coords;

    public Sound getSound() {
        return sound;
    }

    public Color getColor() {
        return color;
    }

    public AbsoluteCoords3D getCoordinates() {
        return coords;
    }

    public void render(ShapeRenderer shapeRenderer, int x, int y, int distance) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(x, y, 10);
    }
}
