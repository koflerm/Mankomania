package boardLogic.minigames;

import java.util.ArrayList;

import playerLogic.Player;

public class Lottery {
    private int lotteryAmount;

    public int winLottery(int playerIndex, ArrayList<Player> players){
        if (getLotteryAmount() == 0){
            players.get(playerIndex).loseMoney(50000);
            setLotteryAmount(50000);
            return -50000;
        }else{
            int win = getLotteryAmount();
            players.get(playerIndex).addMoney(50000);
            resetLotteryAmount();
            return win;
        }
    }
    public void buyLotteryTickets(int playerIndex, int price, ArrayList<Player> players){
        players.get(playerIndex).loseMoney(price);
        setLotteryAmount(getLotteryAmount() + price);
    }
    //------GETTERS----------
    public int getLotteryAmount(){return lotteryAmount;}
    //-----------------------
    //------SETTERS----------
    public void setLotteryAmount(int lotteryAmount) {
        this.lotteryAmount = lotteryAmount;
    }
    public void resetLotteryAmount(){
        this.lotteryAmount = 0;
    }
    //----------------------
}
