package diceLogic;

import java.util.ArrayList;
import java.security.SecureRandom;
import java.util.List;

public class Die {
    private SecureRandom secRand = new SecureRandom();
    private List<Integer> dice;

    /*public Die() {
        this.dice = new ArrayList<>();
    }*/

    public List<Integer> throwDice (int numberOfFaces, int numberOfDice){
        dice = new ArrayList<>();
        for(int i = 0; i < numberOfDice; i++){
         dice.add((secRand.nextInt() * numberOfFaces + 1));
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
    public List<Integer> getDice(){ return dice; }
   // public void setDice(){this.dice = new ArrayList<>(); }
    //---------------------------------
}
