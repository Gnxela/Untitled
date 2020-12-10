package me.alexng.untitled.game;

import me.alexng.untitled.render.Mesh;
import me.alexng.untitled.render.SID;
import me.alexng.untitled.render.ShaderProgram;
import me.alexng.untitled.render.Texture;
import me.alexng.untitled.render.exceptions.ShaderException;
import me.alexng.untitled.render.exceptions.TextureException;
import me.alexng.untitled.render.util.AttributeStore;
import me.alexng.untitled.render.util.CubeData;
import org.joml.Matrix4f;

public class Voxel {

	private int x, y, z;
	private Mesh mesh;
	private ShaderProgram shaderProgram;

	public Voxel(int x, int y, int z) throws ShaderException {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mesh = new Mesh(CubeData.indexData, CubeData.vertexData, new Texture[]{}, AttributeStore.VEC3F_VEC3F_VEC2F);
		this.shaderProgram = new ShaderProgram("me/alexng/untitled/shaders/basic.vert", "me/alexng/untitled/shaders/basic.frag");
	}

	public void draw(Matrix4f view, Matrix4f projection) throws TextureException {
		shaderProgram.use();
		shaderProgram.setMatrix4f(SID.MODEL, new Matrix4f().translate(x, y, z));
		shaderProgram.setMatrix4f(SID.VIEW, view);
		shaderProgram.setMatrix4f(SID.PROJECTION, projection);
		mesh.draw(shaderProgram);
	}
}
