package me.alexng.untitled.render;

import javax.annotation.Nullable;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL30.*;

public class ArrayObject implements Cleanable {

	private final int handle;
	private final BufferObject vbo;
	@Nullable private BufferObject ebo;

	public ArrayObject() {
		this.handle = glGenVertexArrays();
		this.vbo = new BufferObject(GL_ARRAY_BUFFER);
	}

	public void createEbo() {
		ebo = new BufferObject(GL_ELEMENT_ARRAY_BUFFER);
	}

	public void bind() {
		glBindVertexArray(handle);
	}

	@Override
	public void cleanup() {
		glDeleteVertexArrays(handle);
		vbo.cleanup();
		if (ebo != null) {
			ebo.cleanup();
		}
	}

	public BufferObject getVbo() {
		return vbo;
	}

	@Nullable
	public BufferObject getEbo() {
		return ebo;
	}

	public int getHandle() {
		return handle;
	}
}
