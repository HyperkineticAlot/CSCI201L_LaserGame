package com.hyperkinetic.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hyperkinetic.game.LaserGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Laser Game";
		config.width = 500;
		config.height = 500;
		new LwjglApplication(new LaserGame(), config);
	}
}
