package fr.bloomyindev.cgj2024.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.bloomyindev.cgj2024.Main;

public class MainMenuScreen implements Screen {

    final Main game;

    public MainMenuScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.sprite.setProjectionMatrix(game.viewport.getCamera().combined);

        game.sprite.begin();
        // draw text. Remember that x and y are in meters
        game.font.draw(game.sprite, "Welcome to the game!!! ", 1, 1.5f);
        game.font.draw(game.sprite, "Tap anywhere to begin!", 1, 1);
        game.sprite.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
	public void show() {
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
