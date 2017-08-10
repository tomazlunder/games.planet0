package com.mygdx.zegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.zegame.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Title";
		config.width = 800;
		config.height = 480;
		config.resizable = false;
		new LwjglApplication(new Game(), config);
	}
}
