package me.alexng.untitled.render.util;

import me.alexng.untitled.render.VertexArrayObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.alexng.untitled.render.UntitledConstants.FLOAT_WIDTH;
import static org.lwjgl.opengl.GL11.GL_FLOAT;

// TODO: Builder is not the right name here
public class AttributeBuilder {

	// TODO: There should be no default!
	public static final AttributeBuilder DEFAULT_ATTRIBUTE_BUILDER = new AttributeBuilder(
			new Attribute(0, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 0),
			new Attribute(1, 3, GL_FLOAT, false, 8 * FLOAT_WIDTH, 3 * FLOAT_WIDTH),
			new Attribute(2, 2, GL_FLOAT, false, 8 * FLOAT_WIDTH, 6 * FLOAT_WIDTH)
	);

	private final List<Attribute> attributes;

	public AttributeBuilder(Attribute... attributes) {
		this.attributes = new ArrayList<>(Arrays.asList(attributes));
	}

	public void addAttribute(int index, int size, int type, boolean normalized, int stride, int offset) {
		attributes.add(new Attribute(index, size, type, normalized, stride, offset));
	}

	public void setAttributes(VertexArrayObject vao) {
		for (Attribute a : attributes) {
			vao.addAttribPointer(a.index, a.size, a.type, a.normalized, a.stride, a.offset);
		}
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