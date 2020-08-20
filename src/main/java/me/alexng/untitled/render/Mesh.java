package me.alexng.untitled.render;

import me.alexng.untitled.render.exceptions.TextureException;

import static me.alexng.untitled.render.UntitledConstants.FLOAT_WIDTH;
import static org.lwjgl.opengl.GL11.*;

public class Mesh implements Cleanable {

	private final int[] indices;
	private final float[] vertices; // stride: 8. vec3 position, vec3 normal, vec2 texCoord
	private final Texture[] textures;
	private VertexArrayObject vao;

	public Mesh(int[] indices, float[] vertices, Texture[] textures) {
		this.indices = indices;
		this.vertices = vertices;
		this.textures = textures;
		setupMesh();
	}

	private void setupMesh() {
		vao = new VertexArrayObject();
		vao.bind();
		vao.getVbo().bindData(vertices);
		vao.getEbo().bindData(indices);
		vao.addAttribPointer(0, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 0);
		vao.addAttribPointer(1, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 3 * FLOAT_WIDTH);
		vao.addAttribPointer(2, 2, GL_FLOAT, false, 8 * FLOAT_WIDTH, 6 * FLOAT_WIDTH);
		VertexArrayObject.unbind();
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
