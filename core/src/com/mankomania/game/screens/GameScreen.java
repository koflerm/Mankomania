package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {
    private final Texture gameBoard;
    private final Stage stage;
    private final SpriteBatch batch;
    //private final Texture boxTexture;
    private final Skin skin;
    private final float boxWidth;
    private final float boxHeight;

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
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.9f, 0.9f, 0.9f, 1);
        stage.act(delta);
        drawGameBoard();
        drawPlayerInformation();
        stage.draw();
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
        drawPlayerMetadata(Gdx.graphics.getWidth() / 2 - (boxWidth / 2), 100000);
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
