package horseRaceLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

import org.graalvm.compiler.lir.sparc.SPARCMove;

public class Horse extends Actor {
    private int movedSteps;
    private Texture horseTexture;

    private static float HORSE_HEIGHT = Gdx.graphics.getHeight() / 5.5f;
    private static float HORSE_WIDTH = HORSE_HEIGHT * 1.2f;

    public Horse(float x, float y, Texture texture) {
        this.setX(x);
        this.setY(y);
        movedSteps = 0;
        horseTexture = texture;
    }

    public void moveForward() {
        this.clearActions();
        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(this.getX() + Gdx.graphics.getWidth() / 11.5f, this.getY());
        moveAction.setDuration(0.2f);
        this.addAction(moveAction);
        movedSteps++;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(horseTexture, this.getX(), this.getY(), HORSE_WIDTH, HORSE_HEIGHT);
    }

    public int getMovedSteps() {
        return movedSteps;
    }
}
