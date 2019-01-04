package game;

import core.GameEngine;
import core.IGameLogic;

public class Core {
	
	public static void main(String[] args) {
		try {
			boolean vSync = true;
			IGameLogic gameLogic = new Game();
			GameEngine gameEng = new GameEngine("Game", 1280, 720, vSync, gameLogic);
			gameEng.start();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
