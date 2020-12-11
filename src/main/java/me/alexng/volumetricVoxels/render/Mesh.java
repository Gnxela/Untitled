package me.alexng.volumetricVoxels.render;

import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.render.shader.ShaderProgram;

import static org.lwjgl.opengl.GL11.*;

public class Mesh implements Cleanable {

	public static final int STRIDE = 8;

	private final int[] indices;
	private final float[] vertices;
	private final Texture[] textures;
	private VertexArrayObject vao;

	public Mesh(int[] indices, float[] vertices, Texture[] textures, AttributeStore attributeStore) {
		this.indices = indices;
		this.vertices = vertices;
		this.textures = textures;
		verifyData(vertices, attributeStore);
		setupMesh(attributeStore);
	}

	private void setupMesh(AttributeStore attributeStore) {
		vao = new VertexArrayObject();
		vao.bind();
		vao.getVbo().bindData(vertices);
		vao.getEbo().bindData(indices);
		attributeStore.setAttributes(vao);
		VertexArrayObject.unbind();
	}

	private void verifyData(float[] vertices, AttributeStore attributeStore) {
		if (vertices.length % attributeStore.getTotalSize() != 0) {
			throw new IllegalArgumentException("Vertex data did not match attributes");
		}
	}

	public void draw(ShaderProgram shaderProgram) throws TextureException {
		int d = 1, s = 1;
		for (int i = 0; i < textures.length; i++) {
			Texture texture = textures[i];
			String uniformName;
			switch (texture.getType()) {
				case DIFFUSE:
					uniformName = "material.texture_diffuse" + d++;
					break;
				case SPECULAR:
					uniformName = "material.texture_specular" + s++;
					break;
				default:
					throw new TextureException("Invalid texture type: " + texture.getType());
			}
			texture.bind(i);
			shaderProgram.setInt(uniformName, i);
		}

		vao.bind();
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
		VertexArrayObject.unbind();
	}

	@Override
	public void cleanup() {
		for (Texture texture : textures) {
			texture.cleanup();
		}
		vao.cleanup();
	}

	public VertexArrayObject getVao() {
		return vao;
	}


}
