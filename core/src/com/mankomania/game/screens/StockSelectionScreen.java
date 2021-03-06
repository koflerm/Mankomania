package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mankomania.game.MankomaniaGame;

import java.util.ArrayList;


public class StockSelectionScreen extends ScreenAdapter {
    private final Stage stage;
    private final Texture background;
    private final SpriteBatch batch;
    private final Dialog shareSelectionDialog;
    private final ArrayList<TextureRegionDrawable> shareList;
    private final Skin skin;
    private ImageButton dryOilButton;
    private ImageButton hardSteelButton;
    private ImageButton shortCircuitButton;
    private TextButton resetButton;
    private TextButton readyButton;
    private boolean dialogDrawn;
    private static int dryOilCount;
    private static int hardSteelCount;
    private static int shortCircuitCount;
    private Label dryOilLabel;
    private Label hardSteelLabel;
    private Label shortCircuitLabel;
    private final InputMultiplexer inputMultiplexer;
    private static final String DRY_OIL = "DryOil";
    private static final String HARD_STEEL = "HardSteel";
    private static final String SHORT_CIRCUIT = "ShortCircuit";

    public StockSelectionScreen(){
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        stage = new Stage();
        background = new Texture("background.jpg");
        batch = new SpriteBatch();
        shareList = new ArrayList<>();
        dialogDrawn = false;
        shareSelectionDialog = new Dialog("",skin, "alt");
        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }
        initImages();
    }

    private void drawImages(){
        Table tab = new Table();
        tab.align(Align.center);
        float scale = 0.7f;
        String title = "title";
        Label titleLabel = new Label("Select two shares", skin, title);
        titleLabel.setFontScale(Gdx.graphics.getHeight()/650f);
        shareSelectionDialog.setWidth(Gdx.graphics.getWidth() * scale);
        shareSelectionDialog.text(titleLabel).padTop(20f);

        dryOilButton = new ImageButton(shareList.get(0));
        hardSteelButton = new ImageButton(shareList.get(1));
        shortCircuitButton = new ImageButton(shareList.get(2));

        dryOilButton.addListener(dryOilListener());
        hardSteelButton.addListener(hardSteelListener());
        shortCircuitButton.addListener(shortCircuitListener());

        dryOilButton.addListener(changeListener());
        hardSteelButton.addListener(changeListener());
        shortCircuitButton.addListener(changeListener());

        tab.add(dryOilButton).pad(20);
        tab.add(hardSteelButton).pad(20);
        tab.add(shortCircuitButton).pad(20).row();
        tab.pad(30);

        dryOilLabel = new Label(DRY_OIL + ": " + dryOilCount + "x", skin, title);
        hardSteelLabel = new Label(HARD_STEEL + ": " + hardSteelCount + "x", skin, title);
        shortCircuitLabel = new Label(SHORT_CIRCUIT + ": " + shortCircuitCount + "x", skin, title);

        dryOilLabel.setFontScale(Gdx.graphics.getHeight()/800f);
        hardSteelLabel.setFontScale(Gdx.graphics.getHeight()/800f);
        shortCircuitLabel.setFontScale(Gdx.graphics.getHeight()/800f);

        tab.add(dryOilLabel);
        tab.add(hardSteelLabel);
        tab.add(shortCircuitLabel).row();

        resetButton = new TextButton("Reset", skin);
        resetButton.setTransform(true);
        resetButton.getLabel().setFontScale(Gdx.graphics.getHeight()/350f);
        resetButton.addListener(resetListener());
        tab.add(resetButton).pad(30f).padTop(80f).grow();

        tab.add(); //Spacing

        readyButton = new TextButton("Ready", skin);
        readyButton.setTransform(true);
        readyButton.getLabel().setFontScale(Gdx.graphics.getHeight()/350f);
        readyButton.setTouchable(Touchable.disabled);
        readyButton.addListener(readyListener());
        tab.add(readyButton).pad(30f).padTop(80f).grow();

        shareSelectionDialog.getButtonTable().add(tab);
        shareSelectionDialog.setScale(scale);
        shareSelectionDialog.show(stage);

        shareSelectionDialog.setWidth(Gdx.graphics.getWidth() / scale);
        shareSelectionDialog.setY(1000);

        dialogDrawn = true;
    }



    private void initImages(){
        Texture share1 = new Texture(Gdx.files.internal("shares/dryOil.png"));
        Texture share2 = new Texture(Gdx.files.internal("shares/hardSteel.png"));
        Texture share3 = new Texture(Gdx.files.internal("shares/shortCircuit.png"));

        TextureRegionDrawable dryOilDrawable = new TextureRegionDrawable(share1);
        TextureRegionDrawable hardSteelDrawable = new TextureRegionDrawable(share2);
        TextureRegionDrawable shortCircuitDrawable = new TextureRegionDrawable(share3);

        shareList.add(dryOilDrawable);
        shareList.add(hardSteelDrawable);
        shareList.add(shortCircuitDrawable);
    }

    public ClickListener dryOilListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                shareAdd(DRY_OIL);
                dryOilLabel.setText(DRY_OIL + ": " + dryOilCount + "x");
            }
        };
    }

    public ClickListener hardSteelListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                shareAdd(HARD_STEEL);
                hardSteelLabel.setText(HARD_STEEL + ": " + hardSteelCount + "x");
            }
        };
    }

    public ClickListener shortCircuitListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                shareAdd(SHORT_CIRCUIT);
                shortCircuitLabel.setText(SHORT_CIRCUIT + ": " + shortCircuitCount + "x");
            }
        };
    }

    public ChangeListener changeListener(){
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if((dryOilCount+shortCircuitCount+hardSteelCount) == 1){
                    dryOilButton.setTouchable(Touchable.disabled);
                    shortCircuitButton.setTouchable(Touchable.disabled);
                    hardSteelButton.setTouchable(Touchable.disabled);
                    readyButton.setTouchable(Touchable.enabled);
                }
            }
        };
    }

    public ClickListener resetListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                shareReset();
                dryOilButton.setTouchable(Touchable.enabled);
                shortCircuitButton.setTouchable(Touchable.enabled);
                hardSteelButton.setTouchable(Touchable.enabled);
                readyButton.setTouchable(Touchable.disabled);

                dryOilLabel.setText(DRY_OIL + ": " + dryOilCount + "x");
                shortCircuitLabel.setText(SHORT_CIRCUIT + ": " + shortCircuitCount + "x");
                hardSteelLabel.setText(HARD_STEEL + ": " + hardSteelCount + "x");
            }
        };
    }

    public ClickListener readyListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {
                resetButton.setTouchable(Touchable.disabled);
                dispose();
            }
        };
    }

    private static void shareAdd(String shareName){
        switch (shareName){
            case DRY_OIL:
                dryOilCount++;
                break;

            case SHORT_CIRCUIT:
                shortCircuitCount++;
                break;

            case HARD_STEEL:
                hardSteelCount++;
                break;

            default:
                throw new IllegalArgumentException("Wrong Share Name used");
        }
    }

    private static void shareReset() {
        dryOilCount = 0;
        shortCircuitCount = 0;
        hardSteelCount = 0;
    }

    @Override
    public void render(float delta){
        super.render(delta);
        ScreenUtils.clear(1, 1, 1, 1);
        stage.act(delta);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        if (!dialogDrawn)
            drawImages();
        stage.draw();


    }

    @Override
    public void dispose() {
        inputMultiplexer.removeProcessor(stage);
        stage.dispose();
        MankomaniaGame.getInstance().setScreen(new GameScreen());
    }

    public static int getDryOilCount() {
        return dryOilCount;
    }

    public static int getHardSteelCount() {
        return hardSteelCount;
    }

    public static int getShortCircuitCount() {
        return shortCircuitCount;
    }
}

