package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import diceLogic.DiceAnimation;

import com.mankomania.game.Connection;
import com.mankomania.game.MankomaniaGame;

import java.util.ArrayList;
import java.util.List;

import boardLogic.Board;
import io.socket.emitter.Emitter;
import playerLogic.Player;

public class GameScreen extends ScreenAdapter {
    private final Texture gameBoard;
    private final Stage stage;
    private final Skin skin;
    private final float boxWidth;
    private final float boxHeight;
    private final Dialog turnDialog;
    private final Dialog intersectionDialog;

    private InputMultiplexer inputMultiplexer;
    private Texture p1Card;
    private Texture p2Card;
    private Texture p3Card;
    private Texture p4Card;
    private Texture turnBox;

    private boolean turnDialogIsShown;
    private boolean turnDialogNeeded;
    private String turnDialogPlayerName;
    private String turnDialogPlayerMoney;
    private boolean turnDialogIsCurrentPlayer;

    private static final float BOARD_PLAYER_BOX_WIDTH_FACTOR = 4.5f;
    private static final float BOARD_PLAYER_BOX_HEIGHT_FACTOR = 4f;
    private static final float BOARD_PLAYER_MONEY_FACTOR = 4.5f;
    private static final String BOARD_TEXT_STYLE = "title";
    private static final String BOARD_MODAL_BUTTON_STYLE = "default";
    private static final float duration = 3;

    private DiceAnimation diceAnimation;
    private float elapsed;

    private boolean intersectionDialogNeeded;
    private boolean intersectionDialogIsShown;
    private boolean moveToIntersection;
    private boolean intersectionDecided;

    private Player movingPlayer;
    private int movingPlayerTargetSteps;
    private int movingPlayerCurrentSteps;
    private float movingElapsed;

