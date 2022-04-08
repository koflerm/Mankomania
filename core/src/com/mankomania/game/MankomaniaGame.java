package com.mankomania.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mankomania.game.screens.LoadingScreen;
import com.mankomania.game.screens.StartScreen;

import jdk.javadoc.internal.tool.Start;

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
