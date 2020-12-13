package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.render.Camera;
import me.alexng.volumetricVoxels.render.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.LinkedList;
import java.util.List;

import static me.alexng.volumetricVoxels.VVConstants.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class VolumetricVoxels {

	// TODO: Update view matrix when window changes
	public static final Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 5000);

	private Window window;
	private Camera camera;
	private List<Octree> entities = new LinkedList<>();

	public void initialise() {
		window = Window.create(WIDTH, HEIGHT, TITLE);
		glEnable(GL_DEPTH_TEST);
		// glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		// TODO: This should be user defined
		window.setKeyCallback((windowHandle, key, scanCode, action, mode) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				window.setShouldClose(true);
			}
		});
		window.show();
		window.hideAndCaptureCursor();

		camera = new Camera(new Vector3f(0, 0, 0), 0, 0);
		window.setCursorPositionCallback(camera);
	}

	public void update() throws TextureException {
		camera.processInput(window);
		Matrix4f view = camera.createViewMatrix();
		window.clear();

		for (Octree entity : entities) {
			entity.draw(view, projection);
		}
		// TODO: Draw user defined voxels
		// TODO: Separate update and render calls?

		window.update();
	}

	public Octree createEntity(Vector3fc position, Object object) {
		Octree octree = null;
		try {
			octree = Rasterizer.rasterize(position, object);
		} catch (ShaderException | OctreeException e) {
			e.printStackTrace();
		}
		entities.add(octree);
		return octree;
	}

	public Window getWindow() {
		return window;
	}
}
