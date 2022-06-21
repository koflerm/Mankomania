package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.MankomaniaGame;

import java.security.SecureRandom;

import horseRaceLogic.Horse;
import playerlogic.Player;

public class HorseRaceScreen extends ScreenAdapter {
    private final Stage stage;
    private final Texture background;
    private final Texture horseTexture;
    private final SecureRandom random;
    private final Skin skin;
    private final Dialog winnerDialog;
    private float elapsedTime;
    private Horse winningHorse;
    private boolean winnerDialogShown;
    private boolean winnerDialogNeeded;

    private static final String BOARD_TEXT_STYLE = "title";

    public HorseRaceScreen() {
        stage = new Stage();
        background = new Texture("race-board.png");
        horseTexture = new Texture("horse.png");
        elapsedTime = 0;
        random = new SecureRandom();
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        winnerDialog = new Dialog("INFO", skin, "alt") {};
        winnerDialogShown = false;
        winnerDialogNeeded = false;

        //Dummy players till we have actual players
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        Player p4 = new Player();

        p1.setPlayerIndex(1);
        p2.setPlayerIndex(2);
        p3.setPlayerIndex(3);
        p4.setPlayerIndex(4);

        stage.addActor(new Horse(Gdx.graphics.getWidth() / 15f, Gdx.graphics.getHeight() / 15f, horseTexture, p4));
        stage.addActor(new Horse(Gdx.graphics.getWidth() / 15f, (Gdx.graphics.getHeight() / 15f) * 3.7f, horseTexture, p3));
        stage.addActor(new Horse(Gdx.graphics.getWidth() / 15f, (Gdx.graphics.getHeight() / 15f) * 6.4f, horseTexture, p2));
        stage.addActor(new Horse(Gdx.graphics.getWidth() / 15f, (Gdx.graphics.getHeight() / 15f) * 9.2f, horseTexture, p1));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        elapsedTime += delta;
        if (winningHorse == null) {
            if (elapsedTime > 0.4) {
                Horse movingHorse = (Horse) stage.getActors().get(random.nextInt(4));
                movingHorse.moveForward();
                elapsedTime = 0;
                if (movingHorse.getMovedSteps() == 9) {
                    winningHorse = movingHorse;
                    winnerDialogNeeded = true;
                }
            }
        } else {
            if (!winnerDialogShown && winnerDialogNeeded) {
                drawWinnerDialog(winningHorse);
                winnerDialogShown = true;
                elapsedTime = 0;
            } else if (elapsedTime > 4) {
                returnToGameScreen();
            }
        }
        stage.act(delta);
        stage.draw();
    }

    private void drawWinnerDialog(Horse horse) {
        float scale = Gdx.graphics.getWidth() / 1000f;

        Table tab = new Table();
        tab.align(Align.center);
        Label winnerLabel = new Label("Race finished!", skin, BOARD_TEXT_STYLE);
        Label playerNameLabel = new Label("The winner is Player " + horse.getPlayer().getPlayerIndex(), skin, BOARD_TEXT_STYLE);

        tab.add(winnerLabel).row();
        tab.add(playerNameLabel).row();
        tab.pad(20);

        Label l4 = new Label("Returning to game screen...", skin, "default");
        tab.add(l4).row();

        winnerDialog.getContentTable().add(tab);
        winnerDialog.setScale(scale);
        winnerDialog.show(stage);

        float dialogXPosition = (Gdx.graphics.getWidth() / 2f) - ((winnerDialog.getWidth() * scale) / 2f);
        float dialogYPosition = (Gdx.graphics.getHeight() / 2f) - ((winnerDialog.getHeight() * scale) / 2f);

        winnerDialog.setPosition(dialogXPosition, dialogYPosition);
    }

    public void returnToGameScreen() {
        this.winnerDialogShown = false;
        this.winnerDialogNeeded = false;
        this.winnerDialog.hide();
        MankomaniaGame.getInstance().disposeCurrentScreen();
        MankomaniaGame.getInstance().setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        horseTexture.dispose();
    }
}
