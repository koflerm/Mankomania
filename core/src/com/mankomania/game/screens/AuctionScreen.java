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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mankomania.game.MankomaniaGame;

import java.security.SecureRandom;

public class AuctionScreen extends ScreenAdapter {
        private final SecureRandom random;
        private final Stage stage;
        private final Texture background;
        private final RotateToAction rotate;
        private final SpriteBatch batch;
        private static final float DURATION = 8;
        private float elapsed;
        private float multiplier;


        public AuctionScreen(){
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
            calculateMultiplier(spinsDeg);

            Texture wheel = new Texture(Gdx.files.internal("auctionwheel.png"));
            Image auctionwheel = new Image(wheel);

            auctionwheel.setWidth(.5f * Gdx.graphics.getWidth());
            auctionwheel.setScaling(Scaling.fillX);
            auctionwheel.setOrigin(Align.center);
            auctionwheel.setPosition(Gdx.graphics.getWidth()/2f - auctionwheel.getWidth()/2f, Gdx.graphics.getHeight()/2f - auctionwheel.getHeight()/2f);

            Texture wheelPoint = new Texture(Gdx.files.internal("wheel-pointer.png"));
            Image wheelPointer = new Image(wheelPoint);
            wheelPointer.setScale(0.8f,0.8f);
            wheelPointer.setOrigin(Align.center);
            wheelPointer.setPosition(Gdx.graphics.getWidth()/2f - wheelPointer.getWidth()/2f, Gdx.graphics.getHeight()/2f + Gdx.graphics.getHeight()*0.25f);

            rotate.setRotation(spinsDeg);
            rotate.setDuration(2f);
            auctionwheel.addAction(rotate);
            stage.addActor(auctionwheel);
            stage.addActor(wheelPointer);
        }

        private void calculateMultiplier(float degrees){
            Skin skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

            if((degrees%360 >= 0 && degrees%360 < 60) || (degrees%360 >= 120 && degrees%360 < 180) || (degrees%360 >= 240 && degrees%360 < 300)){
                multiplier = 0.5f;
            }
            else if(degrees%360 >= 60 && degrees%360 < 120){
                multiplier = 1f;
            }
            else if(degrees%360 >= 180 && degrees%360 < 240){
                multiplier = 0.25f;
            }
            else{
                multiplier = 2f;
            }
            Label label = new Label(Float.toString(multiplier), skin); //Label to check correct results
            label.setFontScale(Gdx.graphics.getHeight() / 300f);
            label.setPosition(50f,100f);
            stage.addActor(label);
        }


    public float getMultiplier() {
        return multiplier;
    }

    @Override
        public void render(float delta){
            if(elapsed >= DURATION){
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
