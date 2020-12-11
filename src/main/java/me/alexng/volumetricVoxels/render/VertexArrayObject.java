package me.alexng.volumetricVoxels.render;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL30.*;

public class VertexArrayObject implements Cleanable {

	private final int handle;
	private final VertexBufferObject vbo;
	private final VertexBufferObject ebo;

	public VertexArrayObject(VertexBufferObject vbo, VertexBufferObject ebo) {
		this.handle = glGenVertexArrays();
		this.vbo = vbo;
		this.ebo = ebo;
	}

	public VertexArrayObject(VertexBufferObject vbo) {
		this(vbo, new VertexBufferObject(GL_ELEMENT_ARRAY_BUFFER));
	}

	public VertexArrayObject() {
		this(new VertexBufferObject(GL_ARRAY_BUFFER), new VertexBufferObject(GL_ELEMENT_ARRAY_BUFFER));
	}

	public void bind() {
		glBindVertexArray(handle);
	}

	public static void unbind() {
		glBindVertexArray(0);
	}

	public void addAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset) {
		glVertexAttribPointer(index, size, type, normalized, stride, offset);
		glEnableVertexAttribArray(index);
	}

	@Override
	public void cleanup() {
		glDeleteVertexArrays(handle);
		vbo.cleanup();
		ebo.cleanup();
	}

	public VertexBufferObject getVbo() {
		return vbo;
	}

	public VertexBufferObject getEbo() {
		return ebo;
	}

	public int getHandle() {
		return handle;
	}
}
