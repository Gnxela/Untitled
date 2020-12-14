package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.render.Camera;
import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.render.Window;
import me.alexng.volumetricVoxels.render.shader.SID;
import me.alexng.volumetricVoxels.render.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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
	private ShaderProgram voxelMeshShaderProgram;
	private List<Octree> entities = new LinkedList<>();
	private List<Mesh> meshes = new LinkedList<>();

	public void initialise() throws ShaderException {
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
		voxelMeshShaderProgram = new ShaderProgram("me/alexng/volumetricVoxels/shaders/voxelMesh.vert", "me/alexng/volumetricVoxels/shaders/voxelMesh.frag");
		voxelMeshShaderProgram.use();
		voxelMeshShaderProgram.setMatrix4f(SID.PROJECTION, projection);
		voxelMeshShaderProgram.setVec3f(SID.LIGHT_AMBIENT, new Vector3f(0.2f));
		voxelMeshShaderProgram.setVec3f(SID.LIGHT_DIFFUSE, new Vector3f(0.5f));
		voxelMeshShaderProgram.setVec3f(SID.LIGHT_SPECULAR, new Vector3f(0.2f));
	}

	public void update() throws TextureException {
		camera.processInput(window);
		Matrix4f view = camera.createViewMatrix();
		window.clear();

		//for (Octree octree : entities) {
		//	DebugRenderer.drawOctree(octree, Colors.GREEN, new Matrix4f().identity(), view, projection);
		//}

		voxelMeshShaderProgram.use();
		voxelMeshShaderProgram.setMatrix4f(SID.VIEW, view);
		voxelMeshShaderProgram.setVec3f(SID.VIEW_POSITION, camera.getPosition());
		voxelMeshShaderProgram.setVec3f(SID.LIGHT_POSITION, camera.getPosition());
		for (Mesh mesh : meshes) {
			voxelMeshShaderProgram.setMatrix4f(SID.MODEL, new Matrix4f().identity());
			mesh.draw(voxelMeshShaderProgram);
		}
		// TODO: Separate update and render calls?

		window.update();
	}

	public Octree createEntity(Object object) {
		System.out.println("Creating object of size " + object.getSize().toString());
		Octree octree = null;
		try {
			long start = System.nanoTime();
			octree = Rasterizer.rasterize(object);
			System.out.println("Rasterization time: " + (System.nanoTime() - start) / 1000000 + "ms");
		} catch (OctreeException e) {
			e.printStackTrace();
		}
		entities.add(octree);


		long start = System.nanoTime();
		Mesh mesh = Tessellater.tessellate(octree);
		System.out.println("Tesselation time: " + (System.nanoTime() - start) / 1000000 + "ms");
		System.out.println("Num triangles: " + mesh.getNumTriangles());
		System.out.println("Vertex data length: " + mesh.getVertexDataLength());
		meshes.add(mesh);
		return octree;
	}

	public Window getWindow() {
		return window;
	}
}
