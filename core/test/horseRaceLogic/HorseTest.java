package horseRaceLogic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import playerLogic.Player;

public class HorseTest {

    private Horse horse;

    @Before
    public void init() {
        horse = new Horse();
    }

    @Test
    public void getMovedSteps() {
        int movedSteps = horse.getMovedSteps();

        assertEquals(0, movedSteps);
    }
}