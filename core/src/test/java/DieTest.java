import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import diceLogic.Die;

public class DieTest {
    private final int numOfFaces = 6;
    private final int numOfDice = 1;
    @Test
    public void dieInRangeTest_returnTrue(){
        Die dice = new Die();
        List<Integer> testDice = dice.throwDice(numOfFaces,numOfDice);
        for (int index = 0; index < testDice.size(); index++) {
            int valueAtIndex = testDice.get(index);
            assertTrue(valueAtIndex <= numOfFaces && valueAtIndex >= numOfDice);
        }
    }
    @Test
    public void dieOutRangeTest_returnFalse(){
        Die dice = new Die();
        List<Integer> testDice = dice.throwDice(6,1);
        for (int index = 0; index < testDice.size(); index++) {
            int valueAtIndex = testDice.get(index);
            assertFalse(valueAtIndex > numOfFaces && valueAtIndex < numOfDice);
        }
    }

    @Test
    public void sumTest_returnTrue(){
        Die dice = new Die();
        List<Integer> testDice = dice.throwDice(numOfFaces,numOfDice);
        int actualSumOfDice = dice.getSumOfDice(testDice);
        int sum = getMaxSum(numOfFaces);
        assertTrue(actualSumOfDice <= sum && actualSumOfDice >= 1);
    }
    @Test
    public void sumTest_returnFalse(){
        Die dice = new Die();
        List<Integer> testDice = dice.throwDice(6,1);
        int actualSumOfDice = dice.getSumOfDice(testDice);
        int sum = getMaxSum(numOfFaces);
        assertFalse(actualSumOfDice > sum && actualSumOfDice < 1);
    }


    public int getMaxSum(int numOfFaces){
        int sum = 0;
        for(int i = 1; i < numOfFaces; i++){
            sum += i;
        }
        return sum;
    }

}
