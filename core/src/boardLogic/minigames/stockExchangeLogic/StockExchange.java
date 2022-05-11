package boardLogic.minigames.stockExchangeLogic;

import java.security.SecureRandom;

import shareLogic.Share;

public class StockExchange {
    private SecureRandom secRand = new SecureRandom();
    private Share share;
    private int shareTypeAmount = 3;
    private boolean isRising;
    private boolean needUpdate;

    public boolean isRising() {
        return isRising;
    }

    public boolean needUpdate() {
        return needUpdate;
    }

    //----------GETTERS------------
    public Share getShare() {
        return share;
    }
    //-----------------------------
    //----------SETTERS------------
    public void setRising(boolean rising) {
        isRising = rising;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }
//-----------------------------
    public void spinTheWheel(){
        int shareID = secRand.nextInt(shareTypeAmount - 1) + 1;

        switch (shareID){
            case 1: setShare(Share.SHORT_CIRCUIT_PLC);
                    setRising(true);
                    break;
            case 2: setShare(Share.SHORT_CIRCUIT_PLC);
                    setRising(false);
                    break;
            case 3: setShare(Share.HARD_STEEL_PLC);
                    setRising(true);
                    break;
            case 4: setShare(Share.HARD_STEEL_PLC);
                    setRising(false);
                    break;
            case 5: setShare(Share.DRY_OIL_PLC);
                    setRising(true);
                    break;
            case 6: setShare(Share.DRY_OIL_PLC);
                    setRising(false);
                    break;
        }
    }

}