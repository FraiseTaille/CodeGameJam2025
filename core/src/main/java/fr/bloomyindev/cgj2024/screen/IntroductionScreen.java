package fr.bloomyindev.cgj2024.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;

import fr.bloomyindev.cgj2024.Main;

import java.util.ArrayList;

public class IntroductionScreen implements Screen {
    private ArrayList<String> texts;
    private float timeBeforeNextBigThing;
    private int indexText = 0;
    final Main game;
    private boolean doneDisplayingText = false;
    private Texture skyTexture;
    private Sprite skySprite;

    private float time;

    public IntroductionScreen(final Main game) {
        this.game = game;
        this.time = 0;
        timeBeforeNextBigThing = 5;
        this.texts = new ArrayList<>();
        skyTexture = new Texture(Gdx.files.internal("sky.png"));
        skySprite = new Sprite(skyTexture);
        skySprite.setSize(16f,9f);
        addText();
    }

    public void addText() {
        texts.add("Après avoir reçu des signaux étranges provenant du vide spatial,");
        texts.add("une équipe d'explorateurs partira à leur poursuite afin de découvrir leur provenance");
        texts.add("Mais l'espace est semé d'embûches et ils devront se méfier des sons parasites.");
    }

    @Override
    public void render(float idk) {
        ScreenUtils.clear(Color.BLACK);
        float delta = Gdx.graphics.getDeltaTime();
        time+=delta;
        game.viewport.apply();
        game.sprite.setProjectionMatrix(game.viewport.getCamera().combined);


        game.sprite.begin();
        skySprite.draw(game.sprite);
        if (time > timeBeforeNextBigThing) {
            timeBeforeNextBigThing+=5;
            if (indexText == texts.size() - 1) {
                doneDisplayingText = true;
            }
            indexText += indexText < texts.size() - 1 ? 1 : 0;

        }

        game.font.draw(game.sprite, texts.get(indexText), 1, 1.5f);
        if (doneDisplayingText) {
            game.font.draw(game.sprite, "ZQSD/WASD/Flèches: bouger\nEspace: freiner\nCliquez pour continuer", 12f, 1.5f);
        }
        game.sprite.end();
        if (doneDisplayingText && (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.BUTTON_A))) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
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

