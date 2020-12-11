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

		Octree octree = Octree.create(new Vector3f(), 16);
		octree.createChildren();
		octree.getChildren()[0].createChildren();
		octree.getChildren()[0].getChildren()[0].createChildren();
		octree.getChildren()[0].getChildren()[0].getChildren()[0].createChildren();
		Octree octree1 = Octree.create(new Vector3f(16), 16);
		octree1.createChildren();
		octree1.getChildren()[0].createChildren();
		octree1.getChildren()[0].getChildren()[0].createChildren();
		Octree octree2 = Octree.create(new Vector3f(32), 16);
		octree2.createChildren();
		octree2.getChildren()[0].createChildren();
		Octree octree3 = Octree.create(new Vector3f(48), 16);
		octree3.createChildren();
		Octree octree4 = Octree.create(new Vector3f(64), 16);
		Voxel voxel = new Voxel(0, 0, 0, Colors.RED);
		Voxel voxel2 = new Voxel(1, 1, 1, Colors.RED);

		while (!window.shouldClose()) {
			camera.processInput(window);
			Matrix4f view = camera.createViewMatrix();
			window.clear();

			octree.draw(view, projection);
			octree1.draw(view, projection);
			octree2.draw(view, projection);
			octree3.draw(view, projection);
			octree4.draw(view, projection);
			voxel.draw(camera.getPosition(), view, projection);
			voxel2.draw(camera.getPosition(), view, projection);

			window.update();
		}
		window.cleanup();
	}
}
