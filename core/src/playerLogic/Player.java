package playerLogic;

import com.badlogic.gdx.utils.IntArray;

import java.util.HashMap;

import fieldLogic.Field;
import shareLogic.Share;

public class Player {
    private int playerIndex;
    private int money;
    private Field currentPosition;

   // private IntArray movePath;
    private HashMap<Share,Integer> shares;

    public Player(){

    }
    public Player(Field startingField, int playerIndex){
        money = 1000000;
       // movePath = new IntArray();
        // TODO add choosing method for Shares
        shares = new HashMap<>();
        shares.put(Share.DRY_OIL_PLC,0);
        shares.put(Share.SHORT_CIRCUIT_PLC,0);
        shares.put(Share.HARD_STEEL_PLC,0);

        this.playerIndex = playerIndex;
        this.currentPosition = startingField;

    }

    //--------GETTERS-----------
    public int getPlayerIndex() {
        return playerIndex;
    }
    public int getMoney() {
        return money;
    }
    public int getAmountOfShare(Share shareName){return shares.get(shareName);}
    public Field getCurrentPosition(){return currentPosition;}
    //--------------------------
    //--------SETTERS-----------
    public void setCurrentFieldPosition(Field field){
        this.currentPosition = field;
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
