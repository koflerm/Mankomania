package fieldLogic;

public class JumpField extends Field {
    private final Field destination;
    public JumpField(int fieldIndex, String fieldDescription, float x, float y, Field destination){
        super(fieldIndex,fieldDescription, x, y);
        this.destination = destination;
    }

    public Field getMoney(){return destination;}
}
