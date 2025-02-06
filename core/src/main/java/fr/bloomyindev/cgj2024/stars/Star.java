package fr.bloomyindev.cgj2024.stars;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.bloomyindev.cgj2024.CoordinateSystems.*;
import fr.bloomyindev.cgj2024.Ut;

public class Star {
    private Color color;
    private AbsoluteCoords3D coords;
    private float angularSize;
    private int absoluteRadius;
    private ArrayList<Color> listeCouleur;
    private boolean isVisitable;
    private boolean isParasite;
    private boolean isDecorative;
    private boolean isChollet;
    private boolean visited;

    public Star(AbsoluteCoords3D coords, int absoluteRadius, boolean isVisitable, boolean isParasite, boolean isDecorative, boolean isChollet) {
        this.coords = coords;
        this.absoluteRadius = absoluteRadius;
        this.isVisitable = isVisitable;
        this.isParasite = isParasite;
        this.isDecorative = isDecorative;
        this.isChollet = isChollet;
        genererCouleurs();
    }

    public void genererCouleurs() {
        if (isVisitable() || isParasite()) {
            listeCouleur = new ArrayList<>();
            listeCouleur.add(Color.BLUE);
            listeCouleur.add(Color.BROWN);
            listeCouleur.add(Color.RED);
            listeCouleur.add(Color.PURPLE);
            listeCouleur.add(Color.ORANGE);
            Random random = new Random();
            int randomNumber = random.nextInt(4);
            this.color = listeCouleur.get(randomNumber);
        } else if (isCholletStar()) {
            this.color = Color.PINK;
        } else {
            this.color = Color.WHITE;
        }
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

    public long distanceBetween(Star star2) {
        float[] Delta3D = star2.getCoordinates().getDelta(this.coords);

        float xRel = Delta3D[0];
        float yRel = Delta3D[1];
        float zRel = Delta3D[2];

        return (long) Math.sqrt(Ut.pow(xRel, 2.0f) + Ut.pow(yRel, 2.0f) + Ut.pow(zRel, 2.0f));
    }


    /*
    * Please use `ShapeRenderer.begin()` before using this method
    */
    public void render(ShapeRenderer shapeRenderer, float x, float y, long distance, float fovAngleX) {
        float renderSize;

        renderSize = angularSize * (16 / fovAngleX);

        shapeRenderer.setColor(color);
        shapeRenderer.circle(x, y, renderSize, 64);
    }

    public void computeAngularSize(long distance, float fovAngleX) {
        angularSize = 2 * (float) Math.asin((float) (2 * (float)absoluteRadius / distance));
        angularSize /= fovAngleX; // Pour convertir en format normalisé
    }

    public boolean isVisitable() {
        return isVisitable;
    }

    public boolean isCholletStar() {
        return isChollet;
    }

    public boolean isDecorative() {return isDecorative;}

    public boolean starIsInZone(float[] zone) {
        return coords.isInZone(zone);
    }

    public boolean isParasite() {
        return isParasite;
    }

    public void visit() {
        visited = isVisitable();
        TrueStar.augmenterNbVisites();
    }

    public boolean isVisited() {
        return visited;
    }
}
