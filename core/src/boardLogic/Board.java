package boardLogic;

import com.badlogic.gdx.Gdx;

import org.graalvm.compiler.core.common.Fields;

import java.util.ArrayList;
import java.util.List;

//import boardLogic.minigames.lotteryLogic.Lottery;  //To comment out when merged
import fieldLogic.Field;
import playerLogic.Player;

public class Board {
    private Field[] fields;
    private int[] startingFieldIndexes;
    //private Lottery lottery;                      //To comment out when merged
    private List<Player> players;
    private int currentPlayerIndex;
    //private StockExchange stockExchangeData;      //To comment out when merged
    private float x;
    private float y;
    private float length;

    private static final float BOARD_SIZING_FACTOR = 1f;

    public Board(){
        players = new ArrayList<>();

        // UI Properties
        x = calcBoardPosition(Gdx.graphics.getWidth());
        y = calcBoardPosition(Gdx.graphics.getHeight());
        length = calcBoardSize();
        fields = BoardFields.getFields(x, length);

       // stockExchangeData = new StockExchange();  //To comment out when merged
      // lottery = new Lottery();                   //To comment out when merged
      // lottery.setLotteryAmount(0);               //To comment out when merged
        currentPlayerIndex = 0;
    }

    //-------GETTERS---------
    public int getCurrentPlayerIndex(){return currentPlayerIndex;}
    //public StockExchange getStockExchangeData(){return stockExchangeData;}     //To comment out when merged
    public Field getCurrentPlayerField(){return players.get(currentPlayerIndex).getCurrentPosition();}
    public int[] getStartingFieldIndexes(){return startingFieldIndexes;}
    public Player getCurrentPlayer(){return players.get(currentPlayerIndex);}
    public List<Player> getPlayers(){return players;}
    public Field getFieldByIndex(int fieldIndex){return fields[fieldIndex];}
    public Field[] getFields(){return fields;}
    //-----------------------

    //------SETTERS----------
    public void setNextPlayerIndex(){
        setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % players.size());
    }

    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public void addPlayer(Player p){
        players.add(p);
    }
    //-----------------------

    // UI Methods
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getLength() {
        return length;
    }

    private float calcBoardPosition(int base) {
        return base / 2f - (Gdx.graphics.getHeight() / BOARD_SIZING_FACTOR / 2);
    }

    private float calcBoardSize() {
        return Gdx.graphics.getHeight() / BOARD_SIZING_FACTOR;
    }

}
