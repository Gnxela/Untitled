package me.alexng.untitled.render;

import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject implements Cleanable {

	private final int handle;
	private final int target; // GL_ARRAY_BUFFER, GL_ELEMENT_ARRAY_BUFFER

	public VertexBufferObject(int target) {
		this.target = target;
		this.handle = glGenBuffers();
	}

	public void bind() {
		glBindBuffer(target, handle);
	}

	public void bindData(float[] data) {
		bind();
		glBufferData(target, data, GL_STATIC_DRAW);
	}

	public void bindData(int[] data) {
		bind();
		glBufferData(target, data, GL_STATIC_DRAW);
	}

	@Override
	public void cleanup() {
		glDeleteBuffers(handle);
	}

	public int getHandle() {
		return handle;
	}
}
