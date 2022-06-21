package fieldlogic;

import java.util.ArrayList;

import playerlogic.Player;

public class Field {
    private  Field nextField;
    private  Field optionalNextField;
    private  Field previousField;
    private final int fieldIndex;
    private final String fieldDescription;
    private final float x;
    private final float y;

    public Field(int fieldIndex, String fieldDescription, float x, float y){
        this.fieldIndex = fieldIndex;
        this.fieldDescription = fieldDescription;
        this.x = x;
        this.y = y;
    }


    public boolean isIntersection(){return nextField != null && optionalNextField != null; }
    public boolean hasPlayer(ArrayList<Player> players) {
        for (Player player : players) {
             if(player.getCurrentPosition().fieldIndex == this.fieldIndex ){
                 return true;
             }
        }
        return false;
    }

    //-------GETTERS---------
    public int getFieldIndex(){return fieldIndex;}
    public String getFieldDescription(){return fieldDescription;}
    public Field getNextField(){return nextField;}
    public Field getOptionalNextField(){return optionalNextField;}
    public Field getPreviousField(){return previousField;}
    public float getX(){return x;}
    public float getY(){return y;}
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
