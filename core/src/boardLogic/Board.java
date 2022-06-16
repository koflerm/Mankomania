package boardLogic;

import com.badlogic.gdx.Gdx;

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
    private Player currentPlayer;
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
    }

    //-------GETTERS---------

    //public StockExchange getStockExchangeData(){return stockExchangeData;}     //To comment out when merged
    public int[] getStartingFieldIndexes(){return startingFieldIndexes;}
    public Player getCurrentPlayer(){return currentPlayer;}
    public List<Player> getPlayers(){return players;}
    public Field getFieldByIndex(int fieldIndex){return fields[fieldIndex];}
    public Field[] getFields(){return fields;}
    //-----------------------

    //------SETTERS----------
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public void deleteAllPlayers(){
        for (Player p: players)
            p.dispose();

        players.clear();
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
