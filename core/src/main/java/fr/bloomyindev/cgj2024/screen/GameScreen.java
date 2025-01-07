package fr.bloomyindev.cgj2024.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.bloomyindev.cgj2024.Main;

public class GameScreen implements Screen {
    final Main game;

    Texture playerTexture;
    Sprite playerSprite;
    Vector2 touchPos;
    final float[] possibleDirections = new float[] { 0, -90, -180, -270 };
    float direction;

    public GameScreen(final Main game) {
        this.game = game;

        this.playerTexture = new Texture("car.png");
        this.playerSprite = new Sprite(this.playerTexture);
        this.playerSprite.setSize(1f, 1.9f);
        this.playerSprite.setOrigin(.5f, .95f);
        this.direction = 0f;

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
        float speed = 2f;
        float delta = Gdx.graphics.getDeltaTime();
        boolean[] directionPressed = new boolean[4];

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.playerSprite.translateY(speed * delta);
            directionPressed[0] = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.playerSprite.translateY(-speed * delta);
            directionPressed[2] = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.playerSprite.translateX(speed * delta);
            directionPressed[1] = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.playerSprite.translateX(-speed * delta);
            directionPressed[3] = true;
        }

        for (int i = 0; i < directionPressed.length; i++) {
            if (directionPressed[i])
                this.direction = possibleDirections[i];
        }
        for (int i = 0; i < directionPressed.length; i++) {
            if (directionPressed[i] && directionPressed[(i + 1) % 4])
                this.direction = possibleDirections[i] - 45;

        }

        // this.playerSprite.setOrigin(this.playerSprite.getX(),
        // this.playerSprite.getY());
        this.playerSprite.setRotation(this.direction);

        // System.out.printf("%f %f\n", this.playerSprite.getOriginY(),
        // this.playerSprite.getOriginY());
        // if (Gdx.input.isTouched()) {
        // touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        // game.viewport.unproject(touchPos);
        // this.playerSprite.setCenterX(touchPos.x);
        // }
    }

    private void logic() {
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float carWidth = this.playerSprite.getWidth();
        float carHeight = this.playerSprite.getHeight();
        float delta = Gdx.graphics.getDeltaTime();
        this.playerSprite.setX(MathUtils.clamp(this.playerSprite.getX(), 0, worldWidth - carWidth));
        this.playerSprite.setY(MathUtils.clamp(this.playerSprite.getY(), 0, worldHeight - carHeight));
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        this.playerSprite.draw(game.batch);
        game.font.draw(game.batch, String.format("\nX %f\nY %f", this.playerSprite.getX(), this.playerSprite.getY()),
                .25f, worldHeight - .25f);

        game.batch.end();
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
        this.playerTexture.dispose();
    }
}
