package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mankomania.game.MankomaniaGame;

import java.security.SecureRandom;

public class StockScreen extends ScreenAdapter {
    private final SecureRandom random;
    private final Stage stage;
    private final Texture background;
    private final RotateToAction rotate;
    private final SpriteBatch batch;
    private static final float duration = 8;
    private float elapsed;

    public StockScreen(){
        stage = new Stage();
        background = new Texture("background.jpg");
        batch = new SpriteBatch();
        random = new SecureRandom();
        rotate = new RotateToAction();

        InputMultiplexer inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }
        stockwheel();
    }

    private void stockwheel(){
        int spins = random.nextInt(4681-2520)+2520;

        Texture wheel = new Texture(Gdx.files.internal("stockwheel.png"));
        Image stockwheel = new Image(wheel);

        stockwheel.setWidth(.5f * Gdx.graphics.getWidth());
        stockwheel.setScaling(Scaling.fillX);

        stockwheel.setOrigin(Align.center);
        stockwheel.setPosition(Gdx.graphics.getWidth()/2f - stockwheel.getWidth()/2f, Gdx.graphics.getHeight()/2f - stockwheel.getHeight()/2f);

        rotate.setRotation(spins);
        rotate.setDuration(2f);
        stockwheel.addAction(rotate);
        stage.addActor(stockwheel);
    }

    @Override
    public void render(float delta){
        if(elapsed >= duration){
            //MankomaniaGame.getInstance().setScreen(new Gamescreen);
            dispose();
        }
        super.render(delta);
        elapsed+=delta;
        ScreenUtils.clear(1, 1, 1, 1);
        stage.act(delta);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.draw();
    }
}
