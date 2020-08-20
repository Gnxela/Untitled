package me.alexng.untitled;

import me.alexng.untitled.render.*;
import me.alexng.untitled.render.exceptions.UntitledException;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Main {

	private static final int FLOAT_WIDTH = 4;
	private static final float FOV = 45;
	private static final int WIDTH = 1020, HEIGHT = 800;
	private static final String TITLE = "Title";

	public static void main(String[] args) throws IOException, UntitledException {
		float[] vertexData = {
				// positions // normals // texture coords
				-0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
				0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
				0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
				0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
				-0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
				-0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
				-0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
				0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
				0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
				0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
				-0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
				-0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
				-0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
				-0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
				-0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
				-0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
				-0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
				-0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
				0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
				0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
				0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
				0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
				0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
				0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
				-0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
				0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
				0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
				0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
				-0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
				-0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
				-0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
				0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
				0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
				-0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
				-0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
		};
		int[] indexData = {
				0, 1, 3, // first triangle
				1, 2, 3 // second triangle
		};
		int length = 50;
		float spacer = 1.2f;
		Vector3f[] cubePositions = new Vector3f[length * length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				cubePositions[i + j * length] = new Vector3f(i * spacer, 0, j * spacer);
			}
		}

		Window window = Window.create(WIDTH, HEIGHT, TITLE);

		Vector3f lightPosition = new Vector3f(length * spacer / 2, 10, length * spacer / 2);

		// TODO: Update view matrix when window changes
		Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 1000);

		Camera camera = new Camera(new Vector3f(0, 0, 3), 0, -90);
		window.hideAndCaptureCursor();
		window.setCursorPositionCallback(camera);

		ShaderProgram defaultShaderProgram = new ShaderProgram();
		defaultShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.vert"));
		defaultShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.frag"));
		defaultShaderProgram.linkProgram();
		defaultShaderProgram.use();
		defaultShaderProgram.setMatrix4f("projection", projection);
		defaultShaderProgram.setVec3f("objectColor", 1, 0.5f, 0.3f);
		defaultShaderProgram.setVec3f("lightColor", 1, 1, 1);
		defaultShaderProgram.setVec3f("lightPosition", lightPosition);
		defaultShaderProgram.setVec3f("material.ambient", 1, 0.5f, 0.31f);
		defaultShaderProgram.setVec3f("material.diffuse", 1, 0.5f, 0.31f);
		defaultShaderProgram.setVec3f("material.specular", 0.5f, 0.5f, 0.5f);
		defaultShaderProgram.setFloat("material.shininess", 32);
		defaultShaderProgram.setVec3f("light.position", lightPosition);
		defaultShaderProgram.setVec3f("light.ambient", 0.2f, 0.2f, 0.2f);
		defaultShaderProgram.setVec3f("light.diffuse", 0.5f, 0.5f, 0.5f);
		defaultShaderProgram.setVec3f("light.specular", 1, 1, 1);

		VertexArrayObject cubeVao = new VertexArrayObject();
		cubeVao.bind();
		cubeVao.createEbo();
		cubeVao.getEbo().bindData(indexData);
		cubeVao.getVbo().bindData(vertexData);
		cubeVao.addAttribPointer(0, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 0);
		cubeVao.addAttribPointer(1, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 3 * FLOAT_WIDTH);
		cubeVao.addAttribPointer(2, 2, GL_FLOAT, false, 8 * FLOAT_WIDTH, 6 * FLOAT_WIDTH);

		ShaderProgram lightShaderProgram = new ShaderProgram();
		lightShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/light.vert"));
		lightShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/light.frag"));
		lightShaderProgram.linkProgram();
		lightShaderProgram.use();
		lightShaderProgram.setMatrix4f("projection", projection);

		VertexArrayObject lightVao = new VertexArrayObject(cubeVao.getVbo());
		lightVao.bind();
		lightVao.addAttribPointer(0, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 0);

		glEnable(GL_DEPTH_TEST);

		while (!window.shouldClose()) {
			camera.processInput(window);
			Matrix4f view = camera.createViewMatrix();
			window.clear();

			cubeVao.bind();
			defaultShaderProgram.use();
			defaultShaderProgram.setMatrix4f("view", view);
			defaultShaderProgram.setVec3f("viewPosition", camera.getPosition());
			for (Vector3f cubePosition : cubePositions) {
				Matrix4f model = new Matrix4f().identity()
						.translate(cubePosition);
				defaultShaderProgram.setMatrix4f("model", model);
				glDrawArrays(GL_TRIANGLES, 0, 36);
			}

			lightVao.bind();
			lightShaderProgram.use();
			Matrix4f model = new Matrix4f().identity()
					.translate(lightPosition);
			lightShaderProgram.setMatrix4f("view", view);
			lightShaderProgram.setMatrix4f("model", model);
			glDrawArrays(GL_TRIANGLES, 0, 36);

			window.update();
		}
		defaultShaderProgram.cleanup();
		cubeVao.cleanup();
		window.cleanup();
	}
}
