package core;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWErrorCallback;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Display {
	
	private long displayHandle;
	
	private int width;
	private int height;
	private final String title;
	private boolean resized;
	private boolean vSync;
	
	public Display(String title, int width, int height, boolean vSync) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.vSync = vSync;
		this.resized = false;
	}
	
	public void init() {
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		displayHandle = glfwCreateWindow(width, height, title, NULL, NULL);
		if(displayHandle == NULL) {
			throw new RuntimeException("Failed to create GLFW window");
		}
		
		glfwSetFramebufferSizeCallback(displayHandle, (display, width, height) -> {
			this.width = width;
			this.height = height;
			this.setResized(true);
		});
		
		glfwSetKeyCallback(displayHandle, (display, key, scancode, action, mods) -> {
			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(display, true);
			}
		});
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		//Centering the Window
		glfwSetWindowPos(
				displayHandle,
				(vidmode.width() - width) / 2,
				(vidmode.height() - height) / 2
		);
		
		glfwMakeContextCurrent(displayHandle);
		
		if(isvSync()) {
			glfwSwapInterval(1);
		}
		
		glfwShowWindow(displayHandle);
		GL.createCapabilities();
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public void setClearColor(float r, float g, float b, float alpha) {
		glClearColor(r, g, b, alpha);
	}
	
	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(displayHandle, keyCode) == GLFW_PRESS;
	}
	
	public boolean displayShouldClose() {
		return glfwWindowShouldClose(displayHandle);
	}
	
	//Getters and Setters
	public boolean isResized() {
		return resized;
	}

	public void setResized(boolean resized) {
		this.resized = resized;
	}

	public boolean isvSync() {
		return vSync;
	}

	public void setvSync(boolean vSync) {
		this.vSync = vSync;
	}

	public long getDisplayHandle() {
		return displayHandle;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public void update() {
		glfwSwapBuffers(displayHandle);
		glfwPollEvents();
	}
}
