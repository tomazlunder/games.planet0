package com.mygdx.zegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.zegame.Game;
import com.badlogic.gdx.Input;
import net.java.games.input.Component;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class DesktopLauncher  {
	static LwjglApplication app;
	static LwjglApplicationConfiguration config;

	public static void main (String[] arg) {
		config = new LwjglApplicationConfiguration();
		config.title = "Title";
		config.width = 1680;
		config.height = 1050;
		config.resizable = true;


		 app = new LwjglApplication(new Game(), config);
	}



}
