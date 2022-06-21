package shareLogic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ShareTest {
    private Share share;
    @Before
    public void init() {
        share = Share.DRY_OIL_PLC;
    }

    @Test
    public void getName() {
        String shareName = share.getName();
        assertEquals(Share.DRY_OIL_PLC.getName(), shareName);
    }

    @Test
    public void values() {
        int shareCount = Share.values().length;
        assertEquals(3, shareCount);
    }

    @Test
    public void valueOf() {
        Share share = Share.valueOf("DRY_OIL_PLC");
        assertEquals(Share.DRY_OIL_PLC, share);
    }
}