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
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				0.0f, 0.5f, 0.0f,
		};


		Shader vertexShader = new Shader("me/alexng/untitled/shaders/basic.vert");
		Shader fragmentShader = new Shader("me/alexng/untitled/shaders/basic.frag");
		vertexShader.load();
		fragmentShader.load();

		int shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader.getHandler());
		glAttachShader(shaderProgram, fragmentShader.getHandler());
		glLinkProgram(shaderProgram);

		int vao = glGenVertexArrays();
		glBindVertexArray(vao);

		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
		glEnableVertexAttribArray(0);

		while (!window.shouldClose()) {
			window.clear();
			glBindVertexArray(vao);
			glUseProgram(shaderProgram);
			glDrawArrays(GL_TRIANGLES, 0, 3);
			window.update();
		}
		window.cleanup();
	}
}
