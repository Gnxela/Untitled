package me.alexng.untitled.render;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class BufferObject implements Cleanable {

	private final int handle;
	private final int target;

	public BufferObject(int target) {
		this.target = target;
		this.handle = glGenBuffers();
		;
	}

	public void bind() {
		glBindBuffer(target, handle);
	}

	public void bindData(float[] data) {
		bind();
		glBufferData(target, data, GL_STATIC_DRAW);
	}

	public void bindData(int[] data) {
		glBufferData(target, data, GL_STATIC_DRAW);
	}

	public void addAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset) {
		glVertexAttribPointer(index, size, type, normalized, stride, offset);
		glEnableVertexAttribArray(index);
	}

	@Override
	public void cleanup() {
		glDeleteBuffers(handle);
	}

	public int getHandle() {
		return handle;
	}
}
