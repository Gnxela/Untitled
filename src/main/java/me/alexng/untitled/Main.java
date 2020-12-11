package me.alexng.untitled;

import me.alexng.untitled.exceptions.UntitledException;
import me.alexng.untitled.game.Octree;
import me.alexng.untitled.game.Voxel;
import me.alexng.untitled.render.Camera;
import me.alexng.untitled.render.Window;
import me.alexng.untitled.util.Colors;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static me.alexng.untitled.render.UntitledConstants.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class Main {

	// TODO: Update view matrix when window changes
	public static final Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 5000);

	public static void main(String[] args) throws UntitledException {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);

		glEnable(GL_DEPTH_TEST);

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		window.setKeyCallback((windowHandle, key, scanCode, action, mode) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window.getHandle(), true);
			}
		});

		window.show();
		window.hideAndCaptureCursor();
		Camera camera = new Camera(new Vector3f(0, 0, 0), 0, 0);
		window.setCursorPositionCallback(camera);

		Octree octree = Octree.create(16);
		Voxel voxel = new Voxel(8, 8, 8, Colors.RED);

		while (!window.shouldClose()) {
			camera.processInput(window);
			Matrix4f view = camera.createViewMatrix();
			window.clear();

			voxel.draw(camera.getPosition(), view, projection);
			octree.draw(view, projection);

			window.update();
		}
		window.cleanup();
	}
}
