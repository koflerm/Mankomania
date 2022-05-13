package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.security.SecureRandom;

import horseRaceLogic.Horse;

public class HorseRaceScreen extends ScreenAdapter {
    private final Stage stage;
    private final Texture background;
    private final Texture horseTexture;
    private final SecureRandom random;

    private float elapsedTime;
    private boolean horseWon;

    public HorseRaceScreen() {
        stage = new Stage();
        background = new Texture("race-board.png");
        horseTexture = new Texture("horse.png");
        elapsedTime = 0;
        random = new SecureRandom();
        horseWon = false;

        stage.addActor(new Horse(Gdx.graphics.getWidth() / 15f, Gdx.graphics.getHeight() / 15f, horseTexture));
        stage.addActor(new Horse(Gdx.graphics.getWidth() / 15f, (Gdx.graphics.getHeight() / 15f) * 3.7f, horseTexture));
        stage.addActor(new Horse(Gdx.graphics.getWidth() / 15f, (Gdx.graphics.getHeight() / 15f) * 6.4f, horseTexture));
        stage.addActor(new Horse(Gdx.graphics.getWidth() / 15f, (Gdx.graphics.getHeight() / 15f) * 9.2f, horseTexture));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!horseWon) {
            elapsedTime += delta;
            if (elapsedTime > 0.4) {
                Horse movingHorse = (Horse) stage.getActors().get(random.nextInt(4));
                movingHorse.moveForward();
                elapsedTime = 0;
                if (movingHorse.getMovedSteps() == 9) {
                    horseWon = true;
                }
            }
        }
        stage.act(delta);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        horseTexture.dispose();
    }
}
