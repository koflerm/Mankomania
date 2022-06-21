package fieldlogic;
import playerlogic.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class FieldAction extends ScreenAdapter {
    private final Skin skin;
    private final Dialog fieldActionDialog;
    private boolean fieldActionDialogIsShown;

    public FieldAction(){
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        fieldActionDialog = new Dialog("", skin, "alt");
    }

    public void drawFieldActionDialog(Stage stage, Player player){
        Table tab = new Table();
        String fieldDescription = player.getCurrentPosition().getFieldDescription();
        String fieldIndex = String.valueOf(player.getCurrentPosition().getFieldIndex());

        Label labelDescription = new Label(fieldDescription, skin, "title");
        Label labelIndex = new Label("Feld " + fieldIndex, skin, "title");

        labelDescription.setWrap(true);
        labelDescription.setWidth(Gdx.graphics.getWidth()/2f);

        tab.add(labelIndex).row();
        tab.add(labelDescription).width(Gdx.graphics.getWidth()/2f);

        fieldActionDialog.add(tab);

        fieldActionDialog.show(stage);

        float dialogXPosition = (Gdx.graphics.getWidth() / 2f - (fieldActionDialog.getWidth()) / 2f);
        float dialogYPosition = (Gdx.graphics.getHeight() / 2f - (fieldActionDialog.getHeight()) / 2f);
        fieldActionDialog.setPosition(dialogXPosition, dialogYPosition);
        setFieldActionDialogIsShown(true);
    }

    public boolean getFieldActionDialogIsShown() {
        return fieldActionDialogIsShown;
    }

    public void setFieldActionDialogIsShown(boolean fieldActionDialogIsShown) {
        this.fieldActionDialogIsShown = fieldActionDialogIsShown;
    }

    public void removeFieldActionDialog(){
        fieldActionDialog.remove();
        fieldActionDialog.clear();
    }
}


