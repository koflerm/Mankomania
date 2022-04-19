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

public class LoadingScreen extends ScreenAdapter {
    private Stage stage;
    private Animation<Texture> loadingAnimation;
    private SpriteBatch batch;
    private float elapsedTime;
    private Screen nextScreen;
    private final float duration;

    public LoadingScreen(Screen nextScreen) {
        duration = 3;
        initVariables(nextScreen);
    }

    public LoadingScreen(Screen nextScreen, int duration) {
        this.duration = duration;
        initVariables(nextScreen);
    }

    private void initVariables(Screen nextScreen) {
        stage = new Stage();
        batch = new SpriteBatch();
        loadingAnimation = createLoadingAnimation();
        elapsedTime = 0;
        this.nextScreen = nextScreen;
    }

    private Animation<Texture> createLoadingAnimation() {
        Texture[] frames = new Texture[75];

        for (int i = 0; i < 75; i++) {
            if (i < 10) {
                frames[i] = new Texture("loading-icon/frame_0" + i + "_delay-0.04s.gif");
            } else {
                frames[i] = new Texture("loading-icon/frame_" + i + "_delay-0.04s.gif");
            }
        }

        return new Animation<>(1f / 80f, frames);
    }

    @Override
    public void render(float delta) {
        if (elapsedTime >= duration) {
            MankomaniaGame.getInstance().setScreen(nextScreen);
            this.dispose();
        } else {
            super.render(delta);
            ScreenUtils.clear(1, 1, 1, 1);
            elapsedTime += delta;
            stage.act(delta);
            batch.begin();
            batch.draw(loadingAnimation.getKeyFrame(elapsedTime, true),Gdx.graphics.getWidth() / 2f - (Gdx.graphics.getWidth() / 5f / 2),Gdx.graphics.getHeight() / 2f - (Gdx.graphics.getHeight() / 5f / 2), Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 5f);
            batch.end();
            stage.draw();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
