package fr.bloomyindev.cgj2024.Tools;

import fr.bloomyindev.cgj2024.CoordinateSystems.SpaceshipRelative;
import fr.bloomyindev.cgj2024.Spaceship;

public final class StarSniffer {
    private static SpaceshipRelative spaceshipRelClosestStar ;

    public static boolean shipMovesAway() {
        return spaceshipRelClosestStar.getDistance() > spaceshipRelClosestStar.getSmallestDistance();
    }

    public static void updateSpaceshipRelClosestStar() {
        StarManagement.updateIdClosestStar(true);
        int idClosestStar = StarManagement.getIdClosestStar();
        spaceshipRelClosestStar = StarManagement.getSpaceshipRelativeInList(idClosestStar);
        spaceshipRelClosestStar.updateSmallestDistance();
    }

    public static long[] getTheoricalDistances(Spaceship spaceship) {

        float averageDelta = 0.015f;

        long[] smallestDistances = new long[2];

        Spaceship spaceshipTestLeft = new Spaceship(spaceship);
        Spaceship spaceshipTestRight = new Spaceship(spaceship);

        SpaceshipRelative spaceshipRelTestLeft = new SpaceshipRelative(spaceshipRelClosestStar);
        SpaceshipRelative spaceshipRelTestRight = new SpaceshipRelative(spaceshipRelClosestStar);

        spaceshipTestLeft.rotateYaw(-500f * (float) (Math.PI / 180.f) * averageDelta);
        spaceshipTestLeft.move();
        spaceshipRelTestLeft.reComputeRelativeCoords(spaceshipTestLeft.getSpaceshipCoord());
        smallestDistances[0] = spaceshipRelTestLeft.getDistance();

        spaceshipTestRight.rotateYaw(500f * (float) (Math.PI / 180.f) * averageDelta);
        spaceshipTestRight.move();
        spaceshipRelTestRight.reComputeRelativeCoords(spaceshipTestRight.getSpaceshipCoord());
        smallestDistances[1] = spaceshipRelTestRight.getDistance();

        System.out.println(smallestDistances[0] + " " + smallestDistances[1]);

        return smallestDistances;
    }

    public static int getBestDistance(long[] theoricalDistances) {
        if (theoricalDistances[0] < theoricalDistances[1]) {
            return 0;
        } else if (theoricalDistances[0] > theoricalDistances[1]) {
            return 1;
        } else {
            return 2;
        }
    }
}
