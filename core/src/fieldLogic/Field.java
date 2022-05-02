package fieldLogic;

import java.util.ArrayList;

import playerLogic.Player;

public class Field {
    private final Field nextField;
    private final Field optionalNextField;
    private final Field previousField;
    private final String fieldDescription;
    private final FieldColor color;
    private final int fieldIndex;

    public Field(int fieldIndex, Field nextField, Field optionalNextField, Field previousField, String fieldDescription, FieldColor color){
        this.fieldIndex = fieldIndex;
        this.nextField = nextField;
        this.optionalNextField = optionalNextField;
        this.previousField = previousField;
        this.fieldDescription = fieldDescription;
        this.color = color;
    }


    public boolean isIntersection(){return nextField != null && optionalNextField != null; }
    public boolean hasPlayer(ArrayList<Player> players) {
        for (Player player : players) {
             if(player.getCurrentPosition().fieldIndex == this.fieldIndex ){
                 return true;
             }
        };
        return false;
    }

    //-------GETTERS---------
    public int getFieldIndex(){return fieldIndex;}
    public Field getNextField(){return nextField;}
    public Field getOptionalNextField(){return optionalNextField;}
    public Field getPreviousField(){return previousField;}
    public String getFieldDescription(){return fieldDescription;}
    public FieldColor getColor(){return color;}
    //-----------------------

}
