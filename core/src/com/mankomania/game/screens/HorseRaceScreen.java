package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.Connection;
import com.mankomania.game.MankomaniaGame;

import java.security.SecureRandom;

import io.socket.emitter.Emitter;

public class HorseRaceScreen extends ScreenAdapter {
    private final Stage stage;
    private final Texture background;
    private final Texture horseTexture;
    private final SpriteBatch batch;
    private final SecureRandom random;

    private float elapsedTime;
    private boolean horseWon;

    private Horse[] horses;

    public HorseRaceScreen() {
        stage = new Stage();
        batch = new SpriteBatch();
        background = new Texture("race-board.png");
        horseTexture = new Texture("horse.png");
        elapsedTime = 0;
        horses = new Horse[4];
        random = new SecureRandom();
        horseWon = false;

        horses[0] = new Horse(Gdx.graphics.getWidth() / 200f, Gdx.graphics.getHeight() / 60f);
        horses[1] = new Horse(Gdx.graphics.getWidth() / 200f, (Gdx.graphics.getHeight() / 60f) * 12);
        horses[2] = new Horse(Gdx.graphics.getWidth() / 200f, (Gdx.graphics.getHeight() / 60f) * 22);
        horses[3] = new Horse(Gdx.graphics.getWidth() / 200f, (Gdx.graphics.getHeight() / 60f) * 32);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!horseWon) {
            elapsedTime += delta;
            if (elapsedTime > 0.3) {
                Horse movingHorse = horses[random.nextInt(horses.length)];
                movingHorse.moveForward();
                elapsedTime = 0;
                if (movingHorse.getMovedSteps() == 7) {
                    horseWon = true;
                }
            }
        }
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        drawHorses();
        batch.end();
    }

    public void drawHorses() {
        for (int i = 0; i < horses.length; i++) {
            batch.draw(horseTexture, horses[i].getXPosition(), horses[i].getYPosition(), Horse.getWidth(), Horse.getHeight());
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        horseTexture.dispose();
    }
}
