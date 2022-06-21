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
import com.mankomania.game.MankomaniaGame;

public class StartScreen extends ScreenAdapter {
    private final Stage stage;
    private final Texture background;
    private final SpriteBatch batch;
    private InputMultiplexer inputMultiplexer;


    public StartScreen() {
        stage = new Stage();
        batch = new SpriteBatch();
        background = new Texture("background.jpg");

        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }
        initTableAndAddActor(stage);

    }

    private void initTableAndAddActor(Stage stage) {
        Skin skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        Texture logoTexture = new Texture(Gdx.files.internal("mankomania.png"));

        skin.getFont("font").getData().setScale(5, 5);
        Image logoImage = new Image(logoTexture);

        TextButton startButton = new TextButton("START", skin, "default");
        TextButton exitButton = new TextButton("EXIT GAME", skin, "default");
        startButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 400f);
        exitButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 400f);
        startButton.addListener(startListener());
        exitButton.addListener(exitListener());

        stage.addActor(buildTable(startButton, exitButton, logoImage));
    }

    private Table buildTable(TextButton startButton, TextButton exitButton, Image logoImage) {
        Table tab = new Table();
        tab.setFillParent(true);
        tab.setWidth(stage.getWidth());
        tab.align(Align.center | Align.top);
        tab.padTop(Gdx.graphics.getHeight() / 20f);
        tab.add(logoImage).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 4f);
        tab.row();
        tab.add(startButton).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(Gdx.graphics.getHeight() / 5f);
        tab.row();
        tab.add(exitButton).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(Gdx.graphics.getHeight() / 5f);
        tab.row();
        return tab;
    }

    private ClickListener startListener() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                MankomaniaGame.getInstance().disposeCurrentScreen();
                MankomaniaGame.getInstance().setScreen(new LobbyScreen());
            }
        };
    }

    private ClickListener exitListener() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                Gdx.app.exit();
            }
        };
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        MankomaniaGame.renderMenu(stage, batch, delta, background);
    }

    @Override
    public void dispose() {
        inputMultiplexer.removeProcessor(stage);
        stage.dispose();
    }

}
