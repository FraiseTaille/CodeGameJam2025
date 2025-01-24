package fr.bloomyindev.cgj2024.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.bloomyindev.cgj2024.CoordinateSystems.AbsoluteCoords3D;
import fr.bloomyindev.cgj2024.CoordinateSystems.FieldOfView;
import fr.bloomyindev.cgj2024.CoordinateSystems.FieldOfViewCoords;
import fr.bloomyindev.cgj2024.CoordinateSystems.SpaceshipRelative;
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
    private ArrayList<Integer> orderToDrawStars;
    private Texture cockpitTexture;
    private Sprite cockpitSprite;

    public GameScreen(final Main game) {
        this.game = game;
        spaceship = new Spaceship(new AbsoluteCoords3D(0, 0, 0), 0, 0);
        fov = new FieldOfView(0, 0, 1.2217f);

        stars = new ArrayList<Star>();
        starsCoords = new ArrayList<FieldOfViewCoords>();
        spaceshipRelativeToStars = new ArrayList<SpaceshipRelative>();
        orderToDrawStars = new ArrayList<Integer>();
        cockpitTexture = new Texture(Gdx.files.internal("cockpit.png"));
        cockpitSprite = new Sprite(cockpitTexture);
        cockpitSprite.setSize(16f,9f);

        spawnStars();

        for (Star star : stars) {
            spaceshipRelativeToStars.add(new SpaceshipRelative(star.getCoordinates(), spaceship.getSpaceshipCoord()));
            starsCoords.add(new FieldOfViewCoords(fov, spaceshipRelativeToStars.get(spaceshipRelativeToStars.size() - 1)));
        }
    }

    public void spawnStars() {
        stars.add(new Star(new AbsoluteCoords3D(0, 50, 0), Color.CYAN, null, 1));
        stars.add(new Star(new AbsoluteCoords3D(50, 0, 0), Color.RED, null, 1));
        stars.add(new Star(new AbsoluteCoords3D(0, 25, 0), Color.GREEN, null, 1));
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

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))  {
            spaceship.rotateLongitude(-0.17f*delta);
            fov.setCenter(spaceship.getPitch(), spaceship.getYaw());

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            spaceship.rotateLongitude(0.17f*delta);
            fov.setCenter(spaceship.getPitch(), spaceship.getYaw());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            System.out.printf("speed %f\n", spaceship.getSpeed());
            spaceship.increaseSpeed(1);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            spaceship.increaseSpeed(-delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            spaceship.setSpeed(0);
        }
    }

    private void logic() {
        for (int i = 0; i < starsCoords.size(); i++) {
            starsCoords.get(i).reComputeFOVCoords(spaceshipRelativeToStars.get(i));
            spaceshipRelativeToStars.get(i).reComputeRelativeCoords(spaceship.getSpaceshipCoord());
            stars.get(i).computeAngularSize(spaceshipRelativeToStars.get(i).getDistance(), fov.getFovAngles()[0]);
        }
        orderToDrawStars.clear();
        for (int i = 0; i < spaceshipRelativeToStars.size(); i++) {
            orderToDrawStars.add(i);
        }
        spaceship.move();
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.sprite.setProjectionMatrix(game.viewport.getCamera().combined);
        game.shape.setProjectionMatrix(game.viewport.getCamera().combined);
        game.shape.setAutoShapeType(true);
        game.shape.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < stars.size(); i++) {
            float[] normalisedCoords = starsCoords.get(i).getNormalisedCoords();
            normalisedCoords[0] = (normalisedCoords[0] + 1) * 8f;
            normalisedCoords[1] = (normalisedCoords[1] + 1) * 4.5f;
            if (starsCoords.get(i).getVisibility()) {
                stars.get(i).render(game.shape, normalisedCoords[0], normalisedCoords[1], spaceshipRelativeToStars.get(i).getDistance(), fov.getFovAngles()[0]);
            }
        }

        game.shape.end();
        game.sprite.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        cockpitSprite.draw(game.sprite);
        game.font.draw(game.sprite, String.format("Yaw %f\nPitch %f", spaceship.getYaw(), spaceship.getPitch()), 7.5f, 2.75f);
        float[] coords = spaceship.getSpaceshipCoord().getCoords();
        game.font.draw(game.sprite, String.format("Coords %f %f %f", coords[0],  coords[1],  coords[2]), 1f, 1f);


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
