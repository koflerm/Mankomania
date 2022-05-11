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
        wheelPointer.setPosition(Gdx.graphics.getWidth()/2f - wheelPointer.getWidth()/2f, stockwheel.getTop()-wheelPointer.getHeight()/2f);

        rotate.setRotation(spinsDeg+5); //Weil aktuelles Bild 5° off center ist
        rotate.setDuration(2f);
        stockwheel.addAction(rotate);
        stage.addActor(stockwheel);
        stage.addActor(wheelPointer);
    }

    private void calculateStock(float degrees){
        Skin skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        String stock = "";
        String stockChange = "";

        if (degrees%360f >= 0 && degrees%360 < 120){
            stock = "Bruchstahl";
            if(degrees%360f > 80){
                stockChange = "up";
            }else{
                stockChange = "down";
            }
        }
        else if (degrees%360f >= 120 && degrees%360 < 240){
            stock = "Trocken Öl";
            if(degrees%360f > 200){
                stockChange = "up";
            }else{
                stockChange = "down";
            }
        }
       else if (degrees%360f >= 240 && degrees%360 < 360){
            stock = "Kurzschluss";
            if(degrees%360f > 320){
                stockChange = "up";
            }else{
                stockChange = "down";
            }
        }
        Label label = new Label(stock +" "+ stockChange, skin);
        label.setFontScale(Gdx.graphics.getHeight() / 400f);
        label.setPosition(50f,100f);
        stage.addActor(label);
    }

    @Override
    public void render(float delta){
        if(elapsed >= duration){
            //MankomaniaGame.getInstance().setScreen(new StartScreen());
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
