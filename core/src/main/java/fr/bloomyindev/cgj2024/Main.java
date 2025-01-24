package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fr.bloomyindev.cgj2024.screen.MainMenuScreen;


public class Main extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;
    public SoundManager soundManager;

    public void create() {
        batch = new SpriteBatch();
        // use libGDX's default font
        font = new BitmapFont();
        viewport = new FitViewport(16, 9);
        soundManager = new SoundManager(Gdx.audio.newAudioDevice(44100,false));


        // font has 15pt, but we need to scale it to our viewport by ratio of viewport
        // height to screen height
        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
