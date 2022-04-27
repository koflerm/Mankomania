package diceLogic;

import java.util.ArrayList;
import java.security.SecureRandom;
public class Dice {
    private SecureRandom secRand = new SecureRandom();
    public ArrayList<Integer> dice;
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
}
