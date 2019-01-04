package core;

public class GameEngine implements Runnable {
	
	private boolean running = false;
	
	public static final int FPS = 60;
	public static final int UPS = 30;
	
	private final Display display;
	private final Thread gameLoopThread;
	private final IGameLogic gameLogic;
	private final Timer timer;
	
	public GameEngine(String displayTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
		gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		display = new Display(displayTitle, width, height, vSync);
		this.gameLogic = gameLogic;
		timer = new Timer();
	}
	
	public void start() {
		String osName = System.getProperty("os.name");
		if(osName.contains("Mac")) {
			gameLoopThread.run();
		} else {
			gameLoopThread.start();
		}
	}
	
	@Override
	public void run() {
		try {
			init();
			gameLoop();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			gameLogic.cleanup();
		}
	}
	
	protected void init() throws Exception {
		display.init();
		gameLogic.init(display);
		timer.init();
		
		running = true;
	}
	
	protected void gameLoop() {
		float elapsed;
		float steps = 0f;
		float interval = 1f / UPS;
		
		while(running && !display.displayShouldClose()) {
			elapsed = timer.getElapsed();
			steps += elapsed;
			
			input();
			
			while(steps >= interval) {
				update(interval);
				steps -= interval;
			}
			
			render();
			
			if(!display.isvSync()) {
				sync();
			}
		}
	}
	
	private void sync() {
		float loopSlot = 1f / FPS;
		double end = UPS + loopSlot;
		
		while(System.currentTimeMillis() < end) {
			try {
				Thread.sleep(1);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void input() {
		gameLogic.input(display);
	}
	
	protected void update(float interval) {
		gameLogic.update(interval);
	}
	
	protected void render() {
		gameLogic.render(display);
		display.update();
	}
}

class Timer {
	
	private double last;
	
	public void init() {
		last = getTime();
	}
	
	public double getTime() {
		return System.nanoTime() / 1000000000.0;
	}
	
	public float getElapsed() {
		double time = getTime();
		float elapsed = (float)(time - last);
		last = time;
		return elapsed;
	}
	
	public double getLast() {
		return last;
	}
}
