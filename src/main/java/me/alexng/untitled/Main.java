package me.alexng.untitled;

import me.alexng.untitled.render.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Main {

	private static final int WIDTH = 1020, HEIGHT = 800;
	private static final String TITLE = "Title";

	public static void main(String[] args) {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);
		float[] vertexData = {
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				0.0f, 0.5f, 0.0f,
		};
		String vertexShaderSource = "#version 330 core\n" +
				"layout (location = 0) in vec3 aPos;\n" +
				"void main()\n" +
				"{\n" +
				"gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
				"}\n";
		String fragmentShaderSource = "#version 330 core\n" +
				"out vec4 FragColor;\n" +
				"void main()\n" +
				"{\n" +
				"FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
				"}\n";

		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			String log = glGetShaderInfoLog(vertexShader);
			System.out.println("Shader error: " + log);
			return;
		}

		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			String log = glGetShaderInfoLog(fragmentShader);
			System.out.println("Shader error: " + log);
			return;
		}

		int shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			String log = glGetProgramInfoLog(shaderProgram);
			System.out.println("Shader program error: " + log);
			return;
		}
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);

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
