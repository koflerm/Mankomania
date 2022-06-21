package com.mankomania.game.connections;

public class ConAuction {
    float itemprice;
    float multiplicator;
    float moneyFromBank;
    int difference;
    int moneyToSet;

    public ConAuction(float itemprice, float multiplicator, float moneyFromBank, int difference, int moneyToSet) {
        this.itemprice = itemprice;
        this.multiplicator = multiplicator;
        this.moneyFromBank = moneyFromBank;
        this.difference = difference;
        this.moneyToSet = moneyToSet;
    }

    public float getItemprice() {
        return itemprice;
    }

    public void setItemprice(float itemprice) {
        this.itemprice = itemprice;
    }

    public float getMultiplicator() {
        return multiplicator;
    }

    public void setMultiplicator(float multiplicator) {
        this.multiplicator = multiplicator;
    }

    public float getMoneyFromBank() {
        return moneyFromBank;
    }

    public void setMoneyFromBank(float moneyFromBank) {
        this.moneyFromBank = moneyFromBank;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public int getMoneyToSet() {
        return moneyToSet;
    }

    public void setMoneyToSet(int moneyToSet) {
        this.moneyToSet = moneyToSet;
    }
}
