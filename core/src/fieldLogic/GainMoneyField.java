package fieldLogic;

public class GainMoneyField extends Field {
    private final int money;
    public GainMoneyField(int fieldIndex, String fieldDescription, float x, float y, int money){
        super(fieldIndex,fieldDescription, x, y);
        this.money = money;
    }

    public int getMoney(){return money;}
}
