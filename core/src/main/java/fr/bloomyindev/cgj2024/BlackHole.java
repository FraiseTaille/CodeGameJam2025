package fr.bloomyindev.cgj2024;

import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;
import fr.bloomyindev.cgj2024.CoordinateSystems.SpaceshipRelative;

public class BlackHole {
    private float height;
    private AbsoluteCoords3D coords3d;

    public BlackHole(float height, AbsoluteCoords3D coords3d) {
        this.height = height;
        this.coords3d = coords3d;
    }

    public boolean spaceshipInsideBlackHole(Spaceship spaceship) {
        SpaceshipRelative space = new SpaceshipRelative(this.coords3d,spaceship.getSpaceshipCoord());
        return space.getDistance() <= height;
    }

}
