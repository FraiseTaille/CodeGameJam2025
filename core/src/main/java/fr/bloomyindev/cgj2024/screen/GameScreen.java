package fr.bloomyindev.cgj2024.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.bloomyindev.cgj2024.*;
import fr.bloomyindev.cgj2024.CoordinateSystems.*;
import fr.bloomyindev.cgj2024.stars.*;

import java.util.ArrayList;

public class GameScreen implements Screen {
    final Main game;

    private Spaceship spaceship;
    private ArrayList<Star> stars;
    private ArrayList<FieldOfViewCoords> starsCoords;
    private ArrayList<SpaceshipRelative> spaceshipRelativeToStars;
    private FieldOfView fov;
    private float fovAngle = 70.0f * (float) (Math.PI / 180.0);
    private ArrayList<Integer> orderToDrawStars;
    private Texture cockpitTexture;
    private Sprite cockpitSprite;
    private Texture dangerTexture;
    private Sprite dangerSprite;
    private float[] zone20kX20k;
    private boolean backToCenter;
    private boolean reversingAt0;

    public GameScreen(final Main game) {
        this.game = game;

        spaceship = new Spaceship(new AbsoluteCoords3D(0, 0, 0), 0, 0);
        fov = new FieldOfView(0, 0, fovAngle);

        stars = new ArrayList<Star>();
        starsCoords = new ArrayList<FieldOfViewCoords>();
        spaceshipRelativeToStars = new ArrayList<SpaceshipRelative>();

        orderToDrawStars = new ArrayList<Integer>();

        cockpitTexture = new Texture(Gdx.files.internal("cockpit.png"));
        cockpitSprite = new Sprite(cockpitTexture);

        cockpitSprite.setSize(16f, 9f);

        dangerTexture = new Texture(Gdx.files.internal("danger.png"));
        dangerSprite = new Sprite(dangerTexture);

        dangerSprite.setSize(0.44f, 0.44f);
        dangerSprite.setPosition(6.9f, 2.34f);

        spawnStars();

        for (Star star : stars) {
            spaceshipRelativeToStars.add(new SpaceshipRelative(star.getCoordinates(), spaceship.getSpaceshipCoord()));
            starsCoords.add(new FieldOfViewCoords(fov, spaceshipRelativeToStars.get(spaceshipRelativeToStars.size() - 1)));
        }
        //System.out.println(stars);

        zone20kX20k = new float[4];

        backToCenter = false;

        reversingAt0 = false;
    }

    public void spawnStars() {
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
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    private void input() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
            spaceship.rotateYaw(-15.f * (float) (Math.PI / 180.f) * delta);
            fov.setCenter(spaceship.getPitch(), spaceship.getYaw());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            spaceship.rotateYaw(15.f * (float) (Math.PI / 180.f) * delta);
            fov.setCenter(spaceship.getPitch(), spaceship.getYaw());
        }

        collisionManagement(delta);

        //if (Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
        //    spaceship.rotatePitch(10.f * (float) (Math.PI / 180.f) * delta);
        //}