    public GameScreen() {
        stage = new Stage();
        gameBoard = new Texture("board.jpg");
        p1Card = new Texture("P1.png");
        p2Card = new Texture("P2.png");
        p3Card = new Texture("P3.png");
        p4Card = new Texture("P4.png");
        turnBox = new Texture("turnBox.png");
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        boxWidth = calcWidthFactor(BOARD_PLAYER_BOX_WIDTH_FACTOR);
        boxHeight = calcHeightFactor(BOARD_PLAYER_BOX_HEIGHT_FACTOR);
        turnDialogIsShown = false;
        turnDialog = new Dialog("INFO", skin, "alt") {};
        turnDialogNeeded = false;
        diceAnimation = new DiceAnimation();
        elapsed = 0;
        intersectionDialog = new Dialog("", skin, "alt") {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    hideIntersectionDialog();
                    moveToIntersection = false;
                }else{
                    hideIntersectionDialog();
                    moveToIntersection = true;
                }
            }
        };

        intersectionDialogNeeded = false;
        intersectionDialogIsShown = false;
        moveToIntersection = false;
        intersectionDecided = false;

        movingPlayerCurrentSteps = 0;
        movingPlayerTargetSteps = 0;
        movingElapsed = 0;

        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }

        List<Player> players = MankomaniaGame.getInstance().getBoard().getPlayers();
        for (int i = 0; i  < players.size(); i++) {
            stage.addActor(players.get(i));
        }

        showTurnDialog(players.get(0), true);

        /**
         * Debugging purpose
         */

        ConStock cStock = new ConStock(1, 1, 0);
        Connection.emitStocks(cStock);



        /**
         * Listener for "RoleHighestDice"
         * Set roleDice to true
         */

        Emitter.Listener highestDiceListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                System.out.println("Role the Highest Dice listene");

                Connection.convertJsonToPlayer("" + args[0]);

                Connection.setRoleHighestDice(true);

                Connection.setUpdate(true);

                //Nach Dice-Roll, RoleDice wieder auf false setzen

                /**
                 * Debug mode on:
                 */

                Connection.emitHighestDice(6, 6);

            }
        };

        /**
         * Listener for "StartRound"
         * Cheks if cLient can start
         */

        Emitter.Listener startRoundListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                System.out.println("StartRound Listener");

                Connection.convertJsonToPlayer("" + args[0]);

                System.out.println("Args1: " + args[1]);

                if(args[1].toString().equals(Connection.getCs().id())){
                    Connection.setYourTurn(true);

                    System.out.println("My turn");
                }

                //Danach wieder auf false setzen


                Connection.setUpdate(true);

            }
        };

        /**
         * Role Highest Dice Again Listener
         */

        Emitter.Listener roleAgain = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                //Connection.convertJsonToPlayer("" + args[0]);

                System.out.println("Args1 2.mal: " + args[1]);
            }
        };

        Connection.startRound(startRoundListener);

        Connection.roleHighestDice(highestDiceListener);

    }

    @Override
    public void render(float delta) {
        stage.getBatch().begin();
        ScreenUtils.clear(0.9f, 0.9f, 0.9f, 1);
        Board board = MankomaniaGame.getInstance().getBoard();
        renderMovement(delta);
        renderDices(delta, board);
        drawGameBoard(board);
        drawPlayerInformation();
        stage.getBatch().end();

        renderDialogs();
        stage.act(delta);
        stage.draw();

        /**
         * Update all Players
         */

        if(Connection.isUpdate() == true){

            ArrayList<Player> players = Connection.convertConPlayersToPlayersUpdate();
            MankomaniaGame.getInstance().getBoard().deleteAllPlayers();

            for(Player p : players){
                System.out.println("Update player " + p.getPlayerSocketID());
                MankomaniaGame.getInstance().getBoard().addPlayer(p);
            }

            Connection.setUpdate(false);

        }
    }

    private void renderMovement(float delta) {
        if (movingPlayer != null && !intersectionDialogIsShown) {
            if (movingElapsed >= 0.5) {
                if (movingPlayerCurrentSteps < movingPlayerTargetSteps) {
                    movePlayerForward();
                } else {
                    movingPlayer = null;
                    movingPlayerTargetSteps = 0;
                }
                movingElapsed = 0;
            } else {
                movingElapsed += delta;
            }
        }
    }

    private void renderDices(float delta, Board board) {
        if (elapsed >= duration){
            diceAnimation.removeDice();
            diceAnimation.setDiceShown(false);
            movePlayer(diceAnimation.getDiceSum(), board.getCurrentPlayer());
        }
        if(diceAnimation.getDiceShown()){
            elapsed+=delta;
        }
    }

    private void renderDialogs() {
        if (!turnDialogIsShown && turnDialogNeeded) {
            drawTurnDialog();
            turnDialogIsShown = true;
        }
        if (!intersectionDialogIsShown && intersectionDialogNeeded) {
            drawIntersectionDialog();
            intersectionDialogIsShown = true;
        }
    }

    public void movePlayer(int steps, Player player) {
        movingPlayer = player;
        movingPlayerTargetSteps = steps;
    }

    private void movePlayerForward() {
        if (movingPlayer.getCurrentPosition().isIntersection() && !intersectionDecided) {
            intersectionDialogNeeded = true;
        } else if (movingPlayer.getCurrentPosition().isIntersection() && intersectionDecided) {
            movingPlayer.moveForward(moveToIntersection);
        } else {
            movingPlayer.moveForward(false);
            if (intersectionDecided) {
                intersectionDecided = false;
            }
        }
        movingPlayerCurrentSteps++;
    }

    public void showTurnDialog(Player player, boolean isCurrentPlayer) {
        this.turnDialogNeeded = true;
        this.turnDialogPlayerName = "P" + player.getPlayerIndex();
        this.turnDialogPlayerMoney = "" + player.getMoney();
        this.turnDialogIsCurrentPlayer = isCurrentPlayer;
    }

    public void hideTurnDialog() {
        this.turnDialogNeeded = false;
        this.turnDialogIsShown = false;
        this.turnDialog.remove();
    }

    public void hideIntersectionDialog() {
        this.intersectionDecided = true;
        this.intersectionDialogNeeded = false;
        this.intersectionDialogIsShown = false;
        this.intersectionDialog.remove();
    }

    private void drawGameBoard(Board board) {
        stage.getBatch().draw(
                gameBoard,
                board.getX(),
                board.getY(),
                board.getLength(),
                board.getLength()
        );
    }

    private void drawPlayerInformation() {
        drawPlayerBox(0, 0, "P1", p1Card);
        drawPlayerBox(0, Gdx.graphics.getHeight() - boxHeight, "P2", p2Card);
        drawPlayerBox(Gdx.graphics.getWidth() - boxWidth, Gdx.graphics.getHeight() - boxHeight, "P3", p3Card);
        drawPlayerBox(Gdx.graphics.getWidth() - boxWidth, 0, "P4", p4Card);
        drawPlayerMetadata(100000);
    }

    private float calcWidthFactor(float factor) {
        return Gdx.graphics.getWidth() / factor;
    }

    private float calcHeightFactor(float factor) {
        return Gdx.graphics.getHeight() / factor;
    }

    private void drawPlayerBox(float x, float y, String playerName, Texture playerCard) {
        if (playerName.equals(turnDialogPlayerName)) {
            stage.getBatch().draw(turnBox, x, y, boxWidth, boxHeight);
        }
        stage.getBatch().draw(playerCard, x, y, boxWidth, boxHeight);
    }

    private void drawPlayerMetadata(int money) {
        float labelHeight = calcHeightFactor(BOARD_PLAYER_MONEY_FACTOR);
        Label label = new Label("$ " + money, skin, BOARD_TEXT_STYLE);
        label.setFontScale(labelHeight / 200f);
        label.setSize(Gdx.graphics.getWidth(), labelHeight);
        label.setColor(Color.GREEN);
        label.setPosition(0, 0);
        label.setAlignment(Align.center);
        stage.addActor(label);
    }

    private void drawTurnDialog() {
        float scale = Gdx.graphics.getWidth() / 1000f;

        Table tab = new Table();
        tab.align(Align.center);
        Label nextTurnLabel = new Label("Next Turn:", skin, BOARD_TEXT_STYLE);
        Label playerNameLabel = new Label(turnDialogPlayerName, skin, BOARD_TEXT_STYLE);
        Label moneyLabel = new Label("Current money: $" + turnDialogPlayerMoney, skin, BOARD_TEXT_STYLE);

        tab.add(nextTurnLabel).row();
        tab.add(playerNameLabel).row();
        tab.add(moneyLabel).row();
        tab.pad(20);

        if (turnDialogIsCurrentPlayer) {
            TextButton startTurnButton = new TextButton("START TURN", skin, BOARD_MODAL_BUTTON_STYLE);
            startTurnButton.setTransform(true);
            turnDialog.button(startTurnButton, true).padBottom(20);
            startTurnButton.addListener(startTurnListener());
        } else {
            Label l4 = new Label("Waiting for player...", skin, BOARD_MODAL_BUTTON_STYLE);
            tab.add(l4).row();
        }

        turnDialog.getContentTable().add(tab);
        turnDialog.setScale(scale);
        turnDialog.show(stage);

        float dialogXPosition = (Gdx.graphics.getWidth() / 2f) - ((turnDialog.getWidth() * scale) / 2f);
        float dialogYPosition = (Gdx.graphics.getHeight() / 2f) - ((turnDialog.getHeight() * scale) / 2f);

        turnDialog.setPosition(dialogXPosition, dialogYPosition);
    }

    private void drawIntersectionDialog() {
        float scale = Gdx.graphics.getWidth() / 1000f;

        Table tab = new Table();
        tab.align(Align.center);
        Label directionLabel = new Label("USE SHORTER PATH?", skin, BOARD_TEXT_STYLE);

        tab.add(directionLabel).row();
        tab.pad(20);

        TextButton leftButton = new TextButton("NO", skin, BOARD_MODAL_BUTTON_STYLE);
        TextButton rightButton = new TextButton("YES", skin, BOARD_MODAL_BUTTON_STYLE);
        leftButton.setTransform(true);
        rightButton.setTransform(true);
        if (intersectionDialog.getButtonTable().getChildren().size == 0) {
            intersectionDialog.button(leftButton, true).padBottom(20);
            intersectionDialog.button(rightButton, false).padBottom(20);
        }

        intersectionDialog.getContentTable().clear();
        intersectionDialog.getContentTable().add(tab);
        intersectionDialog.setScale(scale);
        intersectionDialog.show(stage);

        float dialogXPosition = (Gdx.graphics.getWidth() / 2f) - ((intersectionDialog.getWidth() * scale) / 2f);
        float dialogYPosition = (Gdx.graphics.getHeight() / 2f) - ((intersectionDialog.getHeight() * scale) / 2f);

        intersectionDialog.setPosition(dialogXPosition, dialogYPosition);
    }


    public ClickListener startTurnListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                hideTurnDialog();
                diceAnimation.showAndRollTheDice(stage);
            }
        };
    }

    @Override
    public void dispose() {
        inputMultiplexer.removeProcessor(stage);
        this.dispose();
    }
}
