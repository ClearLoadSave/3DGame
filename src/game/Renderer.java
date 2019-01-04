package game;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import core.Display;
import core.Utils;
import core.graph.Mesh;
import core.graph.ShaderProgram;

public class Renderer {
	
	private ShaderProgram shaderProgram;
	
	//FOV
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.0f;
	private Matrix4f projectionMatrix;
	
	public Renderer() {
		
	}
	
	public void init(Display display) throws Exception {
		shaderProgram = new ShaderProgram();
		shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
		shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
		shaderProgram.link();
		
		//Projection Matrix
		float aspectRatio = (float) display.getWidth() / display.getHeight();
		projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
		shaderProgram.createUniform("projectionMatrix");
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(Display display, Mesh mesh) {
		clear();
		
		if(display.isResized()) {
			glViewport(0, 0, display.getWidth(), display.getHeight());
			display.setResized(false);
		}
		
		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);
		
		//Drawing the Mesh
		glBindVertexArray(mesh.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
		
		//Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
		shaderProgram.unbind();
	}
	
	public void cleanup() {
		if(shaderProgram != null) {
			shaderProgram.cleanup();
		}
	}
}
