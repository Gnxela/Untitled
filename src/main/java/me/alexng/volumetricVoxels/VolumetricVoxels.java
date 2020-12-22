package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.render.Camera;
import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.render.Window;
import me.alexng.volumetricVoxels.render.shader.SID;
import me.alexng.volumetricVoxels.render.shader.ShaderProgram;
import me.alexng.volumetricVoxels.storage.VoxelStore;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.LinkedList;
import java.util.List;

import static me.alexng.volumetricVoxels.VVConstants.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.*;

public class VolumetricVoxels {

	// TODO: Update view matrix when window changes
	private Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 5000);
	private Matrix4f view;

	private Window window;
	private Camera camera;
	private ShaderProgram voxelMeshShaderProgram;
	private List<Entity> entities = new LinkedList<>();

	public void initialise() throws ShaderException {
		window = Window.create(WIDTH, HEIGHT, TITLE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
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
		voxelMeshShaderProgram = new ShaderProgram("me/alexng/volumetricVoxels/shaders/voxelMesh.vert", "me/alexng/volumetricVoxels/shaders/voxelMesh.frag");
		voxelMeshShaderProgram.use();
		voxelMeshShaderProgram.setMatrix4f(SID.PROJECTION, projection);
		voxelMeshShaderProgram.setVec3f(SID.LIGHT_AMBIENT, new Vector3f(0.2f));
		voxelMeshShaderProgram.setVec3f(SID.LIGHT_DIFFUSE, new Vector3f(0.5f));
		voxelMeshShaderProgram.setVec3f(SID.LIGHT_SPECULAR, new Vector3f(0.2f));
	}

	public void render() throws TextureException {
		camera.processInput(window);
		view = camera.createViewMatrix();
		window.clear();

		//for (Octree octree : entities) {
		//	DebugRenderer.drawOctree(octree, Colors.GREEN, new Matrix4f().identity(), view, projection);
		//}

		voxelMeshShaderProgram.use();
		voxelMeshShaderProgram.setMatrix4f(SID.VIEW, view);
		voxelMeshShaderProgram.setVec3f(SID.VIEW_POSITION, camera.getPosition());
		voxelMeshShaderProgram.setVec3f(SID.LIGHT_POSITION, camera.getPosition());
		for (Entity entity : entities) {
			entity.render(voxelMeshShaderProgram, view, projection);
		}
		// TODO: Separate update and render calls?

		window.update();
	}


	public Entity createEntity(VoxelStore voxelStore, Mesh mesh) {
		Entity entity = new Entity(voxelStore, mesh);
		entities.add(entity);
		return entity;
	}

	public Matrix4f getProjectionMatrix() {
		return projection;
	}

	public Matrix4f getViewMatrix() {
		return view;
	}

	public Window getWindow() {
		return window;
	}
}
