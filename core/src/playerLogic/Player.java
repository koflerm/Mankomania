package playerLogic;

import com.badlogic.gdx.utils.IntArray;

import java.util.HashMap;

import shareLogic.Share;

public class Player {
    private int playerIndex;
    private int money;
    private int currentFieldPosition;

    private IntArray movePath;
    private HashMap<Share,Integer> shares;

    public Player(){

    }
    public Player(int startingField, int playerIndex){
        money = 1000000;
        movePath = new IntArray();

        shares = new HashMap<>();
        shares.put(Share.DRY_OIL_PLC,0);
        shares.put(Share.SHORT_CIRCUIT_PLC,0);
        shares.put(Share.HARD_STEEL_PLC,0);

        this.playerIndex = playerIndex;
        this.currentFieldPosition = startingField;

    }

    //--------GETTERS-----------
    public int getPlayerIndex() {
        return playerIndex;
    }
    public int getMoney() {
        return money;
    }
    public int getAmountOfShare(Share shareName){return shares.get(shareName);}
    public int getCurrentFieldPosition(){return currentFieldPosition;}
    //--------------------------
    //--------SETTERS-----------
    public void setCurrentFieldPosition(int fieldIndex){
        this.currentFieldPosition = fieldIndex;
    }
    public void setPlayerIndex(int playerIndex){
        this.playerIndex = playerIndex;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    //--------------------------

    //-----MONEY UPDATING-------
    public void addMoney(int amount){money += amount;}
    public void loseMoney(int amount){money -= amount;}
    //--------------------------

    public void payToPlayer(Player player, int amount){
        money -= amount;
        player.addMoney(amount);
    }
}
