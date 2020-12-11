package me.alexng.untitled.util;

import me.alexng.untitled.render.AttributeStore;
import me.alexng.untitled.render.VertexArrayObject;
import me.alexng.untitled.render.shader.SID;
import me.alexng.untitled.render.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;


public class DebugRenderer {

	private static ShaderProgram debugShaderProgram;
	private static VertexArrayObject cubeVAO;

	static {
		try {
			debugShaderProgram = new ShaderProgram("me/alexng/untitled/shaders/debug.vert", "me/alexng/untitled/shaders/debug.frag");

			cubeVAO = new VertexArrayObject();
			cubeVAO.bind();
			cubeVAO.getVbo().bindData(CubeData.linesVertexData);
			cubeVAO.getEbo().bindData(CubeData.linesIndexData);
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
		glDrawElements(GL_LINES, CubeData.indexData.length, GL_UNSIGNED_INT, 0);
	}
}
