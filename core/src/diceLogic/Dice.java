package diceLogic;

import java.util.ArrayList;

public class Dice {
    public ArrayList<Integer> dice;
    public ArrayList<Integer> throwDice (int numberOfFaces, int numberOfDice){
        dice = new ArrayList<>();
        for(int i = 0; i < numberOfDice; i++){
         dice.add((int)(Math.random() * numberOfFaces + 1));
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
