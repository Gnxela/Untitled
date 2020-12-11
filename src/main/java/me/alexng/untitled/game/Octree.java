package me.alexng.untitled.game;

import me.alexng.untitled.exceptions.OctreeException;
import me.alexng.untitled.util.Colors;
import me.alexng.untitled.util.DebugRenderer;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public class Octree {

	public static final int NUM_CHILDREN = 8;

	private final int width;
	private final Octree root;
	private final Octree parent;
	@Nullable private Octree[] children;

	private Octree(int width, Octree root, Octree parent) {
		this.width = width;
		this.root = root;
		this.parent = parent;
	}

	private Octree(int width) {
		this.width = width;
		this.root = this;
		this.parent = null;
	}

	public static Octree create(int width) throws OctreeException {
		if (width % 2 != 0) {
			throw new OctreeException("Width not divisible by 2");
		}
		return new Octree(width);
	}

	public void draw(Matrix4f view, Matrix4f projection) {
		DebugRenderer.drawCube(Colors.BLUE, new Matrix4f().translate(0, 0, 0).scale(width), view, projection);
	}

	public void createChildren() {
		if (hasChildren()) {
			return;
		}
		children = new Octree[NUM_CHILDREN];
		int childWidth = width / 2;
		children[0] = new Octree(childWidth, root, this);
	}

	public boolean isRoot() {
		return this == root; // or parent == null
	}

	public boolean hasChildren() {
		return children != null;
	}
}
