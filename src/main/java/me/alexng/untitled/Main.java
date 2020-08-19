package me.alexng.untitled;

import me.alexng.untitled.render.*;
import me.alexng.untitled.render.exceptions.UntitledException;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Main {

	private static final int FLOAT_WIDTH = 4;
	private static final float FOV = 45;
	private static final int WIDTH = 1020, HEIGHT = 800;
	private static final String TITLE = "Title";

	public static void main(String[] args) throws IOException, UntitledException {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);
		float[] vertexData = {
				// positions // texture coords
				-0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
				0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
				0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
				0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
				-0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
				-0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
				-0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
				0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
				0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
				0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
				-0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
				-0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
				-0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
				-0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
				-0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
				-0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
				-0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
				-0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
				0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
				0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
				0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
				0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
				0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
				0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
				-0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
				0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
				0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
				0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
				-0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
				-0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
				-0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
				0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
				0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
				0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
				-0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
				-0.5f, 0.5f, -0.5f, 0.0f, 1.0f
		};
		int[] indexData = {
				0, 1, 3, // first triangle
				1, 2, 3 // second triangle
		};
		Vector3f[] cubePositions = new Vector3f[]{
				new Vector3f(0.0f, 0.0f, 0.0f),
				new Vector3f(2.0f, 5.0f, -15.0f),
				new Vector3f(-1.5f, -2.2f, -2.5f),
				new Vector3f(-3.8f, -2.0f, -12.3f),
				new Vector3f(2.4f, -0.4f, -3.5f),
				new Vector3f(-1.7f, 3.0f, -7.5f),
				new Vector3f(1.3f, -2.0f, -2.5f),
				new Vector3f(1.5f, 2.0f, -2.5f),
				new Vector3f(1.5f, 0.2f, -1.5f),
				new Vector3f(-1.3f, 1.0f, -1.5f)
		};

		ShaderProgram shaderProgram = new ShaderProgram();
		shaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.vert"));
		shaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.frag"));
		shaderProgram.linkProgram();

		int vao = glGenVertexArrays();
		glBindVertexArray(vao);

		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

		int ebo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * FLOAT_WIDTH, 0);
		glEnableVertexAttribArray(0);
//		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 3 * FLOAT_WIDTH);
//		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * FLOAT_WIDTH, 3 * FLOAT_WIDTH);
		glEnableVertexAttribArray(1);

		Texture whiteWallTex = new Texture("me/alexng/untitled/textures/white_wall.jpg");
		whiteWallTex.load();
		Texture grassTex = new Texture("me/alexng/untitled/textures/grass2.jpg");
		grassTex.load();

		shaderProgram.use();
		shaderProgram.setInt("inputTexture1", 0);
		shaderProgram.setInt("inputTexture2", 1);

		// TODO: Update view matrix when window changes
		Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 100);

		Camera camera = new Camera(new Vector3f(0, 0, 3), 0, -90);
		window.hideAndCaptureCursor();
		window.setCursorPositionCallback(camera);

		glEnable(GL_DEPTH_TEST);

		while (!window.shouldClose()) {
			camera.processInput(window);
			window.clear();
			whiteWallTex.bind(0);
			grassTex.bind(1);
			glBindVertexArray(vao);
			shaderProgram.use();
			shaderProgram.setMatrix4f("projection", projection);
			Matrix4f view = camera.createViewMatrix();
			shaderProgram.setMatrix4f("view", view);
			for (Vector3f cubePosition : cubePositions) {
				Matrix4f model = new Matrix4f().identity()
						.translate(cubePosition)
						.rotate((float) Math.toRadians(-55) * (float) glfwGetTime(), 0.5f, 1, 0);
				shaderProgram.setMatrix4f("model", model);
				glDrawArrays(GL_TRIANGLES, 0, 36);
			}
			window.update();
		}
		shaderProgram.cleanup();
		whiteWallTex.cleanup();
		grassTex.cleanup();
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
		window.cleanup();
	}
}
