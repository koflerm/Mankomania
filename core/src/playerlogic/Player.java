package playerlogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

import java.security.SecureRandom;
import java.util.HashMap;

import fieldlogic.Field;
import shareLogic.Share;

public class Player extends Actor {
    private static final float SIZE = Gdx.graphics.getHeight() / 15f;

    private int playerIndex;
    private String playerSocketID;
    private int money;
    private Field currentPosition;
    private HashMap<Share, Integer> shares;
    private Texture playerTexture;
    private int dices = 0;

    public int getDices() {
        return dices;
    }

    public void setDices(int dices) {
        this.dices = dices;
    }

    public Player() {
        money = 1000000;
        shares = new HashMap<>();
        SecureRandom secRand = new SecureRandom();
        setInitialRandomShares(secRand.nextInt(3 - 1) + 1);
    }

    public Player(Field startingField, int playerIndex, String playerSocketID) {
        money = 1000000;
        shares = new HashMap<>();
        SecureRandom secRand = new SecureRandom();
        setInitialRandomShares(secRand.nextInt(3 - 1) + 1);
        this.playerIndex = playerIndex;
        currentPosition = startingField;
        this.setX(currentPosition.getX());
        this.setY(currentPosition.getY());
        playerTexture = new Texture("p" + playerIndex + "icon.png");
        this.playerSocketID = playerSocketID;
    }

    //--------GETTERS-----------
    public int getPlayerIndex() {
        return playerIndex;
    }
    public String getPlayerSocketID() {
        return playerSocketID;
    }
    public int getMoney() {
        return money;
    }
    public int getAmountOfShare(Share shareName) {
        return shares.get(shareName);
    }
    public Field getCurrentPosition() {
        return currentPosition;
    }
    //--------------------------

    //--------SETTERS-----------
    public void setCurrentFieldPosition(Field field) {
        this.currentPosition = field;
        this.setX(currentPosition.getX());
        this.setY(currentPosition.getY());
    }
    public void setPlayerSocketID(String playerSocketID) {
        this.playerSocketID = playerSocketID;
    }
    public void setPlayerIndex(int playerIndex) {
        playerTexture = new Texture("p" + playerIndex + "icon.png");
        this.playerIndex = playerIndex;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public void setInitialRandomShares(int shareIndex) {
        switch (shareIndex) {
            case 1:
                shares.put(Share.DRY_OIL_PLC, 0);
                shares.put(Share.SHORT_CIRCUIT_PLC, 0);
                break;

            case 2:
                shares.put(Share.DRY_OIL_PLC, 0);
                shares.put(Share.HARD_STEEL_PLC, 0);
                break;

            case 3:
                shares.put(Share.HARD_STEEL_PLC, 0);
                shares.put(Share.SHORT_CIRCUIT_PLC, 0);
                break;

            default:
                break;

        }
    }


    public void setShares(int hardSteel, int shortCircuit, int dryOil) {
        shares.clear();
        shares.put(Share.HARD_STEEL_PLC, hardSteel);
        shares.put(Share.SHORT_CIRCUIT_PLC, shortCircuit);
        shares.put(Share.DRY_OIL_PLC, dryOil);
    }

    //--------------------------

    //-----MONEY UPDATING-------
    public void addMoney(int amount) {
        money += amount;
    }

    public void loseMoney(int amount) {
        money -= amount;
    }

    //--------------------------
    public void payToPlayer(Player player, int amount) {
        money -= amount;
        player.addMoney(amount);
    }

    public void moveForward(boolean moveToOptional) {
        this.clearActions();
        Field nextField;
        if (moveToOptional && currentPosition.getOptionalNextField() != null) {
            nextField = currentPosition.getOptionalNextField();
        } else {
            nextField = currentPosition.getNextField();
        }

        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(nextField.getX(), nextField.getY());
        moveAction.setDuration(0.2f);
        currentPosition = nextField;
        this.addAction(moveAction);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(playerTexture, this.getX() - (SIZE / 2), this.getY() - (SIZE / 2), SIZE, SIZE);
    }

    public void dispose() {
        playerTexture.dispose();
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerIndex=" + playerIndex +
                ", playerSocketID='" + playerSocketID + '\'' +
                ", money=" + money +
                ", currentPosition=" + currentPosition +
                ", shares=" + shares +
                ", playerTexture=" + playerTexture +
                '}';
    }
}
