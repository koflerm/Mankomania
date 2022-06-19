package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import diceLogic.DiceAnimation;


import com.mankomania.game.connections.ConStock;
import com.mankomania.game.connections.Connection;
import com.mankomania.game.MankomaniaGame;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import boardLogic.Board;
import fieldLogic.Field;
import fieldLogic.FieldAction;
import io.socket.emitter.Emitter;
import playerLogic.Player;
import shareLogic.Share;

public class GameScreen extends ScreenAdapter {
    private final Texture gameBoard;
    private final Stage stage;
    private final Skin skin;
    private final float boxWidth;
    private final float boxHeight;
    private final Dialog turnDialog;
    private final Dialog winnerDialog;
    private final Dialog intersectionDialog;
    private boolean triggerTurnDialog;

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
    private static final float DURATION = 3;
    private static final float FIELD_ACTION_DURATION = 6;

    private DiceAnimation diceAnimation;
    private float elapsed;
    private float elapsedFieldAction;

    private boolean intersectionDialogNeeded;
    private boolean intersectionDialogIsShown;
    private boolean moveToIntersection;
    private boolean intersectionDecided;
    private boolean gotWinner;
    private Player winningPlayer;
    private boolean winnerDialogShown;

    private Player movingPlayer;
    private int movingPlayerTargetSteps;
    private int movingPlayerCurrentSteps;
    private float movingElapsed;
    private float winnerElapsed;
    private List<Player> players;

