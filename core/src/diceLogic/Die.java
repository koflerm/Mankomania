package diceLogic;

import java.util.ArrayList;
import java.security.SecureRandom;
public class Die {
    private SecureRandom secRand = new SecureRandom();
    private  ArrayList<Integer> dice;

    /*public Die() {
        this.dice = new ArrayList<>();
    }*/

    public ArrayList<Integer> throwDice (int numberOfFaces, int numberOfDice){
        dice = new ArrayList<>();
        for(int i = 0; i < numberOfDice; i++){
         dice.add((int)(secRand.nextInt() * numberOfFaces + 1));
        }
        return dice;
    }
    public int getSumOfDice (){
        int sum = 0;

        for(Integer die: dice){
            sum += die;
        }

        return sum;
    }
    //-----------GETTER-----------------
    public ArrayList<Integer> getDice(){ return dice; }
   // public void setDice(){this.dice = new ArrayList<>(); }
    //---------------------------------
}
