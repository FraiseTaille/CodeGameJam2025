package fr.bloomyindev.cgj2024;

import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;

public class DecorativeStar extends Star {

    public DecorativeStar(AbsoluteCoords3D coords, int absoluteRadius) {
        super(coords, absoluteRadius, false);
    }

    @Override
    public boolean isDecorative() {
        return true;
    }
}
