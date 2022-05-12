package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
    private static final float DURATION = 8;
    private float elapsed;
    private boolean isRising;
    private String share;

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
        int spinsDeg = random.nextInt(4681-2520)+2520;
        calculateStock(spinsDeg);

        Texture wheel = new Texture(Gdx.files.internal("stockwheel.png"));
        Image stockwheel = new Image(wheel);

        stockwheel.setWidth(.5f * Gdx.graphics.getWidth());
        stockwheel.setScaling(Scaling.fillX);
        stockwheel.setOrigin(Align.center);
        stockwheel.setPosition(Gdx.graphics.getWidth()/2f - stockwheel.getWidth()/2f, Gdx.graphics.getHeight()/2f - stockwheel.getHeight()/2f);

        Texture wheelPoint = new Texture(Gdx.files.internal("wheel-pointer.png"));
        Image wheelPointer = new Image(wheelPoint);
        wheelPointer.setScale(0.8f,0.8f);
        wheelPointer.setOrigin(Align.center);
        wheelPointer.setPosition(Gdx.graphics.getWidth()/2f - wheelPointer.getWidth()/2f, Gdx.graphics.getHeight()/2f + Gdx.graphics.getHeight()*0.25f);

        rotate.setRotation(spinsDeg);
        rotate.setDuration(2f);
        stockwheel.addAction(rotate);
        stage.addActor(stockwheel);
        stage.addActor(wheelPointer);
    }

    private void calculateStock(float degrees){
        Skin skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        isRising = false;

        if (degrees%360f >= 0 && degrees%360 < 120){
            share = "HardSteel";
            isRising = degrees % 360f > 84;
        }
        else if (degrees%360f >= 120 && degrees%360 < 240){
            share = "DryOil";
            isRising = degrees % 360f > 204;
        }
       else{
            share = "Shortcircuit";
            isRising = degrees%360f > 324;
        }

        Label label = new Label(share +" "+ isRising, skin); //Label to check correct results
        label.setFontScale(Gdx.graphics.getHeight() / 400f);
        label.setPosition(50f,100f);
        stage.addActor(label);
    }

    public boolean isRising() {
        return isRising;
    }

    public String getShare() {
        return share;
    }

    @Override
    public void render(float delta){
    if(elapsed >= DURATION){
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