    private FieldAction fieldAction;

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
        turnDialog = new Dialog("", skin, "alt") {
        };
        winnerDialog = new Dialog("", skin, "alt") {
        };
        turnDialogNeeded = false;
        diceAnimation = new DiceAnimation();
        fieldAction = new FieldAction();
        elapsed = 0;
        elapsedFieldAction = 0;
        intersectionDialog = new Dialog("", skin, "alt") {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    hideIntersectionDialog();
                    moveToIntersection = false;
                } else {
                    hideIntersectionDialog();
                    moveToIntersection = true;
                }
            }
        };

        intersectionDialogNeeded = false;
        intersectionDialogIsShown = false;
        moveToIntersection = false;
        intersectionDecided = false;
        triggerTurnDialog = false;
        gotWinner = false;
        winnerDialogShown = false;

        movingPlayerCurrentSteps = 0;
        movingPlayerTargetSteps = 0;
        movingElapsed = 0;

        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }

        players = MankomaniaGame.getInstance().getBoard().getPlayers();
        for (int i = 0; i < players.size(); i++) {
            stage.addActor(players.get(i));
        }

        /**
         * Choose stocks
         */

        ConStock cStock = new ConStock(StockSelectionScreen.getShortCircuitCount(), StockSelectionScreen.getDryOilCount(), StockSelectionScreen.getHardSteelCount());

        Connection.emitStocks(cStock);

        Connection.updateMyPlayerPosition(updateMyPosition);

        Connection.updateNextTurn(nextTurnListener);

        Connection.updateDice(updateDice);

        Connection.startRound(startRoundListener);

        Connection.roleHighestDice(highestDiceListener);

        Connection.roleHighestDiceAgain(roleAgain);

        Connection.getMoneyUpdate(getMoneyUpdateListener);

        Connection.loseMoneyUpdate(loseMoneyUpdateListener);

        Connection.collision(collisionListener);

        Connection.stockMinigameUpdate(stockUpdateListener);

        Connection.auctionMinigameUpdate(auctionUpdateListener);

        Connection.winnerUpdate(winnerListener);
    }


    @Override
    public void render(float delta) {
        stage.getBatch().begin();
        ScreenUtils.clear(0.9f, 0.9f, 0.9f, 1);
        Board board = MankomaniaGame.getInstance().getBoard();
        renderMovement(delta, board);
        renderDices(delta, board);
        drawGameBoard(board);
        drawPlayerInformation();
        stage.getBatch().end();

        /**
         * Update all Players
         */

        if (Connection.isUpdate()) {
            ArrayList<Player> players = Connection.convertConPlayersToPlayersUpdate();

            for (Player p : players) {
                for (Player gp : MankomaniaGame.getInstance().getBoard().getPlayers()) {
                    if (p.getPlayerSocketID().equals(gp.getPlayerSocketID())) {
                        int hardSteel = 0;
                        int shortCircuit = 0;
                        int dryOil = 0;

                        for (Share s : Share.values()) {
                            if (s.getName().equals(Share.HARD_STEEL_PLC.getName()))
                                hardSteel = p.getAmountOfShare(s);
                            else if (s.getName().equals(Share.SHORT_CIRCUIT_PLC.getName()))
                                shortCircuit = p.getAmountOfShare(s);
                            else if (s.getName().equals(Share.DRY_OIL_PLC.getName()))
                                dryOil = p.getAmountOfShare(s);
                        }

                        gp.setShares(hardSteel, shortCircuit, dryOil);
                    }
                }
            }

            Connection.setUpdate(false);

        }

        if (MankomaniaGame.getInstance().getBoard().getCurrentPlayer() != null && triggerTurnDialog) {
            showTurnDialog(MankomaniaGame.getInstance().getBoard().getCurrentPlayer(), Connection.isYourTurn());
            triggerTurnDialog = false;
        }

        if (!gotWinner) {
            checkForWinner();
        }

        renderDialogs();
        renderFieldActionDialog(delta);
        renderWinnerDialog(delta);

        stage.act(delta);
        stage.draw();
    }

    private void renderWinnerDialog(float delta) {
        if (gotWinner && !winnerDialogShown) {
            drawWinnerDialog();
            winnerDialogShown = true;
        } else if (gotWinner) {
            if (winnerElapsed >= DURATION) {
                this.winnerDialog.remove();
                this.winnerDialog.clear();
                winnerElapsed = 0;
                gotWinner = false;
                winningPlayer = null;
                MankomaniaGame.getInstance().disposeCurrentScreen();
                MankomaniaGame.getInstance().setScreen(new StartScreen());
                inputMultiplexer.removeProcessor(stage);
            } else {
                winnerElapsed += delta;
            }
        }
    }

    private void drawWinnerDialog() {
        float scale = Gdx.graphics.getWidth() / 1000f;

        Table tab = new Table();
        tab.align(Align.center);
        Label nextTurnLabel = new Label("And the winner is:", skin, BOARD_TEXT_STYLE);
        Label playerNameLabel = new Label("P" + winningPlayer.getPlayerIndex(), skin, BOARD_TEXT_STYLE);

        tab.add(nextTurnLabel).row();
        tab.add(playerNameLabel).row();
        tab.pad(20);

        winnerDialog.getContentTable().add(tab);
        winnerDialog.setScale(scale);
        winnerDialog.show(stage);

        float dialogXPosition = (Gdx.graphics.getWidth() / 2f) - ((winnerDialog.getWidth() * scale) / 2f);
        float dialogYPosition = (Gdx.graphics.getHeight() / 2f) - ((winnerDialog.getHeight() * scale) / 2f);

        winnerDialog.setPosition(dialogXPosition, dialogYPosition);
    }

    private void renderMovement(float delta, Board board) {
        if (movingPlayer != null && !intersectionDialogIsShown) {
            if (movingElapsed >= 0.5) {
                if (movingPlayerCurrentSteps < movingPlayerTargetSteps) {
                    movePlayerForward();
                } else {
                    movingPlayer = null;
                    movingPlayerCurrentSteps = 0;
                    movingPlayerTargetSteps = 0;

                    int currentFieldIndex = board.getCurrentPlayer().getCurrentPosition().getFieldIndex();
                    if (currentFieldIndex == 23) {
                        MankomaniaGame.getInstance().saveScreen();
                        MankomaniaGame.getInstance().setScreen(new StockScreen());

                    }else if(currentFieldIndex == 42) {
                        MankomaniaGame.getInstance().saveScreen();
                        MankomaniaGame.getInstance().setScreen(new AuctionScreen());
                    } else {
                        fieldAction.drawFieldActionDialog(stage, board.getCurrentPlayer());
                    }
                }
                movingElapsed = 0;
            } else {
                movingElapsed += delta;
            }
        }
    }

    private void renderDices(float delta, Board board) {
        if (diceAnimation.getDiceShown() && elapsed < DURATION)
            elapsed += delta;
        else if (diceAnimation.getDiceShown() && elapsed >= DURATION) {
            diceAnimation.removeDice();
            diceAnimation.setDiceShown(false);
            elapsed = 0;
            movePlayer(diceAnimation.getDiceSum(), MankomaniaGame.getInstance().getBoard().getCurrentPlayer());
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

    private boolean checkForWinner() {
        for (Player p : players) {
            if (p.getMoney() <= 0 && p.getPlayerSocketID().equals(Connection.getCs().id())) {
                Connection.emitWinner();
                gotWinner = true;
                winningPlayer = p;
                return true;
            }
        }
        return false;
    }

    private void renderFieldActionDialog(float delta) {
        if (elapsedFieldAction >= FIELD_ACTION_DURATION && fieldAction.getFieldActionDialogIsShown()) {
            elapsedFieldAction = 0;
            fieldAction.removeFieldActionDialog();
            fieldAction.setFieldActionDialogIsShown(false);

            Player currentPlayer = MankomaniaGame.getInstance().getBoard().getCurrentPlayer();

            Field f = MankomaniaGame.getInstance().getBoard().getCurrentPlayer().getCurrentPosition();

            System.out.println("Det field");
            Connection.determineFieldAction(f, MankomaniaGame.getInstance().getBoard().getCurrentPlayer());
            if (checkForWinner())
                return;

            List<String> playerCollision = new ArrayList<>();

            for (Player p : players) {
                if (!(p.getPlayerSocketID().equals(currentPlayer.getPlayerSocketID())) && currentPlayer.getCurrentPosition().equals(p.getCurrentPosition())) {
                    playerCollision.add(p.getPlayerSocketID());
                }
            }
            if (playerCollision.size() > 0) {
                Connection.collisionEmit(playerCollision.toArray(new String[0]));
            }

            int nextPlayerID = 1;

            if (currentPlayer.getPlayerIndex() != 4)
                nextPlayerID = currentPlayer.getPlayerIndex() + 1;

            Player nextPlayer = MankomaniaGame.getInstance().getBoard().getPlayerByIndex(nextPlayerID);
            Connection.setCurrentPlayer(nextPlayer);
            MankomaniaGame.getInstance().getBoard().setCurrentPlayer(nextPlayer);
            triggerTurnDialog = true;

            Connection.setYourTurn(false);
            Connection.emitNextTurn();
        } else if (fieldAction.getFieldActionDialogIsShown())
            elapsedFieldAction += delta;
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
            movingPlayerCurrentSteps++;
            Connection.emitPosition(movingPlayer.getCurrentPosition());
        } else {
            movingPlayer.moveForward(false);
            movingPlayerCurrentSteps++;
            Connection.emitPosition(movingPlayer.getCurrentPosition());
            if (intersectionDecided)
                intersectionDecided = false;
        }
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
        this.turnDialog.getContentTable().clear();
        this.turnDialog.getButtonTable().clear();
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
        drawPlayerBox(0, 0, "P2", p2Card);
        drawPlayerBox(0, Gdx.graphics.getHeight() - boxHeight, "P3", p3Card);
        drawPlayerBox(Gdx.graphics.getWidth() - boxWidth, Gdx.graphics.getHeight() - boxHeight, "P4", p4Card);
        drawPlayerBox(Gdx.graphics.getWidth() - boxWidth, 0, "P1", p1Card);

        if (MankomaniaGame.getInstance().getBoard().getPlayers() != null) {
            for (Player p : MankomaniaGame.getInstance().getBoard().getPlayers()) {
                if (p.getPlayerSocketID().equals(Connection.getCs().id())) {
                    drawPlayerMetadata(p.getMoney());
                }
            }
        } else {
            drawPlayerMetadata(0);
        }
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
        for(Actor a : stage.getActors()){
            if(a.getClass() == Label.class){
                a.addAction(Actions.removeActor());
            }
        }
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


    public ClickListener startTurnListener() {
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
    }

    /**
     * Listener for "RoleHighestDice"
     * Set roleDice to true
     */

    Emitter.Listener highestDiceListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Connection.convertJsonToPlayer("" + args[0]);

            Connection.setRoleHighestDice(true);

            Connection.setUpdate(true);

            /**
             * Roll the dices
             */

            SecureRandom rand = new SecureRandom();

            int dice1 = rand.nextInt((7 - 0 + 1) + 0);

            int dice2 = rand.nextInt((7 - 0 + 1) + 0);

            Connection.emitHighestDice(dice1, dice2);

            /**
             * DEBUG MODE ON
             */
            //Connection.emitHighestDice(6, 6);


        }
    };

    /**
     * Listener for "StartRound"
     * Checks if Client can start
     */

    Emitter.Listener startRoundListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Connection.convertJsonToPlayer("" + args[0]);

            if (args[1].toString().equals(Connection.getCs().id())) {
                //set Turn after the turn on false
                Connection.setYourTurn(true);
            } else {
                Connection.setYourTurn(false);
            }


            for (Player p : players) {
                if (p.getPlayerSocketID().equals(args[1].toString())) {
                    MankomaniaGame.getInstance().getBoard().setCurrentPlayer(p);
                    Connection.setCurrentPlayer(p);
                    triggerTurnDialog = true;
                }
            }

            Connection.setUpdate(true);

        }
    };

    /**
     * Gets next turn, when the current player has finished
     */

    Emitter.Listener nextTurnListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String nextPlayerSocket = args[0].toString();
            List<Player> pList = MankomaniaGame.getInstance().getBoard().getPlayers();

            for (Player p : pList) {
                if (p.getPlayerSocketID().equals(nextPlayerSocket)) {
                    MankomaniaGame.getInstance().getBoard().setCurrentPlayer(p);
                    Connection.setCurrentPlayer(p);
                    triggerTurnDialog = true;
                    if (p.getPlayerSocketID().equals(Connection.getCs().id())) {
                        Connection.setYourTurn(true);
                        Gdx.input.vibrate(new long[]{0, 90, 90, 90}, -1);
                    }
                }
            }
        }
    };

    /**
     * Role Highest Dice Again Listener
     */

    Emitter.Listener roleAgain = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            String[] winners = args[1].toString().split(",");

            Connection.setWinners(winners);

            for (int i = 0; i < winners.length; i++) {
                if (winners[i].equals(Connection.getCs().id())) {
                    Connection.setRoleHighestDice(true);
                }
            }
        }
    };

    /**
     * Update dices after roll listener
     */

    Emitter.Listener updateDice = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            String socketID = args[0].toString();

            String[] dices = args[1].toString().split(",");

            int dice1 = Integer.parseInt(dices[0].substring(1));
            int dice2 = Integer.parseInt(dices[1].substring(0, 1));

            int totalDice = dice1 + dice2;

            MankomaniaGame.getInstance().getBoard().getCurrentPlayer().setDices(totalDice);
        }
    };


    /**
     * Update the position of the moving player
     */

    Emitter.Listener updateMyPosition = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            System.out.println("Update my own position (received from server)");
            GameScreen gs = (GameScreen) MankomaniaGame.getInstance().getScreen();
            gs.hideTurnDialog();

            int fieldNumber = Integer.parseInt(args[1].toString());

            Field nextField = MankomaniaGame.getInstance().getBoard().getFieldByIndex(fieldNumber);

            Player currentPlayer = MankomaniaGame.getInstance().getBoard().getCurrentPlayer();
            if (currentPlayer.getCurrentPosition().getOptionalNextField() != null)
                currentPlayer.moveForward(nextField.getFieldIndex() == currentPlayer.getCurrentPosition().getOptionalNextField().getFieldIndex());
            else
                currentPlayer.moveForward(false);
        }
    };

    /**
     * Get money listener
     */

    Emitter.Listener getMoneyUpdateListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            System.out.println("Receive getMoney");

            String socketID = args[0].toString();

            int amount = Integer.parseInt(args[1].toString());

            List<Player> pl = MankomaniaGame.getInstance().getBoard().getPlayers();

            for (Player p : pl) {
                if (p.getPlayerSocketID().equals(socketID)) {
                    p.addMoney(amount);
                }
            }
        }
    };


    /**
     * Lose money listener
     */

    Emitter.Listener loseMoneyUpdateListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            System.out.println("Receive loseMoney");

            String socketID = args[0].toString();

            int amount = Integer.parseInt(args[1].toString());

            List<Player> pl = MankomaniaGame.getInstance().getBoard().getPlayers();

            for (Player p : pl) {
                if (p.getPlayerSocketID().equals(socketID)) {
                    p.loseMoney(amount);
                }
            }
        }
    };

    /**
     * Listener for players on the same field (collision)
     */

    Emitter.Listener collisionListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            System.out.println("Received a collision");

            System.out.println(args[1].toString());

            String[] players = args[1].toString().split(",");

            players[0] = players[0].substring(1);

            players[players.length - 1] = players[players.length - 1].substring(0, players[players.length - 1].length() - 1);

            int decrease = players.length * 10000;

            MankomaniaGame.getInstance().getBoard().getCurrentPlayer().loseMoney(decrease);

            List<Player> pl = MankomaniaGame.getInstance().getBoard().getPlayers();

            for (Player p : pl) {
                for (String id : players) {
                    if (p.getPlayerSocketID().equals(id)) {
                        p.addMoney(10000);
                    }
                }
            }
        }
    };

    /**
     * StockMinigame Update Listener
     */

    Emitter.Listener stockUpdateListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            System.out.println("Receive Stock Minigame Update");

            String minigameActPlayer = args[0].toString();

            System.out.println(args[1]);

            String[] params = args[1].toString().split(",");


            String stock = params[0].substring(12, params[0].length() - 1);

            String blackAsString = params[1].substring(7, params[1].length() - 1);

            boolean black;

            if (blackAsString.equals("true")) {
                black = true;
            } else {
                black = false;
            }


            List<Player> pl = MankomaniaGame.getInstance().getBoard().getPlayers();

            Share s;

            if (stock.equals("Hardsteel")) {
                s = Share.HARD_STEEL_PLC;
            } else if (stock.equals("Shortcircuit")) {
                s = Share.SHORT_CIRCUIT_PLC;
            } else {
                s = Share.DRY_OIL_PLC;
            }

            for (Player p : pl) {
                if (p.getPlayerSocketID().equals(Connection.getCs().id())) {
                    int amountOfStock = p.getAmountOfShare(s);
                    System.out.println(amountOfStock);
                    if (amountOfStock > 0) {
                        if (black) {
                            p.loseMoney(20000 * amountOfStock);
                        } else {
                            p.addMoney(20000 * amountOfStock);
                        }
                    }
                }
            }
        }
    };


    /**
     * Auction Minigame Update Listener
     */

    Emitter.Listener auctionUpdateListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            String currentPlayerSocketID = args[0].toString();

            System.out.println("Auction Update Listener: " + args[1].toString());


            String[] splitAuctionObject = args[1].toString().split("moneyToSet\":");

            String[] newAuctionObject = splitAuctionObject[1].split(",\"multiplicator");

            String moneyToSetAsString = newAuctionObject[0];

            int moneyToSet = Integer.parseInt(moneyToSetAsString);

            System.out.println("moneyToSet: " + moneyToSet);

            for (Player p : players) {
                if (p.getPlayerSocketID().equals(currentPlayerSocketID)) {
                    p.setMoney(moneyToSet);
                    System.out.println("new money: " + p.getMoney());
                }
            }
        }
    };


    /**
     * Listener if one player wins
     */

    Emitter.Listener winnerListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            String winnerSocket = args[0].toString();

            for (Player p : players) {
                if (p.getPlayerSocketID().equals(winnerSocket)) {
                    gotWinner = true;
                    winningPlayer = p;
                    System.out.println("The winner is: " + p.getPlayerIndex());
                }
            }

        }
    };

}
