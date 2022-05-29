package com.mankomania.game;

import java.util.HashMap;


public class ConPlayer {
    private int dice_2;
    private int dice_1;
    private int money;
    private boolean youTurn;
    private String socket;
    private int position;
    private int dice_Count;
    private int playerIndex;
    private HashMap<String, Integer> stocks;

    public ConPlayer(){

    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public HashMap<String, Integer> getStocks() {
        return stocks;
    }

    public void setStocks(HashMap<String, Integer> stocks) {
        this.stocks = stocks;
    }

    public boolean isYouTurn() {
        return youTurn;
    }

    public void setYouTurn(boolean youTurn) {
        this.youTurn = youTurn;
    }

    public int getDice_1() {
        return dice_1;
    }

    public void setDice_1(int dice_1) {
        this.dice_1 = dice_1;
    }

    public int getDice_2() {
        return dice_2;
    }

    public void setDice_2(int dice_2) {
        this.dice_2 = dice_2;
    }

    public int getDice_Count() {
        return dice_Count;
    }

    public void setDice_Count(int dice_Count) {
        this.dice_Count = dice_Count;
    }

    @Override
    public String toString() {
        return "ConPlayer{" +
                "dice_2=" + dice_2 +
                ", dice_1=" + dice_1 +
                ", money=" + money +
                ", youTurn=" + youTurn +
                ", socket='" + socket + '\'' +
                ", position=" + position +
                ", dice_Count=" + dice_Count +
                ", playerIndex=" + playerIndex +
                ", stocks=" + stocks +
                '}';
    }
}
