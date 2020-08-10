package me.alexng.untitled;

import me.alexng.untitled.render.Shader;
import me.alexng.untitled.render.Texture;
import me.alexng.untitled.render.Window;
import me.alexng.untitled.render.exceptions.UntitledException;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Main {

	private static final int FLOAT_WIDTH = 4;
	private static final int WIDTH = 1020, HEIGHT = 800;
	private static final String TITLE = "Title";

	public static void main(String[] args) throws IOException, UntitledException {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);
		float[] vertexData = {
				// positions // colors // texture coords
				0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
				0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // bottom right
				-0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom left
				-0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f // top left
		};
		int[] indexData = {
				0, 1, 3, // first triangle
				1, 2, 3 // second triangle
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

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 3 * FLOAT_WIDTH);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * FLOAT_WIDTH, 6 * FLOAT_WIDTH);
		glEnableVertexAttribArray(2);

		Texture whiteWallTex = new Texture("me/alexng/untitled/textures/white_wall.jpg");
		whiteWallTex.load();
		Texture grassTex = new Texture("me/alexng/untitled/textures/grass.jpg");
		grassTex.load();

		shaderProgram.use();
		shaderProgram.setInt("inputTexture1", 0);
		shaderProgram.setInt("inputTexture2", 1);

		while (!window.shouldClose()) {
			window.clear();
			whiteWallTex.bind(0);
			grassTex.bind(1);
			shaderProgram.use();
			glBindVertexArray(vao);
			glDrawElements(GL_TRIANGLES, indexData.length, GL_UNSIGNED_INT, 0);
			window.update();
		}
		shaderProgram.cleanup();
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
		window.cleanup();
	}
}
