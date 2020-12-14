package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.util.Colors;
import me.alexng.volumetricVoxels.util.DebugRenderer;
import org.joml.Matrix4f;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

public class Octree {

	// PXPYPZ means positive x, positive y, positive z. Positive x means that child.x < parent.x + width / 2
	private static final int CHILD_PXPYPZ = 0;
	private static final int CHILD_NXPYPZ = 1;
	private static final int CHILD_PXNYPZ = 2;
	private static final int CHILD_NXNYPZ = 3;
	private static final int CHILD_PXPYNZ = 4;
	private static final int CHILD_NXPYNZ = 5;
	private static final int CHILD_PXNYNZ = 6;
	private static final int CHILD_NXNYNZ = 7;

	private static final int NUM_CHILDREN = 8;
	private static final float LOG_OF_2 = (float) Math.log(2);

	private final int width;
	private final Octree root;
	private final Octree parent;

	private Vector3i position;
	private int depth;
	@Nullable private Octree[] children;
	@Nullable private Voxel value;

	private Octree(Vector3ic position, int width, Octree root, Octree parent) {
		this.position = new Vector3i(position);
		this.width = width;
		this.root = root;
		this.parent = parent;
		this.depth = parent.depth + 1;
	}

	private Octree(Vector3ic position, int width) {
		this.position = new Vector3i(position);
		this.width = width;
		this.root = this;
		this.depth = 1;
		this.parent = null;
	}

	/**
	 * Returns the minimum width required to fit {@code width} inside an {@link Octree}.
	 */
	public static int upgradeWidth(int width) {
		return (int) Math.pow(2, Math.ceil(Math.log(width) / LOG_OF_2));
	}

	private static boolean isPowerOf2(int n) {
		return (n & (n - 1)) == 0;
	}

	public static Octree create(int width) throws OctreeException {
		if (!isPowerOf2(width)) {
			throw new OctreeException("Width not divisible by 4");
		}
		return new Octree(new Vector3i(), width);
	}

	public void draw(Matrix4f view, Matrix4f projection) throws TextureException {
		DebugRenderer.drawCubeOutline(Colors.BLUE, new Matrix4f().translate(position.x, position.y, position.z).scale(width), view, projection);
		if (value != null) {
			//value.draw(new Vector3f(), view, projection);
		}

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
		children[CHILD_PXPYPZ] = new Octree(position.add(0, 0, 0, new Vector3i()), childWidth, root, this);
		children[CHILD_NXPYPZ] = new Octree(position.add(childWidth, 0, 0, new Vector3i()), childWidth, root, this);
		children[CHILD_PXNYPZ] = new Octree(position.add(0, childWidth, 0, new Vector3i()), childWidth, root, this);
		children[CHILD_PXPYNZ] = new Octree(position.add(0, 0, childWidth, new Vector3i()), childWidth, root, this);
		children[CHILD_NXNYPZ] = new Octree(position.add(childWidth, childWidth, 0, new Vector3i()), childWidth, root, this);
		children[CHILD_PXNYNZ] = new Octree(position.add(0, childWidth, childWidth, new Vector3i()), childWidth, root, this);
		children[CHILD_NXPYNZ] = new Octree(position.add(childWidth, 0, childWidth, new Vector3i()), childWidth, root, this);
		children[CHILD_NXNYNZ] = new Octree(position.add(childWidth, childWidth, childWidth, new Vector3i()), childWidth, root, this);
	}

	/**
	 * Gets the first value in the hierarchy structure.
	 */
	@Nullable
	public Voxel getFirst(int x, int y, int z) {
		if (isLeaf()) {
			return value;
		}
		if (!hasChildren()) {
			return null;
		}
		int childWidth = width / 2;
		boolean bx = x < position.x + childWidth, by = y < position.y + childWidth, bz = z < position.z + childWidth;
		Octree child;
		if (bx & by & bz) {
			child = children[CHILD_PXPYPZ];
		} else if (!bx & by & bz) {
			child = children[CHILD_NXPYPZ];
		} else if (bx & !by & bz) {
			child = children[CHILD_PXNYPZ];
		} else if (!bx & !by & bz) {
			child = children[CHILD_NXNYPZ];
		} else if (bx & by) {
			child = children[CHILD_PXPYNZ];
		} else if (!bx & by) {
			child = children[CHILD_NXPYNZ];
		} else if (bx) {
			child = children[CHILD_PXNYNZ];
		} else {
			child = children[CHILD_NXNYNZ];
		}
		return child.getFirst(x, y, z);
	}

	public void insert(Voxel voxel) throws OctreeException {
		if (isLeaf()) {
			setValue(voxel);
			return;
		}
		if (!hasChildren()) {
			createChildren();
		}
		int childWidth = width / 2;
		boolean bx = voxel.getPosition().x() < position.x + childWidth, by = voxel.getPosition().y() < position.y + childWidth, bz = voxel.getPosition().z() < position.z + childWidth;
		Octree child;
		if (bx & by & bz) {
			child = children[CHILD_PXPYPZ];
		} else if (!bx & by & bz) {
			child = children[CHILD_NXPYPZ];
		} else if (bx & !by & bz) {
			child = children[CHILD_PXNYPZ];
		} else if (!bx & !by & bz) {
			child = children[CHILD_NXNYPZ];
		} else if (bx & by) {
			child = children[CHILD_PXPYNZ];
		} else if (!bx & by) {
			child = children[CHILD_NXPYNZ];
		} else if (bx) {
			child = children[CHILD_PXNYNZ];
		} else {
			child = children[CHILD_NXNYNZ];
		}
		child.insert(voxel);
	}

	private void setValue(Voxel voxel) throws OctreeException {
		if (!isLeaf()) {
			throw new OctreeException("Can not set value of non leaf nodes.");
		}
		this.value = voxel;
	}

	// TODO: Think about visibility

	@Nullable
	public Octree[] getChildren() {
		return children;
	}

	public Vector3i getPosition() {
		return position;
	}

	@Nullable
	public Voxel getValue() {
		return value;
	}

	public boolean isRoot() {
		return this == root; // or parent == null
	}

	public Octree getRoot() {
		return root;
	}

	public Octree getParent() {
		return parent;
	}

	public boolean isLeaf() {
		return width == 1;
	}

	public boolean hasChildren() {
		return children != null;
	}

	public int getWidth() {
		return width;
	}

	public boolean contains(int x, int y, int z) {
		return x >= position.x && x < position.x + width && y >= position.y && y < position.y + width && z >= position.z && z < position.z + width;
	}
}
