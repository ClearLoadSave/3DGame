package core.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
	
	private final int vaoID;
	private final int posVboID;
	private final int colorVboID;
	private final int idxVboID;
	private final int vertexCount;
	
	public Mesh(float[] positions, float[] colors, int[] indices) {
		FloatBuffer posBuffer = null;
		FloatBuffer colorBuffer = null;
		IntBuffer indicesBuffer = null;
		
		try {
			vertexCount = indices.length;
			
			//Bind VAO
			vaoID = glGenVertexArrays();
			glBindVertexArray(vaoID);
			
			//Position VBO
			posVboID = glGenBuffers();
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, posVboID);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			//Color VBO
			colorVboID = glGenBuffers();
			colorBuffer = MemoryUtil.memAllocFloat(colors.length);
			colorBuffer.put(colors).flip();
			glBindBuffer(GL_ARRAY_BUFFER, colorVboID);
			glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
			
			//Index VBO
			idxVboID = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboID);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {
			if(posBuffer != null) {
				MemoryUtil.memFree(posBuffer);
			}
			
			if(colorBuffer != null) {
				MemoryUtil.memFree(colorBuffer);
			}
			
			if(indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public void cleanUp() {
		glDisableVertexAttribArray(0);
		
		//Delete VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(posVboID);
		glDeleteBuffers(colorVboID);
		glDeleteBuffers(idxVboID);
		
		//Delete VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoID);
	}
}
