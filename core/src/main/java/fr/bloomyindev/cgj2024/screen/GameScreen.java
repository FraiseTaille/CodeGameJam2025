package fr.bloomyindev.cgj2024.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.bloomyindev.cgj2024.CoordinateSystems.*;
import fr.bloomyindev.cgj2024.Main;
import fr.bloomyindev.cgj2024.Spaceship;
import fr.bloomyindev.cgj2024.Star;

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
    private Texture skyboxTexture;
    private Sprite skyboxSprite;
    private Texture cockpitTexture;
    private Sprite cockpitSprite;

    public GameScreen(final Main game) {
        this.game = game;

        spaceship = new Spaceship(new AbsoluteCoords3D(0, 0, 0), 0, 0);
        fov = new FieldOfView(0, 0, fovAngle);

        stars = new ArrayList<Star>();
        starsCoords = new ArrayList<FieldOfViewCoords>();
        spaceshipRelativeToStars = new ArrayList<SpaceshipRelative>();

        orderToDrawStars = new ArrayList<Integer>();

        skyboxTexture = new Texture(Gdx.files.internal("skybox.png"));
        skyboxSprite = new Sprite(skyboxTexture);

        skyboxSprite.setSize(16f, 9f);

        cockpitTexture = new Texture(Gdx.files.internal("cockpit.png"));
        cockpitSprite = new Sprite(cockpitTexture);

        cockpitSprite.setSize(16f, 9f);

        spawnStars();

        for (Star star : stars) {
            spaceshipRelativeToStars.add(new SpaceshipRelative(star.getCoordinates(), spaceship.getSpaceshipCoord()));
            starsCoords
                    .add(new FieldOfViewCoords(fov, spaceshipRelativeToStars.get(spaceshipRelativeToStars.size() - 1)));
        }
        //System.out.println(stars);
    }

    public void spawnStars() {
        stars.add(new Star(new AbsoluteCoords3D(50, 0, 0), Color.CYAN, null, 1));
        stars.add(new Star(new AbsoluteCoords3D(-50, 0, 0), Color.RED, null, 1));
        stars.add(new Star(new AbsoluteCoords3D(0, 50, 0), Color.GREEN, null, 1));
        stars.add(new Star(new AbsoluteCoords3D(0, -50, 0), Color.YELLOW, null, 1));
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
            spaceship.rotateYaw(-10.f * (float) (Math.PI / 180.f) * delta);
            fov.setCenter(spaceship.getPitch(), spaceship.getYaw());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
            spaceship.rotateYaw(10.f * (float) (Math.PI / 180.f) * delta);
            fov.setCenter(spaceship.getPitch(), spaceship.getYaw());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.BUTTON_R1)) {
            spaceship.changeSpeed(-1f * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.BUTTON_R2)) {
            spaceship.changeSpeed(1f * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
            spaceship.rotatePitch(10.f * (float) (Math.PI / 180.f) * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.E) || Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
            spaceship.rotatePitch(-10.f * (float) (Math.PI / 180.f) * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.BUTTON_B)) {
            spaceship.setSpeed(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            spaceship.getSpaceshipCoord().setToOrigin();
        }
    }

    private void logic() {
        spaceship.move();

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

        for (int i = 0; i < starsCoords.size(); i++) {
            Star star = stars.get(i);
            setVisit(star, i);
        }

        //System.out.println(stars);

        orderToDrawStars.clear();
        for (int i = 0; i < spaceshipRelativeToStars.size(); i++) {
            orderToDrawStars.add(i);
        }
    }

    private void setVisit(Star star, int index) {
        if (spaceshipRelativeToStars.get(index).getDistance() <= 5 * star.getAbsoluteRadius() && !star.isVisited()) {
            star.visit();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();

        game.sprite.setProjectionMatrix(game.viewport.getCamera().combined);

        game.sprite.begin();
        skyboxSprite.draw(game.sprite);
        game.sprite.end();

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

                star.render(game.shape, normalisedCoords[0], normalisedCoords[1], relCoords.getDistance(), fovAngle);
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
        game.font.draw(game.sprite, String.format("Coords %f %f %f", coords[0], coords[1], coords[2]), 1f, 1f);

        game.sprite.end();
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
