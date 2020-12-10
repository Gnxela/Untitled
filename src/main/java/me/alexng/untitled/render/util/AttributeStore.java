package me.alexng.untitled.render.util;

import me.alexng.untitled.render.VertexArrayObject;

import static me.alexng.untitled.render.UntitledConstants.FLOAT_WIDTH;
import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class AttributeStore {

	public static final AttributeStore VEC3F_VEC3F_VEC3F = new AttributeStore(
			new Attribute(0, 3, GL_FLOAT, false, 9 * FLOAT_WIDTH, 0),
			new Attribute(1, 3, GL_FLOAT, false, 9 * FLOAT_WIDTH, 3 * FLOAT_WIDTH),
			new Attribute(2, 3, GL_FLOAT, false, 9 * FLOAT_WIDTH, 6 * FLOAT_WIDTH)
	);

	public static final AttributeStore VEC3F_VEC3F_VEC2F = new AttributeStore(
			new Attribute(0, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 0),
			new Attribute(1, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 3 * FLOAT_WIDTH),
			new Attribute(2, 2, GL_FLOAT, false, 8 * FLOAT_WIDTH, 6 * FLOAT_WIDTH)
	);

	public static final AttributeStore VEC3F_VEC3F = new AttributeStore(
			new Attribute(0, 3, GL_FLOAT, false, 6 * FLOAT_WIDTH, 0),
			new Attribute(1, 3, GL_FLOAT, false, 6 * FLOAT_WIDTH, 3 * FLOAT_WIDTH)
	);

	public static final AttributeStore VEC3F_VEC2F = new AttributeStore(
			new Attribute(0, 3, GL_FLOAT, false, 5 * FLOAT_WIDTH, 0),
			new Attribute(1, 2, GL_FLOAT, false, 5 * FLOAT_WIDTH, 3 * FLOAT_WIDTH)
	);

	public static final AttributeStore VEC3F = new AttributeStore(
			new Attribute(0, 3, GL_FLOAT, false, 3 * FLOAT_WIDTH, 0)
	);

	private final Attribute[] attributes;

	public AttributeStore(Attribute... attributes) {
		this.attributes = attributes;
	}

	public void setAttributes(VertexArrayObject vao) {
		for (Attribute a : attributes) {
			vao.addAttribPointer(a.index, a.size, a.type, a.normalized, a.stride, a.offset);
		}
	}

	public int getTotalSize() {
		int size = 0;
		for (Attribute attribute : attributes) {
			size += attribute.size;
		}
		return size;
	}

	public static class Attribute {
		private final int index, size, type, stride, offset;
		private final boolean normalized;

		public Attribute(int index, int size, int type, boolean normalized, int stride, int offset) {
			this.index = index;
			this.size = size;
			this.type = type;
			this.normalized = normalized;
			this.stride = stride;
			this.offset = offset;
		}
	}
}