package me.alexng.untitled.game;

import me.alexng.untitled.render.AttributeStore;
import me.alexng.untitled.render.Mesh;
import me.alexng.untitled.render.Texture;
import me.alexng.untitled.render.exceptions.ShaderException;
import me.alexng.untitled.render.exceptions.TextureException;
import me.alexng.untitled.render.shader.SID;
import me.alexng.untitled.render.shader.ShaderProgram;
import me.alexng.untitled.util.CubeData;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Voxel {

	private final int x, y, z;
	private final Vector3f color;

	private final Mesh mesh;
	private final ShaderProgram shaderProgram;

	public Voxel(int x, int y, int z, Vector3f color) throws ShaderException {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.mesh = new Mesh(CubeData.indexData, CubeData.vertexDataPositionNormal, new Texture[]{}, AttributeStore.VEC3F_VEC3F);
		this.shaderProgram = new ShaderProgram("me/alexng/untitled/shaders/voxel.vert", "me/alexng/untitled/shaders/voxel.frag");
	}

	public void draw(Vector3f viewPosition, Matrix4f view, Matrix4f projection) throws TextureException {
		shaderProgram.use();
		shaderProgram.setVec3f(SID.VOXEL_COLOR, color);
		shaderProgram.setVec3f(SID.LIGHT_POSITION, viewPosition);
		shaderProgram.setVec3f(SID.LIGHT_AMBIENT, 0.2f);
		shaderProgram.setVec3f(SID.LIGHT_DIFFUSE, 0.5f);
		shaderProgram.setVec3f(SID.LIGHT_SPECULAR, 0.05f);
		shaderProgram.setVec3f(SID.VIEW_POSITION, viewPosition);
		shaderProgram.setMatrix4f(SID.MODEL, new Matrix4f().translate(x, y, z));
		shaderProgram.setMatrix4f(SID.VIEW, view);
		shaderProgram.setMatrix4f(SID.PROJECTION, projection);
		mesh.draw(shaderProgram);
	}
}
