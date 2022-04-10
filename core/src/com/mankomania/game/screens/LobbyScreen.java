package com.mankomania.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.ScreenUtils;

public class LobbyScreen extends ScreenAdapter {
    private final Stage stage;
    private final Texture background;
    private final SpriteBatch batch;
    private final Table tab;


    public LobbyScreen(){
        stage = new Stage();
        batch = new SpriteBatch();
        background = new Texture("background.jpg");

        tab = new Table();
        tab.setFillParent(true);
        tab.setWidth(stage.getWidth());
        tab.align(Align.center | Align.top);

        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void show(){
        Skin skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        skin.getFont("font").getData().setScale(5, 5);

        TextButton backButton = new TextButton("BACK", skin, "default");
        backButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 400f);
        backButton.addListener(backListener());

        TextButton startButton = new TextButton("START", skin, "default");
        startButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 400f);
        startButton.addListener(startListener());

        Texture lobbyText = new Texture(Gdx.files.internal("text-field-lobby.png"));
        Image lobbyImage = new Image(lobbyText);

        Texture lobbyBackgroundTexture = new Texture(Gdx.files.internal("lobby-background.png"));
        Image lobbyBackground = new Image(lobbyBackgroundTexture);

        tab.add(lobbyImage).size(Gdx.graphics.getWidth() / 4f,Gdx.graphics.getHeight() / 5f).padTop(50).colspan(2).center();
        tab.row();

        tab.add(lobbyBackground).width(0.7f * Gdx.graphics.getWidth()).height(0.5f * Gdx.graphics.getHeight()).colspan(2).padTop(50).center();
        tab.row();

        tab.add(backButton).expand().bottom().left().padLeft(20).padBottom(20).width(Gdx.graphics.getWidth() / 5f).height(Gdx.graphics.getHeight() / 6f);
        tab.add(startButton).expand().bottom().right().padRight(20).padBottom(20).width(Gdx.graphics.getWidth() / 5f).height(Gdx.graphics.getHeight() / 6f);

        stage.addActor(tab);
    }


    public ClickListener backListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new StartScreen());
            }
        };
    }

    public ClickListener startListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
               //Gamescreen
            }
        };
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(1, 1, 1, 1);
        stage.act(delta);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
