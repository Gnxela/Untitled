package me.alexng.untitled;

import me.alexng.untitled.render.*;
import me.alexng.untitled.render.exceptions.UntitledException;
import me.alexng.untitled.render.util.CubeData;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

import static me.alexng.untitled.render.UntitledConstants.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class Main {

	public static void main(String[] args) throws IOException, UntitledException {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);

		Vector3f lightPosition = new Vector3f(0, 0, -2);
		Vector3f modelPosition = new Vector3f(0, 0, 0);

		// TODO: Update view matrix when window changes
		Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 1000);

		ShaderProgram defaultShaderProgram = new ShaderProgram();
		defaultShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.vert"));
		defaultShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.frag"));
		defaultShaderProgram.linkProgram();
		defaultShaderProgram.use();
		defaultShaderProgram.setMatrix4f("projection", projection);
		defaultShaderProgram.setFloat("material.shininess", 32);
		defaultShaderProgram.setVec3f("light.ambient", 0.2f, 0.2f, 0.2f);
		defaultShaderProgram.setVec3f("light.diffuse", 0.5f, 0.5f, 0.5f);
		defaultShaderProgram.setVec3f("light.specular", 1, 1, 1);

		Mesh cubeMesh = new Mesh(CubeData.indexData, CubeData.vertexData, new Texture[]{});

		ShaderProgram lightShaderProgram = new ShaderProgram();
		lightShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/light.vert"));
		lightShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/light.frag"));
		lightShaderProgram.linkProgram();
		lightShaderProgram.use();
		lightShaderProgram.setMatrix4f("projection", projection);

		glEnable(GL_DEPTH_TEST);

		Model backpack = new Model("me/alexng/untitled/temp/backpack.obj");
		backpack.load();

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		window.show();
		window.hideAndCaptureCursor();
		Camera camera = new Camera(new Vector3f(0, 0, 3), 0, -90);
		window.setCursorPositionCallback(camera);

		while (!window.shouldClose()) {
			camera.processInput(window);
			Matrix4f view = camera.createViewMatrix();
			window.clear();

			float radius = 10f;
			lightPosition.x = (float) (Math.cos(glfwGetTime()) * radius);
			lightPosition.y = (float) (Math.cos(glfwGetTime() / 3) * radius);
			lightPosition.z = (float) (Math.sin(glfwGetTime()) * radius);

			defaultShaderProgram.use();
			defaultShaderProgram.setVec3f("light.position", lightPosition);
			defaultShaderProgram.setMatrix4f("view", view);
			defaultShaderProgram.setVec3f("viewPosition", camera.getPosition());
			for (int i = 0; i < 100; i++) {
				Matrix4f model = new Matrix4f().identity()
						.scale(0.5f)
						.translate(modelPosition)
						.translate(2 * i, 0, 0);
				defaultShaderProgram.setMatrix4f("model", model);
				backpack.draw(defaultShaderProgram);
			}

			lightShaderProgram.use();
			Matrix4f model = new Matrix4f().identity()
					.translate(lightPosition);
			lightShaderProgram.setMatrix4f("view", view);
			lightShaderProgram.setMatrix4f("model", model);
			cubeMesh.draw(lightShaderProgram);

			window.update();
		}
		defaultShaderProgram.cleanup();
		cubeMesh.cleanup();
		window.cleanup();
	}
}
