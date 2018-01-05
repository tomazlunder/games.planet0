package com.mygdx.zegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.zegame.java.GameClass;


public class DesktopLauncher  {
	static LwjglApplication app;
	static LwjglApplicationConfiguration config;

	public static void main (String[] arg) {
		config = new LwjglApplicationConfiguration();
		config.title = "zeGame";
		config.width = 1680;
		config.height = 980;
		config.resizable = true;

		//Cool mode
		config.width = 1920;
		config.height = 1080;
		config.fullscreen = true;


		app = new LwjglApplication(new GameClass(), config);
	}
}
