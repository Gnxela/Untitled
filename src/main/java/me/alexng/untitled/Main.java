package me.alexng.untitled;

import me.alexng.untitled.render.Shader;
import me.alexng.untitled.render.Window;
import me.alexng.untitled.render.exceptions.UntitledException;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Main {

	private static final int WIDTH = 1020, HEIGHT = 800;
	private static final String TITLE = "Title";

	public static void main(String[] args) throws IOException, UntitledException {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);
		float[] vertexData = {
				0.5f, 0.5f, 0.0f, // top right
				0.5f, -0.5f, 0.0f, // bottom right
				-0.5f, -0.5f, 0.0f, // bottom left
				-0.5f, 0.5f, 0.0f // top left
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

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
		glEnableVertexAttribArray(0);

		while (!window.shouldClose()) {
			window.clear();
			glBindVertexArray(vao);
			glUseProgram(shaderProgram.getHandle());
			glDrawElements(GL_TRIANGLES, indexData.length, GL_UNSIGNED_INT, 0);
			window.update();
		}
		shaderProgram.cleanup();
		window.cleanup();
	}
}
