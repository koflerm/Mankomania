package com.mankomania.game;

public class ConPlayer {
    private int dice2;
    private int dice1;
    private int money;
    private boolean youTurn;
    private String socket;
    private int position;
    private int diceCount;
    private int playerIndex;
    private int hardSteelPlc;
    private int shortCircuitPlc;
    private int dryOilPlc;

    public ConPlayer() {
        //Default Constructor
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

    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }

    public void setDiceCount(int diceCount) {
        this.diceCount = diceCount;
    }

    public int getHardSteelPlc() {
        return hardSteelPlc;
    }

    public void setHardSteelPlc(int hardSteelPlc) {
        this.hardSteelPlc = hardSteelPlc;
    }

    public int getShortCircuitPlc() {
        return shortCircuitPlc;
    }

    public void setShortCircuitPlc(int shortCircuitPlc) {
        this.shortCircuitPlc = shortCircuitPlc;
    }

    public int getDryOilPlc() {
        return dryOilPlc;
    }

    public void setDryOilPlc(int dryOilPlc) {
        this.dryOilPlc = dryOilPlc;
    }

    @Override
    public String toString() {
        return "ConPlayer{" +
                "dice_2=" + dice2 +
                ", dice_1=" + dice1 +
                ", money=" + money +
                ", youTurn=" + youTurn +
                ", socket='" + socket + '\'' +
                ", position=" + position +
                ", dice_Count=" + diceCount +
                ", playerIndex=" + playerIndex +
                ", HARD_STEEL_PLC=" + hardSteelPlc +
                ", SHORT_CIRCUIT_PLC=" + shortCircuitPlc +
                ", DRY_OIL_PLC=" + dryOilPlc +
                '}';
    }
}
