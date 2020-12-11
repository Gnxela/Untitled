package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.util.Colors;
import me.alexng.volumetricVoxels.util.DebugRenderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class Octree {

	public static final int NUM_CHILDREN = 8;

	private final int width;
	private final Octree root;
	private final Octree parent;

	private Vector3f position;
	private int depth;
	@Nullable private Octree[] children;

	private Octree(Vector3f position, int width, Octree root, Octree parent) {
		this.position = position;
		this.width = width;
		this.root = root;
		this.parent = parent;
		this.depth = parent.depth + 1;
	}

	private Octree(Vector3f position, int width) {
		this.position = position;
		this.width = width;
		this.root = this;
		this.depth = 1;
		this.parent = null;
	}

	public static Octree create(Vector3f position, int width) throws OctreeException {
		if (width % 2 != 0) {
			throw new OctreeException("Width not divisible by 2");
		}
		return new Octree(position, width);
	}

	public void draw(Matrix4f view, Matrix4f projection) {
		DebugRenderer.drawCubeOutline(Colors.BLUE, new Matrix4f().translate(position).scale(width), view, projection);
		if (hasChildren()) {
			for (Octree child : children) {
				child.draw(view, projection);
			}
		}
	}

	@SuppressWarnings("SuspiciousNameCombination")
	public void createChildren() throws OctreeException {
		if (width == 1) {
			throw new OctreeException("Can not create children for unit voxel");
		}
		if (hasChildren()) {
			return;
		}
		children = new Octree[NUM_CHILDREN];
		int childWidth = width / 2;
		children[0] = new Octree(position.add(0, 0, 0, new Vector3f()), childWidth, root, this);
		children[1] = new Octree(position.add(childWidth, 0, 0, new Vector3f()), childWidth, root, this);
		children[2] = new Octree(position.add(0, childWidth, 0, new Vector3f()), childWidth, root, this);
		children[3] = new Octree(position.add(0, 0, childWidth, new Vector3f()), childWidth, root, this);
		children[4] = new Octree(position.add(childWidth, childWidth, 0, new Vector3f()), childWidth, root, this);
		children[5] = new Octree(position.add(0, childWidth, childWidth, new Vector3f()), childWidth, root, this);
		children[6] = new Octree(position.add(childWidth, 0, childWidth, new Vector3f()), childWidth, root, this);
		children[7] = new Octree(position.add(childWidth, childWidth, childWidth, new Vector3f()), childWidth, root, this);
	}

	// TODO: Think about visibility
	@Nullable
	public Octree[] getChildren() {
		return children;
	}

	public boolean isRoot() {
		return this == root; // or parent == null
	}

	public boolean hasChildren() {
		return children != null;
	}
}
