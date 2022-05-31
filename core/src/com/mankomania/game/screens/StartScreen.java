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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mankomania.game.ConPlayer;
import com.mankomania.game.Connection;
import com.mankomania.game.MankomaniaGame;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import netscape.javascript.JSObject;
import playerLogic.Player;

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
        TextButton settingsButton = new TextButton("SETTINGS", skin, "default");
        startButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 400f);
        settingsButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 400f);
        startButton.addListener(startListener());

        stage.addActor(buildTable(startButton, settingsButton, logoImage));
    }

    private Table buildTable(TextButton startButton, TextButton settingsButton, Image logoImage) {
        Table tab = new Table();
        tab.setFillParent(true);
        tab.setWidth(stage.getWidth());
        tab.align(Align.center | Align.top);
        tab.padTop(Gdx.graphics.getHeight() / 20f);
        tab.add(logoImage).width(Gdx.graphics.getWidth() / 1.5f).height(Gdx.graphics.getHeight() / 4f);
        tab.row();
        tab.add(startButton).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(Gdx.graphics.getHeight() / 5f);
        tab.row();
        tab.add(settingsButton).padBottom(50).width(Gdx.graphics.getWidth() / 2f).height(Gdx.graphics.getHeight() / 5f);
        tab.row();
        return tab;
    }

    private ClickListener startListener() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                MankomaniaGame.getInstance().disposeCurrentScreen();
                MankomaniaGame.getInstance().setScreen(new LobbyScreen());

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

                        //System.out.println(args[1].toString());

                       // System.out.println("Str0: " + str[0]);
                       // System.out.println("Str1: " + str[1]);

                        Connection.setStart(true);

                        /**
                         * Split Player-Array in multiple Player Strings
                         */

                        String[] players = args[1].toString().split("},", 10);

                        for(int i = 0; i < players.length; i++) {

                            String temp = players[i];

                            String newString = "";

                            if(i == 0){
                                //Ersten 24 Zeichen entfernen
                                //} hinten hinzufügen

                                newString = temp.substring(24);
                                newString += "}";
                                players[i] = newString;
                            }

                            else if(i < (players.length - 1)){
                                //Ersten 23 Zeichen entfernen
                                //} hinten hinzufügen

                                newString = temp.substring(23);
                                newString += "}";

                                players[i] = newString;

                            }else if(i == (players.length - 1)){
                                //Ersten 23 Zeichen entfernen
                                //Hinten entfernen: }

                                newString = temp.substring(23);
                                newString = newString.substring(0, newString.length()-1);
                                players[i] = newString;
                            }

                           // System.out.println(players[i]);

                        }

                        System.out.println();
                        System.out.println();

                        /**
                         * Parser for Stocks
                         */

                        ArrayList<String> stockAsString = new ArrayList<String>();


                        for(int i = 0; i < players.length; i++){
                            String[] stock = players[i].split(":\\{");

                            String newStock = stock[1];

                            String newNewStock = "{";

                            newNewStock += newStock;

                            String finalStock = newNewStock.substring(0, newNewStock.length()-1);

                            stockAsString.add(finalStock);


                        }

                   /*     System.out.println("Stocks");
                        for(String s : stockAsString){
                            System.out.println(s);
                        }*/



                        /**
                         * Parse Player-Strings into Con-Players
                         */

                        String player1 = players[0];
                        String player2 = players[1];
                        String player3 = players[2];
                        String player4 = players[3];

                        ArrayList<String> allPlayersAsString = new ArrayList<String>();

                        allPlayersAsString.add(player1);
                        allPlayersAsString.add(player2);
                        allPlayersAsString.add(player3);
                        allPlayersAsString.add(player4);

                        ArrayList<ConPlayer> cp = new ArrayList<ConPlayer>();

                        for(String p : allPlayersAsString) {

                            JsonObject jsonObject = JsonParser.parseString(p).getAsJsonObject();

                            ConPlayer temp = new ConPlayer();

                            temp.setDice_2(jsonObject.get("dice_2").getAsInt());

                            temp.setDice_1(jsonObject.get("dice_1").getAsInt());

                            temp.setMoney(jsonObject.get("money").getAsInt());

                            temp.setYouTurn(jsonObject.get("yourTurn").getAsBoolean());

                            temp.setSocket(jsonObject.get("socket").getAsString());

                            temp.setPosition(jsonObject.get("position").getAsInt());

                            temp.setDice_Count(jsonObject.get("dice_Count").getAsInt());

                            temp.setPlayerIndex(jsonObject.get("playerIndex").getAsInt());

                            cp.add(temp);

                        }

                        /**
                         * Parse Stocks into ConPlayer Object
                         */

                        for(int i = 0; i < stockAsString.size(); i++){

                            JsonObject jsonStock = JsonParser.parseString(stockAsString.get(i)).getAsJsonObject();

                            cp.get(i).setHARD_STEEL_PLC(jsonStock.get("HardSteel_PLC").getAsInt());
                            cp.get(i).setSHORT_CIRCUIT_PLC(jsonStock.get("ShortCircuit_PLC").getAsInt());
                            cp.get(i).setDRY_OIL_PLC(jsonStock.get("DryOil_PLC").getAsInt());
                        }

                        for(ConPlayer out : cp){

                            System.out.println("ConPlayer: " + out.toString());
                        }
                    }
                };

                /**
                 * Call Connection Methods
                 */
                Connection.startGame(startGameListener);

                Connection.joinRoom(joinRoomListener);

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
