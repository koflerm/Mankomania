package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {
    private final Texture gameBoard;
    private final Stage stage;
    private final SpriteBatch batch;
    private final Skin skin;
    private final float boxWidth;
    private final float boxHeight;
    private Dialog turnDialog;

    private boolean turnDialogIsShown;
    private boolean turnDialogNeeded;
    private String turnDialogPlayerName;
    private String turnDialogPlayerMoney;
    private boolean turnDialogIsCurrentPlayer;

    private static final float BOARD_SIZING_FACTOR = 1f;
    private static final float BOARD_PLAYER_BOX_WIDTH_FACTOR = 4.5f;
    private static final float BOARD_PLAYER_BOX_HEIGHT_FACTOR = 4f;
    private static final float BOARD_PLAYER_MONEY_FACTOR = 4.5f;

    public GameScreen() {
        gameBoard = new Texture("board.jpg");
        stage = new Stage();
        batch = new SpriteBatch();
        //boxTexture = new Texture("raw/progress-bar.png");
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        boxWidth = calcWidthFactor(BOARD_PLAYER_BOX_WIDTH_FACTOR);
        boxHeight = calcHeightFactor(BOARD_PLAYER_BOX_HEIGHT_FACTOR);
        turnDialogIsShown = false;
        turnDialog = new Dialog("INFO", skin, "alt") {};
        turnDialogNeeded = false;

        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        ScreenUtils.clear(0.9f, 0.9f, 0.9f, 1);
        drawGameBoard();
        drawPlayerInformation();
        if (!turnDialogIsShown && turnDialogNeeded) {
            drawTurnDialog();
            turnDialogIsShown = true;
        }
        stage.draw();
    }

    public void showTurnDialog(String playerName, String playerMoney, boolean isCurrentPlayer) {
        this.turnDialogNeeded = true;
        this.turnDialogPlayerName = playerName;
        this.turnDialogPlayerMoney = playerMoney;
        this.turnDialogIsCurrentPlayer = isCurrentPlayer;
    }

    public void hideTurnDialog() {
        this.turnDialogNeeded = false;
        this.turnDialog.hide();
    }

    private void drawGameBoard() {
        batch.begin();
        batch.draw(
                gameBoard,
                calcBoardPosition(Gdx.graphics.getWidth()),
                calcBoardPosition(Gdx.graphics.getHeight()),
                calcBoardSize(),
                calcBoardSize()
        );
        batch.end();
    }

    private void drawPlayerInformation() {
        batch.begin();
        drawPlayerBox(0, 0, "P1", Color.BLACK);
        drawPlayerBox(0, Gdx.graphics.getHeight() - boxHeight, "P2", Color.GOLDENROD);
        drawPlayerBox(Gdx.graphics.getWidth() - boxWidth, Gdx.graphics.getHeight() - boxHeight, "P3", Color.BLUE);
        drawPlayerBox(Gdx.graphics.getWidth() - boxWidth, 0, "P4", Color.RED);
        //drawPlayerMetadata(Gdx.graphics.getWidth() / 2 - (boxWidth / 2), 100000);
        batch.end();
    }

    private float calcWidthFactor(float factor) {
        return Gdx.graphics.getWidth() / factor;
    }

    private float calcHeightFactor(float factor) {
        return Gdx.graphics.getHeight() / factor;
    }

    private void drawPlayerBox(float x, float y, String playerName, Color playerColor) {
        Texture boxTexture = new Texture(playerName + ".png");
        batch.draw(boxTexture, x, y, boxWidth, boxHeight);
    }

    private void drawPlayerMetadata(float x, int money) {
        float labelHeight = calcHeightFactor(BOARD_PLAYER_MONEY_FACTOR);
        Label label = new Label("$ " + money, skin, "title");
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
        Label l1 = new Label("Next Turn:", skin, "title");
        Label l2 = new Label(turnDialogPlayerName, skin, "title");
        Label l3 = new Label("Current money: $" + turnDialogPlayerMoney, skin, "title");

        tab.add(l1).row();
        tab.add(l2).row();
        tab.add(l3).row();
        tab.pad(20);

        if (turnDialogIsCurrentPlayer) {
            TextButton b1 = new TextButton("START TURN", skin, "default");
            b1.setTransform(true);
            turnDialog.button(b1, true).padBottom(20);
        } else {
            Label l4 = new Label("Waiting for player...", skin, "default");
            tab.add(l4).row();
        }

        turnDialog.getContentTable().add(tab);
        turnDialog.setScale(scale);
        turnDialog.show(stage);
        turnDialog.setPosition(Gdx.graphics.getWidth() / 2 - ((turnDialog.getWidth() * scale) / 2), Gdx.graphics.getHeight() / 2 - ((turnDialog.getHeight() * scale) / 2));
    }

    private float calcBoardPosition(int base) {
        return base / 2 - (Gdx.graphics.getHeight() / BOARD_SIZING_FACTOR / 2);
    }

    private float calcBoardSize() {
        return Gdx.graphics.getHeight() / BOARD_SIZING_FACTOR;
    }

    @Override
    public void dispose() {
        this.dispose();
    }
}
