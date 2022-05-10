package com.mankomania.game.screens;

import com.badlogic.gdx.Gdx;

public class Horse {
    private float xPosition;
    private float yPosition;
    private int movedSteps;

    private static float HORSE_HEIGHT = Gdx.graphics.getHeight() / 5.5f;
    private static float HORSE_WIDTH = HORSE_HEIGHT * 1.2f;

    public Horse(float x, float y) {
        xPosition = x;
        yPosition = y;
        movedSteps = 0;
    }

    public float getXPosition() {
        return xPosition;
    }

    public float getYPosition() {
        return yPosition;
    }

    public static float getWidth() {
        return HORSE_WIDTH;
    }

    public static float getHeight() {
        return HORSE_HEIGHT;
    }

    public void moveForward() {
        xPosition = xPosition + Gdx.graphics.getWidth() / 8f;
        movedSteps++;
    }

    public int getMovedSteps() {
        return movedSteps;
    }
}
