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
import fr.bloomyindev.cgj2024.Ut;

public class GameScreen implements Screen {
    final Main game;
    final float[] possibleDirections = new float[] { 0, -90, -180, -270 };
    final float maxSpeed = 10;

    Texture playerTexture;
    Sprite playerSprite;
    Vector2 touchPos;

    float direction;
    Vector2 carDir;
    float carSpeed = 0;

    public GameScreen(final Main game) {
        this.game = game;

        this.playerTexture = new Texture("car.png");
        this.playerSprite = new Sprite(this.playerTexture);
        this.playerSprite.setSize(1f, 1.9f);
        this.playerSprite.setOrigin(.5f, .95f);
        this.direction = 0f;
        this.carDir = new Vector2(1, 0);

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
        float accel = 2f;
        float delta = Gdx.graphics.getDeltaTime();

        boolean directionPressed = false;

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.carSpeed = MathUtils.clamp(this.carSpeed + accel, 0, this.maxSpeed);
            directionPressed = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.carSpeed = MathUtils.clamp(this.carSpeed-accel*.2f, 0, this.maxSpeed);
            directionPressed = true;
        }

        this.playerSprite.translate(this.carDir.x * this.carSpeed * delta, this.carDir.y * this.carSpeed * delta);

        if (!directionPressed) {
            this.carSpeed = MathUtils.clamp(this.carSpeed - Math.signum(this.carSpeed) * (.001f / delta) * .5f * Ut.pow(this.carSpeed, 2), 0,
                    this.maxSpeed);

        }

        if (this.carSpeed > .25f || this.carSpeed < -.25f) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                this.carDir.rotateDeg(-90 * delta);
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                this.carDir.rotateDeg(90 * delta);
            }
        }

        // this.playerSprite.setOrigin(this.playerSprite.getX(),
        // this.playerSprite.getY());
        this.playerSprite.setRotation(this.carDir.angleDeg() - 90);
        //System.out.printf("%f %f\n", this.carDir.x, this.carDir.y);
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
        game.font.draw(game.batch,
                String.format("\nX %f\nY %f\ndir %f\nspeed %f", this.playerSprite.getX(), this.playerSprite.getY(),
                        this.carDir.angleDeg(), this.carSpeed),
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
