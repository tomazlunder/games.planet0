package com.mygdx.zegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.zegame.java.GameTest;


public class DesktopLauncher  {
	static LwjglApplication app;
	static LwjglApplicationConfiguration config;

	public static void main (String[] arg) {
		config = new LwjglApplicationConfiguration();
		config.title = "Title";
		config.width = 1680;
		config.height = 980;
		config.resizable = true;


		 app = new LwjglApplication(new GameTest(), config);
	}



}
