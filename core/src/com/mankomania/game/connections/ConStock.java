package com.mankomania.game.connections;

public class ConStock {
    int ShortCircuit_PLC;
    int HardSteel_PLC;
    int DryOil_PLC;

    public ConStock(int shortC, int dry, int hard){
        this.ShortCircuit_PLC = shortC;
        this.DryOil_PLC = dry;
        this.HardSteel_PLC = hard;
    }
}
