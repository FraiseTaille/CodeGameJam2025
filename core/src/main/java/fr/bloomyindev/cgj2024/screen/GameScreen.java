package fr.bloomyindev.cgj2024.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
    private FieldOfViewCoords fovCoords;
    private FieldOfView fov;
    private ArrayList<Integer> orderToDrawStars;

    public GameScreen(final Main game) {
        this.game = game;
        spaceship = new Spaceship(new AbsoluteCoords3D(0, 0, 0), 0, 0);
        fov = new FieldOfView(0, 0, 1.2217f);

        stars = new ArrayList<Star>();
        starsCoords = new ArrayList<FieldOfViewCoords>();
        spaceshipRelativeToStars = new ArrayList<SpaceshipRelative>();
        orderToDrawStars = new ArrayList<Integer>();

        spawnStars();

        for (Star star : stars) {
            spaceshipRelativeToStars.add(new SpaceshipRelative(star.getCoordinates(), spaceship.getSpaceshipCord()));
            starsCoords.add(new FieldOfViewCoords(fov, spaceshipRelativeToStars.get(spaceshipRelativeToStars.size() - 1)));
        }
    }

    public void spawnStars() {
        stars.add(new Star(new AbsoluteCoords3D(10, 0, 0), Color.CYAN, null));
        stars.add(new Star(new AbsoluteCoords3D(20, 0, 0), Color.CYAN, null));
        stars.add(new Star(new AbsoluteCoords3D(0, 15, 0), Color.CYAN, null));
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
    }

    private void logic() {
        for (int i = 0; i < starsCoords.size(); i++) {
            starsCoords.get(i).reComputeFOVCoords(spaceshipRelativeToStars.get(i));
        }
        orderToDrawStars.clear();
        for (int i = 0; i < spaceshipRelativeToStars.size(); i++) {
            orderToDrawStars.add(i);
        }


        for (int i = 0; i < spaceshipRelativeToStars.size(); i++) {
            System.out.printf("%d=>%d\n", i,spaceshipRelativeToStars.get(i).getDistance());

        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.sprite.setProjectionMatrix(game.viewport.getCamera().combined);
        game.shape.setProjectionMatrix(game.viewport.getCamera().combined);
        game.shape.setAutoShapeType(true);
        game.shape.begin(ShapeRenderer.ShapeType.Filled);

        this.stars.get(0).render(game.shape, 8f, 4.5f, 1);

        game.shape.end();
        game.sprite.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        //game.font.draw(game.batch, String.format("Joystick %d", Gdx.input.), 0, 0);


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
