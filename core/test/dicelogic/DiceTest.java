package dicelogic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DiceTest {

    private Dice dice;

    @Before
    public void init() {
        dice = new Dice();
    }

    @Test
    public void throwDiceTest() {
        List<Integer> dices = dice.throwDice(6, 2);
        for (Integer dice: dices) {
            assertEquals(true, dice <= 6 && dice >= 1);
        }
    }

    @Test
    public void getSumOfDiceTest() {
        List<Integer> dices = dice.throwDice(6, 2);
        int sum = 0;
        for (Integer dice: dices) {
            sum += dice;
        }
        int methodSum = dice.getSumOfDice(dices);
        assertEquals(sum, methodSum);
    }

    @Test
    public void getDiceTest() {
        List<Integer> dices = dice.throwDice(6, 2);
        assertEquals(dices, dice.getDicee());
    }
}