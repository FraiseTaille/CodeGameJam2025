package fr.bloomyindev.cgj2024.stars;

import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;
import fr.bloomyindev.cgj2024.CoordinateSystems.FieldOfView;
import fr.bloomyindev.cgj2024.CoordinateSystems.FieldOfViewCoords;
import fr.bloomyindev.cgj2024.CoordinateSystems.SpaceshipRelative;
import fr.bloomyindev.cgj2024.Spaceship;
import fr.bloomyindev.cgj2024.Ut;

import java.util.ArrayList;

public class StarManagement {
    private static ArrayList<Star> stars = new ArrayList<>();
    private static ArrayList<FieldOfViewCoords> starsCoords = new ArrayList<>();
    private static ArrayList<SpaceshipRelative> spaceshipRelativeToStars = new ArrayList<>();
    private static int idClosestStar = -1;
    private static float[] zone20kX20k = new float[4];
    private static boolean backToCenter = false;
    private static int nbRemovedStars = 0;

    public StarManagement() {}

    public static void spawnStars(Spaceship spaceship, FieldOfView fov) {
        stars.add(new Chollet(new AbsoluteCoords3D(Ut.randomMinMax(-50000, 50000), Ut.randomMinMax(-50000, 50000),0), 1));
        for (int i = 0; i < 15; i++) {
            boolean confirmedStar = false;
            while (!confirmedStar) {
                AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates(-50000, 50000, -50000,
                    50000, 0, 0);
                Star star;
                if (i <= 10) {
                    star = new TrueStar(coordinates, 1);
                } else {
                    star = new ParasiteStar(coordinates, 1);
                }
                if (stars.isEmpty()) {
                    confirmedStar = true;
                } else {
                    boolean allConfirmed = true;
                    int j = 0;
                    while (allConfirmed && j < stars.size()) {
                        Star starTest = stars.get(j);
                        if (star.distanceBetween(starTest) < 10000) {
                            allConfirmed = false;
                        }
                        j++;
                    }
                    if (allConfirmed) {
                        confirmedStar = true;
                    }
                }
                if (confirmedStar) {
                    stars.add(star);
                }
            }
        }
        for (Star star : stars) {
            spaceshipRelativeToStars.add(new SpaceshipRelative(star.getCoordinates(), spaceship.getSpaceshipCoord()));
            starsCoords.add(new FieldOfViewCoords(fov, spaceshipRelativeToStars.get(spaceshipRelativeToStars.size() - 1)));
        }
    }

    public static void updateIdClosestStar(boolean notVisitedOnly) {
        idClosestStar = SpaceshipRelative.smallestDistanceStarId(spaceshipRelativeToStars, stars, notVisitedOnly);
    }

    public static int getIdClosestStar() {
        return idClosestStar;
    }

