package me.alexng.untitled;

import me.alexng.untitled.game.Voxel;
import me.alexng.untitled.render.Camera;
import me.alexng.untitled.render.Mesh;
import me.alexng.untitled.render.Texture;
import me.alexng.untitled.render.Window;
import me.alexng.untitled.render.exceptions.UntitledException;
import me.alexng.untitled.render.util.AttributeStore;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

import static me.alexng.untitled.render.UntitledConstants.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class Main {

	// TODO: Update view matrix when window changes
	public static final Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 5000);

	public static void main(String[] args) throws IOException, UntitledException {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);

		glEnable(GL_DEPTH_TEST);

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		window.setKeyCallback((windowHandle, key, scanCode, action, mode) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window.getHandle(), true);
			} else if (key == GLFW_KEY_R && action == GLFW_RELEASE) {

			}
		});

		window.show();
		window.hideAndCaptureCursor();
		Camera camera = new Camera(new Vector3f(0, 0, 0), -90, -90);
		window.setCursorPositionCallback(camera);

		Voxel voxel = new Voxel(0, 0, 0, 0xFF0000);

		while (!window.shouldClose()) {
			camera.processInput(window);
			Matrix4f view = camera.createViewMatrix();
			window.clear();

			voxel.draw(camera.getPosition(), view, projection);

			window.update();
		}
		window.cleanup();
	}

	private static Mesh generateTextureMesh(int x, int z, Texture texture) {
		final int dx = 5, dz = 5, y = 0;
		return new Mesh(new int[]{0, 1, 2, 1, 3, 2}, new float[]{x, y, z, 0, 0, x + dx, y, z, 1, 0, x, y, z + dz, 0, 1, x + dx, y, z + dz, 1, 1}, new Texture[]{texture}, AttributeStore.VEC3F_VEC2F);
	}
}
