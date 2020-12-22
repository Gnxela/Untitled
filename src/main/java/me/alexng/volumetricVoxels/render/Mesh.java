package me.alexng.volumetricVoxels.render;

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

	public void draw(ShaderProgram shaderProgram) {
		shaderProgram.use();
		vao.bind();
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
		VertexArrayObject.unbind();
	}

	public void drawOutline(ShaderProgram shaderProgram) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		draw(shaderProgram);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
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

	public int getVertexDataLength() {
		return vertices.length;
	}

	public int getNumTriangles() {
		return indices.length / 3;
	}
}
