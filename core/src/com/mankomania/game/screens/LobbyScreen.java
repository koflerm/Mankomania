package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.Connection;
import com.mankomania.game.MankomaniaGame;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import playerLogic.Player;


public class LobbyScreen extends ScreenAdapter{
    private final Stage stage;
    private final Texture background;
    private final SpriteBatch batch;
    private final Table tab;
    private final Table innerTab;
    private InputMultiplexer inputMultiplexer;
    private String[] players;
    private final Skin skin;
    private Label label1;
    private Label label2;
    private Label label3;
    private Label label4;

    public LobbyScreen() {
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        skin.getFont("font").getData().setScale(5, 5);

        stage = new Stage();
        batch = new SpriteBatch();
        background = new Texture("background.jpg");

        tab = new Table();
        tab.setFillParent(true);
        tab.setWidth(stage.getWidth());
        tab.align(Align.center | Align.top);

        innerTab = new Table();
        innerTab.setWidth(stage.getWidth());
        innerTab.setHeight(stage.getHeight());
        innerTab.align(Align.center);

        label1 = new Label("",skin);
        label2 = new Label("",skin);
        label3 = new Label("",skin);
        label4 = new Label("",skin);

        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }

        initConnection();
    }

    public void initConnection() {
        /**
         * Create Socket Connection
         */
        Connection.createConnection();

        /**
         * Create playerJoinRoom Listener
         */

        Emitter.Listener joinRoomListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Connection.setLobbyID(args[0].toString());

                String temp = args[1].toString().substring(1, args[1].toString().length() - 1);

                String temp1 = temp.replaceAll("[\"]", "");

                Connection.setPlayers(temp1.split(","));

            }
        };


        /**
         * Create GAME_START Listener
         */
        Emitter.Listener startGameListener = new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                /**
                 * Convert args[1] into Player Object
                 */
                Connection.convertJsonToPlayer("" + args[1]);

                Connection.setStart(true);
            }
        };

        /**
         * Call Connection Methods
         */
        Connection.startGame(startGameListener);

        Connection.joinRoom(joinRoomListener);
    }

    @Override
    public void show() {
        TextButton backButton = new TextButton("BACK", skin, "default");
        backButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 400f);
        backButton.addListener(backListener());

        TextButton startButton = new TextButton("START", skin, "default");
        startButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 400f);
        startButton.addListener(startListener());

        Texture lobbyText = new Texture(Gdx.files.internal("text-field-lobby.png"));
        Image lobbyImage = new Image(lobbyText);

        Texture lobbyBackgroundTexture = new Texture(Gdx.files.internal("lobby-background.png"));
        TextureRegionDrawable lobbyBackground = new TextureRegionDrawable(lobbyBackgroundTexture);

        innerTab.setBackground(lobbyBackground);
        innerTab.add().expandY();
        innerTab.row();
        innerTab.add(label1).pad(20f).align(Align.center).row();
        innerTab.add(label2).pad(20f).align(Align.center).row();
        innerTab.add(label3).pad(20f).align(Align.center).row();
        innerTab.add(label4).pad(20f).align(Align.center).row();
        innerTab.add().expandY();

        tab.add(lobbyImage).size(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 5f).padTop(50).colspan(2).center();
        tab.row();
        tab.add(innerTab).colspan(2).expandY();
        tab.row();

        tab.add(backButton).expandX().left().padLeft(20).padBottom(20).width(Gdx.graphics.getWidth() / 5f).height(Gdx.graphics.getHeight() / 6f);
        tab.add(startButton).expandX().bottom().right().padRight(20).padBottom(20).width(Gdx.graphics.getWidth() / 5f).height(Gdx.graphics.getHeight() / 6f);

        stage.addActor(tab);
    }

    private void update(){
        players = Connection.getPlayers();

        label1.setText(players[0]);

        if(players.length == 2) {
            label2.setText(players[1]);
        }

        if(players.length == 3) {
            label2.setText(players[1]);
            label3.setText(players[2]);
        }

        if(players.length == 4) {
            label2.setText(players[1]);
            label3.setText(players[2]);
            label4.setText(players[3]);
        }
    }


    public ClickListener backListener() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                MankomaniaGame.getInstance().disposeCurrentScreen();
                MankomaniaGame.getInstance().setScreen(new StartScreen());

                //Comment for Debug purpose:
                Connection.closeConnection();

            }
        };
    }

    public ClickListener startListener() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {

                if(Connection.getStart()){
                    MankomaniaGame.getInstance().disposeCurrentScreen();
                    MankomaniaGame.getInstance().setScreen(new GameScreen());
                }

            }
        };
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        MankomaniaGame.renderMenu(stage, batch, delta, background);
        update();
        if(Connection.getStart() == true){
            ArrayList<Player> players = Connection.convertConPlayersToPlayersInitial();

            for(Player p : players){
                System.out.println("Adding player " + p.getPlayerSocketID());
                MankomaniaGame.getInstance().getBoard().addPlayer(p);
            }

            MankomaniaGame.getInstance().disposeCurrentScreen();
            MankomaniaGame.getInstance().setScreen(new GameScreen());
        }
    }

    public void doDispose(){
        MankomaniaGame.getInstance().disposeCurrentScreen();
        MankomaniaGame.getInstance().setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        inputMultiplexer.removeProcessor(stage);
        stage.dispose();
    }

}
