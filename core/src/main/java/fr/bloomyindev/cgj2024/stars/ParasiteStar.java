package fr.bloomyindev.cgj2024.stars;

import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;

public class ParasiteStar extends Star {
    public ParasiteStar(AbsoluteCoords3D coords, int absoluteRadius) {
        super(coords, absoluteRadius, false, true, false, false);
    }
}
