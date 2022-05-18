package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;

import java.security.SecureRandom;
import java.util.ArrayList;

public class AuctionScreen extends ScreenAdapter {
    private final SecureRandom random;
    private final Stage stage;
    private final Texture background;
    private final RotateToAction rotate;
    private final SpriteBatch batch;
    private static final float DURATION = 8;
    private float elapsed;
    private float multiplier;
    private final Skin skin;
    private final ArrayList<TextureRegionDrawable> auctionItems;
    private final Dialog auctionDialog;
    private InputMultiplexer inputMultiplexer;
    private float itemPrice;
    private boolean itemSelected;


    public AuctionScreen() {
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        stage = new Stage();
        background = new Texture("background.jpg");
        batch = new SpriteBatch();
        random = new SecureRandom();
        rotate = new RotateToAction();
        auctionItems = new ArrayList<>();
        auctionDialog = new Dialog("", skin, "alt");
        itemSelected = false;

        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }
        initImages();
        createDialog();
    }

    private void auctionwheel() {
        int spinsDeg = random.nextInt(4681 - 2520) + 2520;
        calculateMultiplier(spinsDeg);

        Texture wheel = new Texture(Gdx.files.internal("auctionwheel.png"));
        Image auctionwheelImg = new Image(wheel);

        auctionwheelImg.setHeight(.8f * Gdx.graphics.getHeight());
        auctionwheelImg.setScaling(Scaling.fillY);
        auctionwheelImg.setOrigin(Align.center);
        auctionwheelImg.setPosition(Gdx.graphics.getWidth() / 2f - auctionwheelImg.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - auctionwheelImg.getHeight() / 2f);

        Texture wheelPoint = new Texture(Gdx.files.internal("wheel-pointer.png"));
        Image wheelPointer = new Image(wheelPoint);
        wheelPointer.setScale(0.8f);
        wheelPointer.setOrigin(Align.center);
        wheelPointer.setPosition(Gdx.graphics.getWidth() / 2f - wheelPointer.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + auctionwheelImg.getHeight() /2f - wheelPointer.getHeight()/2f);

        rotate.setRotation(spinsDeg);
        rotate.setDuration(2f);
        auctionwheelImg.addAction(rotate);
        stage.addActor(auctionwheelImg);
        stage.addActor(wheelPointer);
    }

    private void calculateMultiplier(float degrees) {
        if ((degrees % 360 >= 0 && degrees % 360 < 60) || (degrees % 360 >= 120 && degrees % 360 < 180) || (degrees % 360 >= 240 && degrees % 360 < 300)) {
            multiplier = 0.5f;
        } else if (degrees % 360 >= 60 && degrees % 360 < 120) {
            multiplier = 1f;
        } else if (degrees % 360 >= 180 && degrees % 360 < 240) {
            multiplier = 0.25f;
        } else {
            multiplier = 2f;
        }
        Label label = new Label(Float.toString(multiplier), skin); //Label to check correct results
        label.setFontScale(Gdx.graphics.getHeight() / 200f);
        label.setPosition(50f, 100f);

        Label priceLabel = new Label(Float.toString(itemPrice), skin);
        priceLabel.setFontScale(Gdx.graphics.getHeight() / 200f);
        priceLabel.setPosition(50f, 200f);

        stage.addActor(label);
        stage.addActor(priceLabel);
    }

    private void createDialog() {
        float scale = Gdx.graphics.getWidth() / 1500f;
        Label label = new Label("Pick an item", skin, "title");
        auctionDialog.text(label).align(Align.center);

        Table tab = new Table();
        tab.align(Align.center);

        ImageButton img1 = new ImageButton(auctionItems.get(0));
        ImageButton img2 = new ImageButton(auctionItems.get(1));
        ImageButton img3 = new ImageButton(auctionItems.get(2));
        ImageButton img4 = new ImageButton(auctionItems.get(3));
        ImageButton img5 = new ImageButton(auctionItems.get(4));
        ImageButton img6 = new ImageButton(auctionItems.get(5));

        img1.addListener(item1Listener());
        img2.addListener(item2Listener());
        img3.addListener(item3Listener());
        img4.addListener(item4Listener());
        img5.addListener(item5Listener());
        img6.addListener(item6Listener());

        tab.add(img1);
        tab.add(img2);
        tab.add(img3).row();
        tab.add(img4);
        tab.add(img5);
        tab.add(img6);
        tab.pad(20);

        auctionDialog.getButtonTable().add(tab);
        auctionDialog.setScale(scale);
        auctionDialog.show(stage);

        float dialogXPosition = (Gdx.graphics.getWidth() / 2f - (auctionDialog.getWidth() * scale) / 2f);
        float dialogYPosition = (Gdx.graphics.getHeight() / 2f - (auctionDialog.getHeight() * scale) / 2f);
        auctionDialog.setPosition(dialogXPosition, dialogYPosition);
    }


    private void initImages() {
        Texture au1 = new Texture(Gdx.files.internal("auction/auction1.png"));
        Texture au2 = new Texture(Gdx.files.internal("auction/auction2.png"));
        Texture au3 = new Texture(Gdx.files.internal("auction/auction3.png"));
        Texture au4 = new Texture(Gdx.files.internal("auction/auction4.png"));
        Texture au5 = new Texture(Gdx.files.internal("auction/auction5.png"));
        Texture au6 = new Texture(Gdx.files.internal("auction/auction6.png"));

        TextureRegionDrawable auction1 = new TextureRegionDrawable(au1);
        TextureRegionDrawable auction2 = new TextureRegionDrawable(au2);
        TextureRegionDrawable auction3 = new TextureRegionDrawable(au3);
        TextureRegionDrawable auction4 = new TextureRegionDrawable(au4);
        TextureRegionDrawable auction5 = new TextureRegionDrawable(au5);
        TextureRegionDrawable auction6 = new TextureRegionDrawable(au6);

        auctionItems.add(auction1);
        auctionItems.add(auction2);
        auctionItems.add(auction3);
        auctionItems.add(auction4);
        auctionItems.add(auction5);
        auctionItems.add(auction6);
    }

    public ClickListener item1Listener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                auctionDialog.hide();
                itemPrice = 80000;
                itemSelected = true;
                auctionwheel();
            }
        };
    }

    public ClickListener item2Listener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                auctionDialog.hide();
                itemPrice = 60000;
                itemSelected = true;
                auctionwheel();
            }
        };
    }

    public ClickListener item3Listener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                auctionDialog.hide();
                itemPrice = 80000;
                itemSelected = true;
                auctionwheel();
            }
        };
    }

    public ClickListener item4Listener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                auctionDialog.hide();
                itemPrice = 20000;
                itemSelected = true;
                auctionwheel();
            }
        };
    }

    public ClickListener item5Listener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                auctionDialog.hide();
                itemPrice = 40000;
                itemSelected = true;
                auctionwheel();
            }
        };
    }

    public ClickListener item6Listener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                auctionDialog.hide();
                itemPrice = 100000;
                itemSelected = true;
                auctionwheel();
            }
        };
    }


    public float getMultiplier() {
        return multiplier;
    }

    public float getItemPrice(){
        return itemPrice;
    }

    @Override
    public void render(float delta) {
        if (elapsed >= DURATION) {
            //Go back to Gamescreen
            dispose();
        }
        super.render(delta);
        if(itemSelected) {
            elapsed += delta;
        }
        ScreenUtils.clear(1, 1, 1, 1);
        stage.act(delta);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        inputMultiplexer.removeProcessor(stage);
    }
}

