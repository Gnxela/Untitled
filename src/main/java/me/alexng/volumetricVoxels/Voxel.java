package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.render.AttributeStore;
import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.render.Texture;
import me.alexng.volumetricVoxels.render.shader.SID;
import me.alexng.volumetricVoxels.render.shader.ShaderProgram;
import me.alexng.volumetricVoxels.util.CubeData;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public class Voxel {

	private final Vector3i position;
	private final Vector3f color;

	private final Mesh mesh;
	private final ShaderProgram shaderProgram;

	public Voxel(int x, int y, int z, Vector3f color) throws ShaderException {
		this.position = new Vector3i(x, y, z);
		this.color = color;
		this.mesh = new Mesh(CubeData.indexData, CubeData.vertexDataPositionNormal, new Texture[]{}, AttributeStore.VEC3F_VEC3F);
		this.shaderProgram = new ShaderProgram("me/alexng/volumetricVoxels/shaders/voxel.vert", "me/alexng/volumetricVoxels/shaders/voxel.frag");
	}

	public void draw(Vector3f viewPosition, Matrix4f view, Matrix4f projection) throws TextureException {
		shaderProgram.use();
		shaderProgram.setVec3f(SID.VOXEL_COLOR, color);
		shaderProgram.setVec3f(SID.LIGHT_POSITION, new Vector3f(-1));
		shaderProgram.setVec3f(SID.LIGHT_AMBIENT, 0.2f);
		shaderProgram.setVec3f(SID.LIGHT_DIFFUSE, 0.5f);
		shaderProgram.setVec3f(SID.LIGHT_SPECULAR, 0.05f);
		shaderProgram.setVec3f(SID.VIEW_POSITION, viewPosition);
		shaderProgram.setMatrix4f(SID.MODEL, new Matrix4f().translate(position.x, position.y, position.z));
		shaderProgram.setMatrix4f(SID.VIEW, view);
		shaderProgram.setMatrix4f(SID.PROJECTION, projection);
		mesh.draw(shaderProgram);
	}

	public Vector3ic getPosition() {
		return position;
	}
}
