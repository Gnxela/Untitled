package me.alexng.untitled.render;

import javax.annotation.Nullable;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL30.*;

public class VertexArrayObject implements Cleanable {

	private final int handle;
	private final VertexBufferObject vbo;
	@Nullable private VertexBufferObject ebo;

	public VertexArrayObject(VertexBufferObject vbo) {
		this.handle = glGenVertexArrays();
		this.vbo = vbo;
	}

	public VertexArrayObject() {
		this(new VertexBufferObject(GL_ARRAY_BUFFER));
	}

	public void createEbo() {
		ebo = new VertexBufferObject(GL_ELEMENT_ARRAY_BUFFER);
	}

	public void bind() {
		glBindVertexArray(handle);
	}

	public void addAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset) {
		glVertexAttribPointer(index, size, type, normalized, stride, offset);
		glEnableVertexAttribArray(index);
	}

	@Override
	public void cleanup() {
		glDeleteVertexArrays(handle);
		vbo.cleanup();
		if (ebo != null) {
			ebo.cleanup();
		}
	}

	public VertexBufferObject getVbo() {
		return vbo;
	}

	@Nullable
	public VertexBufferObject getEbo() {
		return ebo;
	}

	public void setEbo(VertexBufferObject ebo) {
		this.ebo = ebo;
	}

	public int getHandle() {
		return handle;
	}
}
