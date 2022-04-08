package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import sun.awt.image.GifImageDecoder;

public class LoadingScreen extends ScreenAdapter {
    private final Stage stage;
    private Animation<Texture> loadingAnimation;
    private final SpriteBatch batch;
    private float elapsedTime;

    public LoadingScreen() {
        stage = new Stage();
        batch = new SpriteBatch();
        loadingAnimation = createLoadingAnimation();
        elapsedTime = 0;

        Gdx.input.setInputProcessor(stage);
    }

    private Animation createLoadingAnimation() {
        Texture[] frames = new Texture[75];

        for (int i = 0; i < 75; i++) {
            if (i < 10) {
                frames[i] = new Texture("loading-icon/frame_0" + i + "_delay-0.04s.gif");
            } else {
                frames[i] = new Texture("loading-icon/frame_" + i + "_delay-0.04s.gif");
            }
        }

        return new Animation(1f/25f, frames);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        System.out.println(elapsedTime);
        ScreenUtils.clear(1, 1, 1, 1);
        elapsedTime += delta;
        stage.act(delta);
        batch.begin();
        batch.draw(loadingAnimation.getKeyFrame(elapsedTime, true),Gdx.graphics.getWidth() / 2f - (Gdx.graphics.getWidth() / 5f / 2),Gdx.graphics.getHeight() / 2f - (Gdx.graphics.getHeight() / 5f / 2), Gdx.graphics.getWidth() / 5f, Gdx.graphics.getHeight() / 5f);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
