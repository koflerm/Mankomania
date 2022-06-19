package com.mankomania.game.connections;

public class ConAuction {

    int itemprice;
    int multiplicator;
    int moneyFromBank;
    int difference;
    int moneyToSet;

    public ConAuction(int itemprice, int multiplicator, int moneyFromBank, int difference, int moneyToSet) {
        this.itemprice = itemprice;
        this.multiplicator = multiplicator;
        this.moneyFromBank = moneyFromBank;
        this.difference = difference;
        this.moneyToSet = moneyToSet;
    }

    public int getItemprice() {
        return itemprice;
    }

    public void setItemprice(int itemprice) {
        this.itemprice = itemprice;
    }

    public int getMultiplicator() {
        return multiplicator;
    }

    public void setMultiplicator(int multiplicator) {
        this.multiplicator = multiplicator;
    }

    public int getMoneyFromBank() {
        return moneyFromBank;
    }

    public void setMoneyFromBank(int moneyFromBank) {
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
