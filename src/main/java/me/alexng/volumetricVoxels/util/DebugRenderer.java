package me.alexng.volumetricVoxels.util;

import me.alexng.volumetricVoxels.render.AttributeStore;
import me.alexng.volumetricVoxels.render.VertexArrayObject;
import me.alexng.volumetricVoxels.render.shader.SID;
import me.alexng.volumetricVoxels.render.shader.ShaderProgram;
import me.alexng.volumetricVoxels.storage.Octree;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3i;

import static org.lwjgl.opengl.GL11.*;


public class DebugRenderer {

	private static ShaderProgram debugShaderProgram;
	private static VertexArrayObject cubeOutlineVAO;
	private static VertexArrayObject cubeVAO;

	static {
		try {
			debugShaderProgram = new ShaderProgram("me/alexng/volumetricVoxels/shaders/debug.vert", "me/alexng/volumetricVoxels/shaders/debug.frag");

			cubeOutlineVAO = new VertexArrayObject();
			cubeOutlineVAO.bind();
			cubeOutlineVAO.getVbo().bindData(CubeData.linesVertexData);
			cubeOutlineVAO.getEbo().bindData(CubeData.linesIndexData);
			AttributeStore.VEC3F.setAttributes(cubeOutlineVAO);

			cubeVAO = new VertexArrayObject();
			cubeVAO.bind();
			cubeVAO.getVbo().bindData(CubeData.vertexDataPosition);
			cubeVAO.getEbo().bindData(CubeData.indexData);
			AttributeStore.VEC3F.setAttributes(cubeVAO);


			VertexArrayObject.unbind();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void drawCube(Vector3f color, Matrix4f model, Matrix4f view, Matrix4f projection) {
		debugShaderProgram.use();
		debugShaderProgram.setVec3f(SID.DEBUG_COLOR, color);
		debugShaderProgram.setMatrix4f(SID.MODEL, model);
		debugShaderProgram.setMatrix4f(SID.VIEW, view);
		debugShaderProgram.setMatrix4f(SID.PROJECTION, projection);
		cubeVAO.bind();
		glDrawElements(GL_TRIANGLES, CubeData.indexData.length, GL_UNSIGNED_INT, 0);
	}

	public static void drawCubeOutline(Vector3f color, Matrix4f model, Matrix4f view, Matrix4f projection) {
		debugShaderProgram.use();
		debugShaderProgram.setVec3f(SID.DEBUG_COLOR, color);
		debugShaderProgram.setMatrix4f(SID.MODEL, model);
		debugShaderProgram.setMatrix4f(SID.VIEW, view);
		debugShaderProgram.setMatrix4f(SID.PROJECTION, projection);
		cubeOutlineVAO.bind();
		glDrawElements(GL_LINES, CubeData.indexData.length, GL_UNSIGNED_INT, 0);
	}

	public static void drawOctree(Octree octree, Vector3f color, Matrix4f model, Matrix4f view, Matrix4f projection) {
		debugShaderProgram.use();
		debugShaderProgram.setVec3f(SID.DEBUG_COLOR, color);
		debugShaderProgram.setMatrix4f(SID.VIEW, view);
		debugShaderProgram.setMatrix4f(SID.PROJECTION, projection);
		cubeOutlineVAO.bind();
		drawOctree(octree, model);
	}

	private static void drawOctree(Octree octree, Matrix4fc model) {
		Vector3i position = octree.getPosition();
		debugShaderProgram.setMatrix4f(SID.MODEL, new Matrix4f(model).translate(position.x, position.y, position.z, new Matrix4f()).scale(octree.getWidth()));
		glDrawElements(GL_LINES, CubeData.indexData.length, GL_UNSIGNED_INT, 0);
		if (octree.getValue() != null) {
			//value.draw(new Vector3f(), view, projection);
		}

		if (octree.hasChildren()) {
			for (Octree child : octree.getChildren()) {
				drawOctree(child, model);
			}
		}
	}
}
