package fieldLogic;

import java.util.ArrayList;

import playerLogic.Player;

public class Field {
    private  Field nextField;
    private  Field optionalNextField;
    private  Field previousField;
    private final String fieldDescription;
    private final FieldColor color;
    private final int fieldIndex;

    public Field(int fieldIndex,  String fieldDescription, FieldColor color){
        this.fieldIndex = fieldIndex;
        this.nextField = null;
        this.optionalNextField = null;
        this.previousField = null;
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
    //--------SETTERS--------
    public void setNextField(Field field){
        this.nextField = field;
    }
    public void setOptionalNextField(Field field){
        this.optionalNextField = field;
    }
    public void setPreviousField(Field field){
        this.previousField = field;
    }
    //-----------------------

}