        //if (Gdx.input.isKeyPressed(Input.Keys.E) || Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
        //    spaceship.rotatePitch(-10.f * (float) (Math.PI / 180.f) * delta);
        //}

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.BUTTON_B)) {
            spaceship.setSpeed(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            spaceship.getSpaceshipCoord().setToOrigin();
            backToCenter = true;
        }
    }

    private void collisionManagement(float delta) {
        int idClosestStar = SpaceshipRelative.smallestDistanceStarId(spaceshipRelativeToStars, stars, false);
        FieldOfViewCoords closestStarCoord = starsCoords.get(idClosestStar);
        SpaceshipRelative spaceshipRelativeToClosestStar = spaceshipRelativeToStars.get(idClosestStar);
        Star star = stars.get(idClosestStar);
        if (closestStarCoord.getVisibility() && spaceshipRelativeToClosestStar.getDistance() <= 6L * star.getAbsoluteRadius()) {
            if (Math.abs(spaceship.getSpeed()) > 0 && !((Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.BUTTON_R2)) && reversingAt0)) {
                spaceship.setSpeed(0);
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.BUTTON_R1)) {
                spaceship.changeSpeed(-1f * delta);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.BUTTON_R2)) {
            if (!reversingAt0 && spaceship.getSpeed() == 0) {
                reversingAt0 = true;
            }
            spaceship.changeSpeed(delta);
        } else {
            reversingAt0 = false;
        }
    }

    private void logic() {
        spaceship.move();

        generateStarsInDelimitedZone();

        int idClosestStar = SpaceshipRelative.smallestDistanceStarId(spaceshipRelativeToStars, stars, true);
        //System.out.println(idClosestStar);
        //System.out.println(spaceshipRelativeToStars.get(idClosestStar).getDistance());

        game.soundManager.playSound(spaceshipRelativeToStars.get(idClosestStar).getDistance(), stars.get(idClosestStar));
        for (int i = 0; i < starsCoords.size(); i++) {
            SpaceshipRelative relCoords = spaceshipRelativeToStars.get(i);
            relCoords.reComputeRelativeCoords(spaceship.getSpaceshipCoord());
            spaceshipRelativeToStars.set(i, relCoords);

            FieldOfViewCoords FOVCoords = starsCoords.get(i);
            FOVCoords.reComputeFOVCoords(relCoords);
            starsCoords.set(i, FOVCoords);

            Star star = stars.get(i);
            star.computeAngularSize(relCoords.getDistance(), fovAngle);
            stars.set(i, star);
        }

        for (int i = 0; i < 16; i++) {
            Star star = stars.get(i);
            if (star.isVisitable()) {
                setVisit(star, i);
            }
        }

        orderToDrawStars.clear();
        for (int i = 0; i < spaceshipRelativeToStars.size(); i++) {
            orderToDrawStars.add(i);
        }

        //System.out.println(spaceship.getSpaceshipCoord().getCoords()[0] + " " + spaceship.getSpaceshipCoord().getCoords()[1] + " " + spaceship.getSpaceshipCoord().getCoords()[2]);
        System.out.println(spaceship.getSpeed());
    }

    private void setVisit(Star star, int index) {
        if (spaceshipRelativeToStars.get(index).getDistance() <= 7L * star.getAbsoluteRadius() && star.isVisitable() && !star.isVisited()) {
            star.visit();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();

        game.sprite.setProjectionMatrix(game.viewport.getCamera().combined);

        game.shape.setProjectionMatrix(game.viewport.getCamera().combined);
        game.shape.setAutoShapeType(true);

        game.shape.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = orderToDrawStars.size()-1; i > -1; i--) {
            int j = orderToDrawStars.get(i);
            FieldOfViewCoords starFOVCoords = starsCoords.get(j);
            float[] normalisedCoords = starFOVCoords.getNormalisedCoords();

            normalisedCoords[0] = (normalisedCoords[0] + 1) * 8f;
            normalisedCoords[1] = (normalisedCoords[1] + 1) * 4.5f;

            if (starFOVCoords.getVisibility()) {
                Star star = stars.get(j);
                SpaceshipRelative relCoords = spaceshipRelativeToStars.get(j);

                if (relCoords.getDistance() < 10000) {
                    star.render(game.shape, normalisedCoords[0], normalisedCoords[1], relCoords.getDistance(), fovAngle);
                }
            }
        }

        game.shape.end();
        game.sprite.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        cockpitSprite.draw(game.sprite);
        game.font.draw(game.sprite, String.format("Yaw %f\nPitch %f", spaceship.getYaw(), spaceship.getPitch()), 7.5f,
                2.75f);

        float[] coords = spaceship.getSpaceshipCoord().getCoords();
        BitmapFont coord = new BitmapFont();
        coord.setUseIntegerPositions(false);
        coord.getData().setScale(0.02f);

        coord.draw(game.sprite, String.format("%d", (int) coords[0]), 6.6f, 7.9f);
        coord.draw(game.sprite, String.format("%d", (int) coords[1]), 7.6f, 7.9f);
        coord.draw(game.sprite, String.format("%d", (int) coords[2]), 8.6f, 7.9f);

        int idClosestStar = SpaceshipRelative.smallestDistanceStarId(spaceshipRelativeToStars, stars, true);
        game.font.draw(game.sprite, String.format("%d", spaceshipRelativeToStars.get(idClosestStar).getDistance()), 3f, 1f);

        if (!stars.get(idClosestStar).isVisitable() && !stars.get(idClosestStar).isCholletStar() && spaceshipRelativeToStars.get(idClosestStar).getDistance() < 300) {
            dangerSprite.draw(game.sprite);
        }

        game.font.draw(game.sprite, String.format("Étoiles visitées : %d / 10", TrueStar.getNbVisites()), 8f, 1f);

        game.sprite.end();
    }

    private void generateStarsInDelimitedZone() {
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
                generateNewDecorativeStar(coordinates);
            }
        } else if (!spaceship.getSpaceshipCoord().identicalZones(zone20kX20k)) {
            int nb = 3;
            int newPartSize = 21 - (int) Math.abs(spaceship.getSpeed());
            boolean updateZone = false;
            if (newZone20kX20k[0] < zone20kX20k[0] - newPartSize && newZone20kX20k[0] >= -50000) {
                for (int j = 0; j < nb; j++) {
                    AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates((int) zone20kX20k[0] - 1, (int) zone20kX20k[0],
                        (int) zone20kX20k[2], (int) zone20kX20k[3], -3000, 3000);
                    generateNewDecorativeStar(coordinates);
                }
                updateZone = true;
            }
            if (newZone20kX20k[1] > zone20kX20k[1] + newPartSize && newZone20kX20k[1] <= 50000) {
                for (int j = 0; j < nb; j++) {
                    AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates((int) zone20kX20k[1], (int) zone20kX20k[1] + 1,
                        (int) zone20kX20k[2], (int) zone20kX20k[3], -3000, 3000);
                    generateNewDecorativeStar(coordinates);
                }
                updateZone = true;
            }
            if (newZone20kX20k[2] < zone20kX20k[2] - newPartSize && newZone20kX20k[2] >= -50000) {
                for (int j = 0; j < nb; j++) {
                    AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates((int) zone20kX20k[0],
                        (int) zone20kX20k[1], (int) zone20kX20k[2] - 1, (int) zone20kX20k[2], -3000, 3000);
                    generateNewDecorativeStar(coordinates);
                }
                updateZone = true;
            }
            if (newZone20kX20k[3] > zone20kX20k[3] + newPartSize && newZone20kX20k[3] <= 50000) {
                for (int j = 0; j < nb; j++) {
                    AbsoluteCoords3D coordinates = AbsoluteCoords3D.generateNewRandomCoordinates((int) zone20kX20k[0],
                        (int) zone20kX20k[1], (int) zone20kX20k[3], (int) zone20kX20k[3] + 1, -3000, 3000);
                    generateNewDecorativeStar(coordinates);
                }
                updateZone = true;
            }
            if (updateZone) {
                zone20kX20k = newZone20kX20k;
            }
            int nbStarsNotInZone = getNbStarsNotInZone();
            if (nbStarsNotInZone > 500) {
                removeStarsNotInZone();
            }
        }
    }

    private int getNbStarsNotInZone() {
        int nbStarsNotInZone = 0;
        for (int i = 0; i < starsCoords.size(); i++) {
            if (!stars.get(i).starIsInZone(zone20kX20k) && stars.get(i).isDecorative()) {
                nbStarsNotInZone++;
            }
        }
        return nbStarsNotInZone;
    }

    private void removeStarsNotInZone() {
        for (int i = 0; i < starsCoords.size(); i++) {
            if (!stars.get(i).starIsInZone(zone20kX20k) && stars.get(i).isDecorative()) {
                stars.remove(i);
                spaceshipRelativeToStars.remove(i);
                starsCoords.remove(i);
                i--;
            }
        }
        System.runFinalization();
        System.gc();
    }

    private void generateNewDecorativeStar(AbsoluteCoords3D coordinates) {
        Star star = new DecorativeStar(coordinates, 1);
        stars.add(star);
        spaceshipRelativeToStars.add(new SpaceshipRelative(star.getCoordinates(), spaceship.getSpaceshipCoord()));
        starsCoords.add(new FieldOfViewCoords(fov, spaceshipRelativeToStars.get(spaceshipRelativeToStars.size() - 1)));
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
