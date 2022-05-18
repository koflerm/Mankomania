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

import playerLogic.Player;

public class Horse extends Actor {
    private int movedSteps;
    private Texture horseTexture;
    private Player player;

    private static float horseHeight = Gdx.graphics.getHeight() / 5.5f;
    private static float horseWidth = horseHeight * 1.2f;

    public Horse(float x, float y, Texture texture, Player p) {
        this.setX(x);
        this.setY(y);
        movedSteps = 0;
        horseTexture = texture;
        player = p;
    }

    public Player getPlayer() {
        return player;
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
        batch.draw(horseTexture, this.getX(), this.getY(), horseWidth, horseHeight);
    }

    public int getMovedSteps() {
        return movedSteps;
    }
}
