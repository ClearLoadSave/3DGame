package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import core.Display;
import core.IGameLogic;
import core.graph.Mesh;

public class Game implements IGameLogic {
	
	private final Renderer renderer;
	private Mesh mesh;
	
	private int direction = 0;
	private float color = 0.0f;
	
	public Game() {
		renderer = new Renderer();
	}
	
	@Override
	public void init(Display display) throws Exception {
		renderer.init(display);
		
		float[] positions = new float[] {
				-0.5f,  0.5f, -5.05f,
	            -0.5f, -0.5f, -5.05f,
	             0.5f, -0.5f, -5.05f,
	             0.5f,  0.5f, -10.05f,
		};
		
		float[] colors = new float[] {
			0.5f, 0.0f, 0.0f,
			0.0f, 0.5f, 0.0f,
			0.0f, 0.0f, 0.5f,
			0.0f, 0.5f, 0.5f
		};
		
		int[] indices = new int[] {
			0, 1, 3, 3, 1, 2
		};
		
		mesh = new Mesh(positions, colors, indices);
	}

	@Override
	public void input(Display display) {
		if(display.isKeyPressed(GLFW_KEY_UP)) {
			direction = 1;
		} else if(display.isKeyPressed(GLFW_KEY_DOWN)) {
			direction = -1;
		} else {
			direction = 0;
		}
	}

	@Override
	public void update(float interval) {
		color += direction * 0.01f;
		
		if(color > 1) {
			color = 1.0f;
		} else if(color < 0) {
			color = 0.0f;
		}
	}

	@Override
	public void render(Display display) {
		display.setClearColor(color, color, color, 0.0f);
		renderer.render(display, mesh);
	}
	
	@Override
	public void cleanup() {
		renderer.cleanup();
		mesh.cleanUp();
	}
}
