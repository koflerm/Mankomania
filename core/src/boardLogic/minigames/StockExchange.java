package boardLogic.minigames;

import shareLogic.Share;

public class StockExchange {
    private Share share;
    private boolean isRising;
    private boolean needUpdate;

    public boolean isRising(){return isRising;}
    public boolean needUpdate(){return needUpdate;}
    //----------GETTERS------------
    public Share getShare() {return share; }
    //-----------------------------
    //----------SETTERS------------
    public void setRising(boolean rising){isRising = rising;}
    public void setShare(Share share){this.share = share;}
    public void setNeedUpdate(boolean needUpdate){this.needUpdate = needUpdate;}
    //-----------------------------
    //TODO add roulette method to check if the stock are rising or not (random logic)
}
