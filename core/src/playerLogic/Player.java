package playerLogic;

import com.badlogic.gdx.utils.IntArray;

import java.security.SecureRandom;
import java.util.HashMap;

import fieldLogic.Field;
import shareLogic.Share;

public class Player {
    private int playerIndex;
    private int money;
    private Field currentPosition;
    private SecureRandom secRand = new SecureRandom();
   // private IntArray movePath;
    private HashMap<Share,Integer> shares;

    public Player(){

    }
    public Player(Field startingField, int playerIndex){
        money = 1000000;
       // movePath = new IntArray();
        shares = new HashMap<>();
        setInitialRandomShares(secRand.nextInt(3 - 1) + 1);
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
    public void setInitialRandomShares(int share_Index){
        switch(share_Index){
            case 1: shares.put(Share.DRY_OIL_PLC,0);
                    shares.put(Share.SHORT_CIRCUIT_PLC,0);

            case 2:shares.put(Share.DRY_OIL_PLC,0);
                   shares.put(Share.HARD_STEEL_PLC,0);

            case 3:shares.put(Share.HARD_STEEL_PLC,0);
                   shares.put(Share.SHORT_CIRCUIT_PLC,0);
        }
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
