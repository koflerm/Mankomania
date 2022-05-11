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

}