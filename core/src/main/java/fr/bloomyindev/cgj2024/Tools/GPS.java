package fr.bloomyindev.cgj2024.Tools;

import fr.bloomyindev.cgj2024.CoordinateSystems.SpaceshipRelative;
import fr.bloomyindev.cgj2024.Spaceship;

import java.util.ArrayList;

public final class GPS {
    private static SpaceshipRelative spaceshipRelClosestStar ;
    private static int idClosestStar = -1;
    private static ArrayList<SpaceshipRelative> spaceshipRelativeToStars = new ArrayList<>();

    public static void updateIdClosestStar(boolean notVisitedOnly) {
        int i = 0;
        for (int j = 0; j < 16; j++) {
            if (spaceshipRelativeToStars.get(j).getDistance() < spaceshipRelativeToStars.get(i).getDistance()) {
                if (notVisitedOnly && !StarManagement.getStarInStarsList(j).isVisited()) {
                    i = j;
                } else if (!notVisitedOnly) {
                    i = j;
                }
            }
        }
        idClosestStar = i;
    }

    public static int getIdClosestStar() {
        return idClosestStar;
    }

    public static boolean shipMovesAway(Spaceship spaceship) {
        return spaceshipRelClosestStar.getDistance() > spaceshipRelClosestStar.getPreviousDistance() ||
            (spaceshipRelClosestStar.getDistance() == spaceshipRelClosestStar.getPreviousDistance() && Math.abs(spaceship.getSpeed()) > 0);
    }

    public static void updateSpaceshipRelClosestStar() {
        updateIdClosestStar(true);
        spaceshipRelClosestStar = spaceshipRelativeToStars.get(idClosestStar);
        spaceshipRelClosestStar.updateSmallestDistance();
    }

    public static long[] getTheoricalDistances(Spaceship spaceship) {

        float averageDelta = 0.015f;

        long[] smallestDistances = new long[] {spaceshipRelClosestStar.getDistance(), spaceshipRelClosestStar.getDistance()};

        Spaceship spaceshipTestLeft = new Spaceship(spaceship);
        Spaceship spaceshipTestRight = new Spaceship(spaceship);

        SpaceshipRelative spaceshipRelTestLeft = new SpaceshipRelative(spaceshipRelClosestStar);
        SpaceshipRelative spaceshipRelTestRight = new SpaceshipRelative(spaceshipRelClosestStar);

        spaceshipTestLeft.setSpeed(-20f);

        spaceshipTestRight.setSpeed(-20f);


        while (Math.abs(smallestDistances[0] - smallestDistances[1]) <= 1 || (smallestDistances[0] >= spaceshipRelClosestStar.getDistance() && smallestDistances[1] >= spaceshipRelClosestStar.getDistance())) {
            spaceshipTestLeft.rotateYaw(-50f * (float) (Math.PI / 180.f) * averageDelta);
            spaceshipTestLeft.move();
            spaceshipRelTestLeft.reComputeRelativeCoords(spaceshipTestLeft.getSpaceshipCoord());
            smallestDistances[0] = spaceshipRelTestLeft.getDistance();

            spaceshipTestRight.rotateYaw(50f * (float) (Math.PI / 180.f) * averageDelta);
            spaceshipTestRight.move();
            spaceshipRelTestRight.reComputeRelativeCoords(spaceshipTestRight.getSpaceshipCoord());
            smallestDistances[1] = spaceshipRelTestRight.getDistance();
        }

        return smallestDistances;
    }

    public static int getBestDistance(long[] theoricalDistances) {
        if (theoricalDistances[0] < theoricalDistances[1]) {
            return 0;
        } else {
            return 1;
        }
    }

    public static SpaceshipRelative getSpaceshipRelativeInList(int index) {
        return spaceshipRelativeToStars.get(index);
    }

    public static ArrayList<SpaceshipRelative> getSpaceshipRelativeToStars() {
        return spaceshipRelativeToStars;
    }

    public static void addSpaceshipRelativeToStar(SpaceshipRelative spaceshipRelativeToStar) {
        spaceshipRelativeToStars.add(spaceshipRelativeToStar);
    }

    public static void removeSpaceshipRelativeToStar(int index) {
        spaceshipRelativeToStars.remove(index);
    }
}
