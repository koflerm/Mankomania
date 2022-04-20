package boardLogic;

import java.util.ArrayList;

import fieldLogic.Field;
import playerLogic.Player;
import shareLogic.StockExchange;

public class Board {
    private Field[] fields;
    private int[] startingFieldIndexes;
    private Lottery lottery;
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private StockExchange stockExchangeData;

    public Board(){
        players = new ArrayList<>();
        stockExchangeData = new StockExchange();
        lottery = new Lottery();
        lottery.setLotteryAmount(0);
        currentPlayerIndex = 0;

    }

    //-------GETTERS---------
    public int getCurrentPlayerIndex(){return currentPlayerIndex;}
    public StockExchange getStockExchangeData(){return stockExchangeData;}
    public Field getCurrentPlayerField(){return fields[players.get(currentPlayerIndex).getCurrentFieldPosition()];}
    public int[] getStartingFieldIndexes(){return startingFieldIndexes;}
    public Player getCurrentPlayer(){return players.get(currentPlayerIndex);}
    public Field getFieldByIndex(int fieldIndex){return fields[fieldIndex];}
    public Field[] getFields(){return fields;}
    //-----------------------

    //------SETTERS----------
    public void setNextPlayerIndex(){
        setCurrentPlayerIndex((getCurrentPlayerIndex() + 1) % players.size());
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    //-----------------------

}
