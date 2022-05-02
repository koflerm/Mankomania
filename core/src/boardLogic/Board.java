package boardLogic;

import java.util.ArrayList;

//import boardLogic.minigames.lotteryLogic.Lottery;  //To comment out when merged
import fieldLogic.Field;
import playerLogic.Player;

public class Board {
    private Field[] fields;
    private int[] startingFieldIndexes;
    //private Lottery lottery;                      //To comment out when merged
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    //private StockExchange stockExchangeData;      //To comment out when merged

    public Board(){
        players = new ArrayList<>();
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

    public void addPlayer(Player p){
        players.add(p);
    }
    //-----------------------

}
