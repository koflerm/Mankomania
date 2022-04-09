package com.mankomania.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.mankomania.game.screens.LoadingScreen;
import com.mankomania.game.screens.StartScreen;

public class MankomaniaGame extends Game {
	private static MankomaniaGame game;

	public static MankomaniaGame getInstance() {
		if (game == null) {
			game = new MankomaniaGame();
		}
		return game;
	}

	@Override
	public void create () {
		//Get current Screen to dispose later
		com.badlogic.gdx.Screen currentScreen = this.getScreen();

		//Set new Screen
		ScreenAdapter loadingScreen = new LoadingScreen(new StartScreen());
		this.setScreen(loadingScreen);

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
