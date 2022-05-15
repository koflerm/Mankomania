package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;


public class StockSelectionScreen extends ScreenAdapter {
    private final Stage stage;
    private final Texture background;
    private final SpriteBatch batch;
    private final Dialog shareSelectionDialog;
    private final ArrayList<TextureRegionDrawable> shareList;
    private final Skin skin;
    private InputMultiplexer inputMultiplexer;

    public StockSelectionScreen(){
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        stage = new Stage();
        background = new Texture("background.jpg");
        batch = new SpriteBatch();
        shareList = new ArrayList<>();
        shareSelectionDialog = new Dialog("",skin, "alt");
        inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        if (!inputMultiplexer.getProcessors().contains(stage, true)) {
            inputMultiplexer.addProcessor(stage);
        }
        initImages();
        drawImages();
    }

    private void drawImages(){
        float scale = Gdx.graphics.getWidth() / 2800f;
        Label label = new Label("Select two shares", skin, "title");
        shareSelectionDialog.text(label).align(Align.center);

        Table tab = new Table();
        tab.align(Align.center);

        ImageButton dryOilButton = new ImageButton(shareList.get(0));
        ImageButton hardSteelButton = new ImageButton(shareList.get(1));
        ImageButton shortCircuitButton = new ImageButton(shareList.get(2));

        dryOilButton.addListener(dryOilListener());
        hardSteelButton.addListener(hardSteelListener());
        shortCircuitButton.addListener(shortCircuitListener());

        tab.add(dryOilButton).pad(20);
        tab.add(hardSteelButton).pad(20);
        tab.add(shortCircuitButton).pad(20);
        tab.pad(30);

        shareSelectionDialog.getButtonTable().add(tab);
        shareSelectionDialog.setScale(scale);
        shareSelectionDialog.show(stage);

        float dialogXPosition = (Gdx.graphics.getWidth() / 2f - (shareSelectionDialog.getWidth() * scale) / 2f);
        float dialogYPosition = (Gdx.graphics.getHeight() / 2f - (shareSelectionDialog.getHeight() * scale) / 2f);
        shareSelectionDialog.setPosition(dialogXPosition, dialogYPosition);
    }

    private void initImages(){
        Texture share1 = new Texture(Gdx.files.internal("shares/dryOil.png"));
        Texture share2 = new Texture(Gdx.files.internal("shares/hardSteel.png"));
        Texture share3 = new Texture(Gdx.files.internal("shares/shortCircuit.png"));

        TextureRegionDrawable dryOil = new TextureRegionDrawable(share1);
        TextureRegionDrawable hardSteel = new TextureRegionDrawable(share2);
        TextureRegionDrawable shortCircuit = new TextureRegionDrawable(share3);

        shareList.add(dryOil);
        shareList.add(hardSteel);
        shareList.add(shortCircuit);
    }

    public ClickListener dryOilListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {

            }
        };
    }

    public ClickListener hardSteelListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {

            }
        };
    }

    public ClickListener shortCircuitListener(){
        return new ClickListener() {
            @Override
            public void clicked(InputEvent inputEvent, float x, float y) {

            }
        };
    }

    @Override
    public void render(float delta){
        super.render(delta);
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
