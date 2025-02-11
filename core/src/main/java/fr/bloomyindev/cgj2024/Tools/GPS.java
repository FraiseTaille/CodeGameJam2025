package fr.bloomyindev.cgj2024.Tools;

import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;
import fr.bloomyindev.cgj2024.CoordinateSystems.SpaceshipRelative;
import fr.bloomyindev.cgj2024.Spaceship;
import fr.bloomyindev.cgj2024.Stars.Star;

import java.util.ArrayList;

public final class GPS {
    private static SpaceshipRelative spaceshipRelClosestStar ;
    private static int idClosestStar = -1;
    private static ArrayList<SpaceshipRelative> spaceshipRelativeToStars = new ArrayList<>();
    private static AbsoluteCoords3D shipCoords;
    private static float shipYaw;

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
        shipCoords = spaceship.getSpaceshipCoord();
        shipYaw = spaceship.getYaw();

        long[] smallestDistances = new long[] {spaceshipRelClosestStar.getDistance(), spaceshipRelClosestStar.getDistance()};

        SpaceshipRelative spaceshipRelativeLeft = new SpaceshipRelative(spaceshipRelClosestStar);
        SpaceshipRelative spaceshipRelativeRight = new SpaceshipRelative(spaceshipRelClosestStar);

        AbsoluteCoords3D leftCoords = new AbsoluteCoords3D(shipCoords);
        AbsoluteCoords3D rightCoords = new AbsoluteCoords3D(shipCoords);

        float shipYawLeft = shipYaw;
        float shipYawRight = shipYaw;

        while (Math.abs(smallestDistances[0] - smallestDistances[1]) <= 1 || (smallestDistances[0] >= spaceshipRelClosestStar.getDistance() && smallestDistances[1] >= spaceshipRelClosestStar.getDistance())) {
            shipYawLeft = getChangedYaw(true, shipYawLeft);
            float[] speedCoordsLeft = getSpeedCoords(shipYawLeft);
            leftCoords.move(speedCoordsLeft[0], speedCoordsLeft[1], speedCoordsLeft[2]);
            spaceshipRelativeLeft.reComputeRelativeCoords(leftCoords);
            smallestDistances[0] = spaceshipRelativeLeft.getDistance();

            shipYawRight = getChangedYaw(false, shipYawRight);
            float[] speedCoordsRight = getSpeedCoords(shipYawRight);
            rightCoords.move(speedCoordsRight[0], speedCoordsRight[1], speedCoordsRight[2]);
            spaceshipRelativeRight.reComputeRelativeCoords(rightCoords);
            smallestDistances[1] = spaceshipRelativeRight.getDistance();
        }

        return smallestDistances;
    }

    public static float getChangedYaw(boolean left, float yaw) {
        float averageDelta = 0.015f;
        if (left) {
            yaw += -50f * (float) (Math.PI / 180.f) * averageDelta;

        } else {
            yaw += 50f * (float) (Math.PI / 180.f) * averageDelta;
        }
        if (yaw < -Math.PI+.02f) {
            yaw = (float)Math.PI+.005f;
        } else if (yaw > Math.PI+.02f) {
            yaw = -(float)Math.PI+.0625f;
        }
        return yaw;
    }

    public static float[] getSpeedCoords(float yaw) {
        float speedZ = 0f;
        float speedY = (float) Math.sin(yaw) * (float) Math.cos(0) * -20f;
        float speedX = (float) Math.cos(yaw) * (float) Math.cos(0) * -20f;
        return new float[] {speedX, speedY, speedZ};
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

    public static Star getClosestStar() {
        return StarManagement.getStarInStarsList(idClosestStar);
    }

    public static SpaceshipRelative getClosestStarRelative() {
        return spaceshipRelativeToStars.get(idClosestStar);
    }
}
