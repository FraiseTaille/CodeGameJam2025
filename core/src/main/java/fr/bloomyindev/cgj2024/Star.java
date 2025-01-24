package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.bloomyindev.cgj2024.CoordinateSystems.*;

public class Star {
    private Sound sound;
    private Color color;
    private AbsoluteCoords3D coords;

    private float angularSize;
    private int absoluteRadius;

    public Sound getSound() {
        return sound;
    }

    public Color getColor() {
        return color;
    }

    public AbsoluteCoords3D getCoordinates() {
        return coords;
    }

    public void render(ShapeRenderer shapeRenderer, int x, int y, int distance, float fovAngleX) {
        float renderSize;

        renderSize = angularSize * (16 / fovAngleX);

        shapeRenderer.setColor(color);
        shapeRenderer.circle(x, y, renderSize);
    }

    public void computeAngularSize(long distance, float fovAngleX) {
        angularSize = 2 * (float) Math.asin((float) (2 * this.absoluteRadius / distance));
        angularSize /= fovAngleX; // Pour convertir en format normalisé
    }
}
