package com.mankomania.game.connections;

public class StockMinigame {

    String stock;
    boolean black;

    public StockMinigame(String stock, boolean black) {
        this.stock = stock;
        this.black = black;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public boolean isBlack() {
        return black;
    }

    public void setBlack(boolean black) {
        this.black = black;
    }
}
