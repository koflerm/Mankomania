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
    private int HARD_STEEL_PLC;
    private int SHORT_CIRCUIT_PLC;
    private int DRY_OIL_PLC;

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

    public int getHARD_STEEL_PLC() {
        return HARD_STEEL_PLC;
    }

    public void setHARD_STEEL_PLC(int HARD_STEEL_PLC) {
        this.HARD_STEEL_PLC = HARD_STEEL_PLC;
    }

    public int getSHORT_CIRCUIT_PLC() {
        return SHORT_CIRCUIT_PLC;
    }

    public void setSHORT_CIRCUIT_PLC(int SHORT_CIRCUIT_PLC) {
        this.SHORT_CIRCUIT_PLC = SHORT_CIRCUIT_PLC;
    }

    public int getDRY_OIL_PLC() {
        return DRY_OIL_PLC;
    }

    public void setDRY_OIL_PLC(int DRY_OIL_PLC) {
        this.DRY_OIL_PLC = DRY_OIL_PLC;
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
                ", HARD_STEEL_PLC=" + HARD_STEEL_PLC +
                ", SHORT_CIRCUIT_PLC=" + SHORT_CIRCUIT_PLC +
                ", DRY_OIL_PLC=" + DRY_OIL_PLC +
                '}';
    }
}
