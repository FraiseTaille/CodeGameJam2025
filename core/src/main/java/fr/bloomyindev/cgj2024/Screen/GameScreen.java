package fr.bloomyindev.cgj2024.Screen;

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
import fr.bloomyindev.cgj2024.Tools.StarManagement;
import fr.bloomyindev.cgj2024.Stars.*;
import fr.bloomyindev.cgj2024.Tools.GPS;

public class GameScreen implements Screen {
    final Main game;

    private Spaceship spaceship;
    private FieldOfView fov;
    private float fovAngle = 70.0f * (float) (Math.PI / 180.0);
    private Texture cockpitTexture;
    private Sprite cockpitSprite;
    private Texture dangerTexture;
    private Sprite dangerSprite;
    private BitmapFont coord;
    private Texture leftTexture;
    private Sprite leftSprite;
    private Texture rightTexture;
    private Sprite rightSprite;
    private int nbRepetitions;
    private int leftRight;

    public GameScreen(final Main game) {
        this.game = game;

        spaceship = new Spaceship(new AbsoluteCoords3D(0, 0, 0), 0, 0);
        fov = new FieldOfView(0, 0, fovAngle);

        cockpitTexture = new Texture(Gdx.files.internal("cockpit.png"));
        cockpitSprite = new Sprite(cockpitTexture);

        cockpitSprite.setSize(16f, 9f);

        dangerTexture = new Texture(Gdx.files.internal("danger.png"));
        dangerSprite = new Sprite(dangerTexture);
        dangerSprite.setSize(0.44f, 0.44f);
        dangerSprite.setPosition(6.9f, 2.34f);

        leftTexture = new Texture(Gdx.files.internal("left.png"));
        leftSprite = new Sprite(leftTexture);
        leftSprite.setSize(0.44f, 0.44f);
        leftSprite.setPosition(6f, 2.34f);

        rightTexture = new Texture(Gdx.files.internal("right.png"));
        rightSprite = new Sprite(rightTexture);
        rightSprite.setSize(0.44f, 0.44f);
        rightSprite.setPosition(9.65f, 2.34f);

        StarManagement.spawnStars(spaceship, fov);
        //System.out.println(stars);

        coord = new BitmapFont();
        coord.setUseIntegerPositions(false);
        coord.getData().setScale(0.02f);

        nbRepetitions = 0;
        leftRight = -1;
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

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.BUTTON_R2)) {
            spaceship.changeSpeed(delta);
        }

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
            StarManagement.goBackToCenter();
        }
    }

    private void collisionManagement(float delta) {
        GPS.updateIdClosestStar(false);

        int idClosestStar = GPS.getIdClosestStar();

        FieldOfViewCoords closestStarCoord = StarManagement.getCoordInStarCoords(idClosestStar);
        SpaceshipRelative spaceshipRelativeToClosestStar = GPS.getSpaceshipRelativeInList(idClosestStar);
        Star star = StarManagement.getStarInStarsList(idClosestStar);

        if (closestStarCoord.getVisibility() && spaceshipRelativeToClosestStar.getDistance() <= 6L * star.getAbsoluteRadius()) {
            if (spaceship.getSpeed() < 0) {
                spaceship.setSpeed(0);
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.BUTTON_R1)) {
                spaceship.changeSpeed(-1f * delta);
            }
        }
    }

    private void logic() {
        spaceship.updateSpeedCoords();

        GPS.updateIdClosestStar(true);

        if (GPS.getClosestStar().isParasite() && GPS.getClosestStarRelative().getDistance() <= 10000) {
            float parasiteYaw = GPS.getClosestStarRelative().getLng();
            float speedToParasite = (-10f) / 10000f * (10000f - (float) GPS.getClosestStarRelative().getDistance());
            spaceship.adjustMovement(parasiteYaw, speedToParasite);
        }

        spaceship.move();

        StarManagement.generateStarsInDelimitedZone(spaceship, fov);

        //System.out.println(idClosestStar);
        //System.out.println(spaceshipRelativeToStars.get(idClosestStar).getDistance());

        game.soundManager.playSound(GPS.getSpaceshipRelativeInList(GPS.getIdClosestStar()).getDistance(), StarManagement.getStarInStarsList(GPS.getIdClosestStar()));

        for (int i = 0; i < StarManagement.getStarsCoords().size(); i++) {
            SpaceshipRelative relCoords = GPS.getSpaceshipRelativeInList(i);
            relCoords.reComputeRelativeCoords(spaceship.getSpaceshipCoord());

            FieldOfViewCoords FOVCoords = StarManagement.getCoordInStarCoords(i);
            FOVCoords.reComputeFOVCoords(relCoords);

            Star star = StarManagement.getStarInStarsList(i);
            star.computeAngularSize(relCoords.getDistance(), fovAngle);
            if (i < 16) {
                if (star.isVisitable()) {
                    setVisit(star, i);
                }
            }
        }
    }

    private void setVisit(Star star, int index) {
        if (GPS.getSpaceshipRelativeInList(index).getDistance() <= 7L * star.getAbsoluteRadius() && star.isVisitable() && !star.isVisited()) {
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

        drawStars();

        game.shape.end();
        game.sprite.begin();

        //float worldWidth = game.viewport.getWorldWidth();
        //float worldHeight = game.viewport.getWorldHeight();

        cockpitSprite.draw(game.sprite);
        game.font.draw(game.sprite, String.format("Yaw %f\nPitch %f", spaceship.getYaw(), spaceship.getPitch()), 7.5f, 2.75f);

        float[] coords = spaceship.getSpaceshipCoord().getCoords();

        coord.draw(game.sprite, String.format("%d", (int) coords[0]), 6.6f, 7.9f);
        coord.draw(game.sprite, String.format("%d", (int) coords[1]), 7.6f, 7.9f);
        coord.draw(game.sprite, String.format("%d", (int) coords[2]), 8.6f, 7.9f);

        GPS.updateIdClosestStar(true);
        int idClosestStar = GPS.getIdClosestStar();
        //game.font.draw(game.sprite, String.format("%d", StarManagement.getSpaceshipRelativeInList(idClosestStar).getDistance()), 3f, 1f);

        if (StarManagement.getStarInStarsList(idClosestStar).isParasite() && GPS.getSpaceshipRelativeInList(idClosestStar).getDistance() < 300) {
            dangerSprite.draw(game.sprite);
        }

        drawGPS();

        game.font.draw(game.sprite, String.format("Étoiles visitées : %d / 10", TrueStar.getNbVisites()), 8f, 1f);

        game.sprite.end();
    }

    private void drawStars() {
        for (int i = GPS.getSpaceshipRelativeToStars().size() -1; i > -1; i--) {
            FieldOfViewCoords starFOVCoords = StarManagement.getCoordInStarCoords(i);
            float[] normalisedCoords = starFOVCoords.getNormalisedCoords();

            normalisedCoords[0] = (normalisedCoords[0] + 1) * 8f;
            normalisedCoords[1] = (normalisedCoords[1] + 1) * 4.5f;

            if (starFOVCoords.getVisibility()) {
                Star star = StarManagement.getStarInStarsList(i);
                SpaceshipRelative relCoords = GPS.getSpaceshipRelativeInList(i);

                if (relCoords.getDistance() < 10000) {
                    star.render(game.shape, normalisedCoords[0], normalisedCoords[1], relCoords.getDistance(), fovAngle);
                }
            }
        }
    }

    private void drawGPS() {
        GPS.updateSpaceshipRelClosestStar();
        if (GPS.shipMovesAway(spaceship)) {
            long[] theoricalDistances = GPS.getTheoricalDistances(spaceship);
            if (GPS.getBestDistance(theoricalDistances) == 0) {
                leftSprite.draw(game.sprite);
                leftRight = 0;
                nbRepetitions = 0;
            } else if (GPS.getBestDistance(theoricalDistances) == 1) {
                rightSprite.draw(game.sprite);
                leftRight = 1;
                nbRepetitions = 0;
            }
        } else {
            if (leftRight == 0 && nbRepetitions < 100) {
                leftSprite.draw(game.sprite);
                nbRepetitions++;
            } else if (leftRight == 1 && nbRepetitions < 100) {
                rightSprite.draw(game.sprite);
                nbRepetitions++;
            } else {
                leftRight = -1;
                nbRepetitions = 0;
            }
        }
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
