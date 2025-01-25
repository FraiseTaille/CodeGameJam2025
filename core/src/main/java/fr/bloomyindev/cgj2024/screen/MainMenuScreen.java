package fr.bloomyindev.cgj2024.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.bloomyindev.cgj2024.Main;

public class MainMenuScreen implements Screen {

    final Main game;
    private BitmapFont fontBig;

    public MainMenuScreen(final Main game) {
        this.game = game;
        fontBig = new BitmapFont();
        fontBig.setUseIntegerPositions(false);
        fontBig.getData().setScale(.0625f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.sprite.setProjectionMatrix(game.viewport.getCamera().combined);

        game.sprite.begin();
        // draw text. Remember that x and y are in meters
        fontBig.draw(game.sprite, "The Symphony Of Stars", 3f, 5f);
        game.font.draw(game.sprite, "Cliquez pour commencer !" + "\n" + "Appuyez sur C pour voir les crédits", 1, 1);
        game.sprite.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.BUTTON_A)) {
            game.setScreen(new IntroductionScreen(game));
            dispose();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            game.setScreen(new CreditsScreen(game));
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
