package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.bloomyindev.cgj2024.CoordinateSystems.*;

public class Star {
    private Sound sound;
    private Color color;
    private AbsoluteCoords3D coords;
    private float angularSize;
    private int absoluteRadius;
    private boolean visited;

    public Star(AbsoluteCoords3D coords, Color color, Sound sound, int absoluteRadius) {
        this.coords = coords;
        this.color = color;
        this.sound = sound;
        this.absoluteRadius = absoluteRadius;
        this.visited = false;
    }


    public Sound getSound() {
        return sound;
    }

    public Color getColor() {
        return color;
    }

    public AbsoluteCoords3D getCoordinates() {
        return coords;
    }

    public int getAbsoluteRadius() {
        return absoluteRadius;
    }


    /*
    * Please use `ShapeRenderer.begin()` before using this method
    */
    public void render(ShapeRenderer shapeRenderer, float x, float y, long distance, float fovAngleX) {
        float renderSize;

        renderSize = angularSize * (16 / fovAngleX);

        shapeRenderer.setColor(color);
        shapeRenderer.circle(x - renderSize, y - renderSize, renderSize, 64);
    }

    public void computeAngularSize(long distance, float fovAngleX) {
        angularSize = 2 * (float) Math.asin((float) (2 * (float)absoluteRadius / distance));
        angularSize /= fovAngleX; // Pour convertir en format normalisé
    }

    public void visit() {
        visited = true;
    }

    public boolean isVisited() {
        return visited;
    }

    @Override
    public String toString() {
        return visited ? "Visited" : "Not Visited";
    }
}
