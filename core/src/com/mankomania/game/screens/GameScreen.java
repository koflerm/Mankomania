package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mankomania.game.MankomaniaGame;

public class GameScreen extends ScreenAdapter {
    private final Texture gameBoard;
    private final Stage stage;
    private final SpriteBatch batch;

    private static final float BOARD_SIZING_FACTOR = 1.5f;

    public GameScreen() {
        gameBoard = new Texture("board.jpg");
        stage = new Stage();
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(1, 1, 1, 1);
        stage.act(delta);
        batch.begin();
        drawGameBoard();
        drawPlayerInformation();
        batch.end();
        stage.draw();
    }

    private void drawGameBoard() {
        batch.draw(
                gameBoard,
                calcBoardPosition(Gdx.graphics.getWidth()),
                calcBoardPosition(Gdx.graphics.getHeight()),
                calcBoardSize(),
                calcBoardSize()
        );
    }

    private void drawPlayerInformation() {
        // TODO
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