    public static void generateStarsInDelimitedZone(Spaceship spaceship, FieldOfView fov) {
        float[] newZone20kX20k = spaceship.getSpaceshipCoord().get20kX20kZone();
        if (zone20kX20k[0] == 0 && zone20kX20k[1] == 0 && zone20kX20k[2] == 0 && zone20kX20k[3] == 0 || backToCenter) {
            zone20kX20k = newZone20kX20k;
            if (backToCenter) {
                for (int i = 0; i < starsCoords.size(); i++) {
                    if (stars.get(i).starIsInZone(zone20kX20k) && stars.get(i).isDecorative()) {
                        stars.remove(i);
                        spaceshipRelativeToStars.remove(i);
                        starsCoords.remove(i);
                        i--;
                    }
                }
                backToCenter = false;
            }
            for (int i = 0; i < 5000; i++) {
                AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates((int) zone20kX20k[0], (int) zone20kX20k[1],
                    (int) zone20kX20k[2], (int) zone20kX20k[3], -3000, 3000);
                generateNewDecorativeStar(coordinates, spaceship, fov);
            }
        } else if (!spaceship.getSpaceshipCoord().identicalZones(zone20kX20k)) {
            int nb = 3;
            int newPartSize = 21 - (int) Math.abs(spaceship.getSpeed());
            boolean updateZone = false;
            if (newZone20kX20k[0] < zone20kX20k[0] - newPartSize && newZone20kX20k[0] >= -50000) {
                for (int j = 0; j < nb; j++) {
                    AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates((int) zone20kX20k[0] - 1, (int) zone20kX20k[0],
                        (int) zone20kX20k[2], (int) zone20kX20k[3], -3000, 3000);
                    generateNewDecorativeStar(coordinates, spaceship, fov);
                }
                updateZone = true;
            }
            if (newZone20kX20k[1] > zone20kX20k[1] + newPartSize && newZone20kX20k[1] <= 50000) {
                for (int j = 0; j < nb; j++) {
                    AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates((int) zone20kX20k[1], (int) zone20kX20k[1] + 1,
                        (int) zone20kX20k[2], (int) zone20kX20k[3], -3000, 3000);
                    generateNewDecorativeStar(coordinates, spaceship, fov);
                }
                updateZone = true;
            }
            if (newZone20kX20k[2] < zone20kX20k[2] - newPartSize && newZone20kX20k[2] >= -50000) {
                for (int j = 0; j < nb; j++) {
                    AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates((int) zone20kX20k[0],
                        (int) zone20kX20k[1], (int) zone20kX20k[2] - 1, (int) zone20kX20k[2], -3000, 3000);
                    generateNewDecorativeStar(coordinates, spaceship, fov);
                }
                updateZone = true;
            }
            if (newZone20kX20k[3] > zone20kX20k[3] + newPartSize && newZone20kX20k[3] <= 50000) {
                for (int j = 0; j < nb; j++) {
                    AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates((int) zone20kX20k[0],
                        (int) zone20kX20k[1], (int) zone20kX20k[3], (int) zone20kX20k[3] + 1, -3000, 3000);
                    generateNewDecorativeStar(coordinates, spaceship, fov);
                }
                updateZone = true;
            }
            if (updateZone) {
                zone20kX20k = newZone20kX20k;
            }
            removeStarsNotInZone();
            if (nbRemovedStars > 500) {
                System.runFinalization();
                System.gc();
                nbRemovedStars = 0;
            }
        }
    }

    private static void generateNewDecorativeStar(AbsoluteCoords3D coordinates, Spaceship spaceship, FieldOfView fov) {
        Star star = new DecorativeStar(coordinates, 1);
        stars.add(star);
        spaceshipRelativeToStars.add(new SpaceshipRelative(star.getCoordinates(), spaceship.getSpaceshipCoord()));
        starsCoords.add(new FieldOfViewCoords(fov, spaceshipRelativeToStars.get(spaceshipRelativeToStars.size() - 1)));
    }

    private static void removeStarsNotInZone() {
        for (int i = 0; i < starsCoords.size(); i++) {
            if (!stars.get(i).starIsInZone(zone20kX20k) && stars.get(i).isDecorative()) {
                stars.remove(i);
                spaceshipRelativeToStars.remove(i);
                starsCoords.remove(i);
                i--;
                nbRemovedStars++;
            }
        }
    }

    public static void goBackToCenter() {
        backToCenter = true;
    }

    public static ArrayList<Star> getStars() {
        return stars;
    }

    public static Star getStarInStarsList(int index) {
        return stars.get(index);
    }

    public static ArrayList<FieldOfViewCoords> getStarsCoords() {
        return starsCoords;
    }

    public static FieldOfViewCoords getCoordInStarCoords(int index) {
        return starsCoords.get(index);
    }

    public static ArrayList<SpaceshipRelative> getSpaceshipRelativeToStars() {
        return spaceshipRelativeToStars;
    }

    public static SpaceshipRelative getSpaceshipRelativeInList(int index) {
        return spaceshipRelativeToStars.get(index);
    }
}
