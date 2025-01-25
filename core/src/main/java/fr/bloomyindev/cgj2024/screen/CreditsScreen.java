package fr.bloomyindev.cgj2024.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.bloomyindev.cgj2024.Main;

import java.util.ArrayList;

public class CreditsScreen implements Screen {
    final Main game;

    private Music AC = Gdx.audio.newMusic(Gdx.files.internal("ACLong.mp3"));

    public CreditsScreen(final Main game) {
        this.game = game;
        AC.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.sprite.setProjectionMatrix(game.viewport.getCamera().combined);

        game.sprite.begin();
        // draw text. Remember that x and y are in meters
        game.font.draw(game.sprite, "Programmation :", 1, 7);
        game.font.draw(game.sprite, "Luben Bastien", 1, 6.5f);
        game.font.draw(game.sprite, "Rocafort Jordi", 1, 6);
        game.font.draw(game.sprite, "Dubois Clément", 1, 5.5f);
        game.font.draw(game.sprite, "Dellaroli Romain", 1, 5);
        game.font.draw(game.sprite, "Musique :", 1, 4);
        game.font.draw(game.sprite, "Masami Komuro", 1, 3.5f);
        game.font.draw(game.sprite, "Un grand merci à l’équipe d’organisation de la code game jam.", 1, 2.5f);
        game.font.draw(game.sprite, "@suno ai", 1, 2);
        game.sprite.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
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
