package com.mankomania.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mankomania.game.screens.LoadingScreen;
import com.mankomania.game.screens.StartScreen;

public class MankomaniaGame extends Game {

	@Override
	public void create () {
		//Get current Screen to dispose later
		com.badlogic.gdx.Screen currentScreen = this.getScreen();

		//Set new Screen
		ScreenAdapter newScreen = new LoadingScreen();
		this.setScreen(newScreen);

		//dispose old Screen
		if (currentScreen != null) {
			currentScreen.dispose();
		}
	}
	
	@Override
	public void dispose () {
		this.dispose();
	}
}
