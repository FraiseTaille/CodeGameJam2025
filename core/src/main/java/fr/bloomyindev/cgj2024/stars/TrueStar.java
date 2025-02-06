package fr.bloomyindev.cgj2024.stars;

import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;

public class TrueStar extends Star {
    private static int nbVisites = 0;

    public TrueStar(AbsoluteCoords3D coords, int absoluteRadius) {
        super(coords, absoluteRadius, true, false, false, false);
    }

    public static int getNbVisites() {
        return nbVisites;
    }

    public static void augmenterNbVisites() {
        nbVisites++;
    }
}
