package boardLogic.minigames.auctionLogic;

import java.security.SecureRandom;

public class Auction {
    private int multTypeAmount = 4;
    private double multiplicator = 0;
    private SecureRandom secRand = new SecureRandom();
    private Lot chosenLot;

    //--------GETTER-------------
    public Lot getChosenLot(){
        return chosenLot;
    }
    public double getEvaluatedPrice(){
        return chosenLot.getPrice() * multiplicator;
    }
    //---------------------------
    //--------SETTER-------------
    private void setLot(Lot lot){
        chosenLot = lot;
    }
    private void setMultiplicator(double mult){
        multiplicator = mult;
    }
    //---------------------------

    public void spinTheHammer(){
        int multID = secRand.nextInt(multTypeAmount - 1) + 1;

        switch (multID){
            case 1: setMultiplicator(1);
                break;
            case 2: setMultiplicator(0.5);
                break;
            case 3: setMultiplicator(0.25);
                break;
            case 4: setMultiplicator(2);
                break;
        }
    }
}
