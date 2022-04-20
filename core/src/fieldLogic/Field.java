package fieldLogic;

public class Field {
    private final int nextField;
    private final int optionalNextField;
    private final int previousField;
    private final String fieldDescription;
    private final FieldColor color;
    private final int fieldIndex;

    public Field(int fieldIndex, int nextField, int optionalNextField, int previousField, String fieldDescription, FieldColor color){
        this.fieldIndex = fieldIndex;
        this.nextField = nextField;
        this.optionalNextField = optionalNextField;
        this.previousField = previousField;
        this.fieldDescription = fieldDescription;
        this.color = color;
    }


    public boolean isIntersection(){return nextField > -1 && optionalNextField > -1; }
    //-------GETTERS---------
    public int getFieldIndex(){return fieldIndex;}
    public int getNextField(){return nextField;}
    public int getOptionalNextField(){return optionalNextField;}
    public int getPreviousField(){return previousField;}
    public String getFieldDescription(){return fieldDescription;}
    public FieldColor getColor(){return color;}
    //-----------------------

}
