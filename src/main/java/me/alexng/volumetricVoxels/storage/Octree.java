package me.alexng.volumetricVoxels.storage;

import me.alexng.volumetricVoxels.Voxel;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

public class Octree implements VoxelStore {

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

	private final Octree root;
	private final Vector3i position;
	private final int width;

	@Nullable private Octree[] children;
	// TODO: Storing leaves as an entire new Octree is a waste of memory.
	@Nullable private Voxel value;

	private Octree(Vector3ic position, int width, Octree root) {
		this.position = new Vector3i(position);
		this.width = width;
		this.root = root;
	}

	private Octree(Vector3ic position, int width) {
		this.position = new Vector3i(position);
		this.width = width;
		this.root = this;
	}

	@Override
	public Vector3ic getSize() {
		return null;
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

	public static Octree create(int width) throws VoxelStoreException {
		if (!isPowerOf2(width)) {
			throw new VoxelStoreException("Width not divisible by 4");
		}
		return new Octree(new Vector3i(), width);
	}

	@SuppressWarnings("SuspiciousNameCombination")
	public void createChildren() throws VoxelStoreException {
		if (width == 1) {
			throw new VoxelStoreException("Can not create children for unit voxel");
		}
		if (hasChildren()) {
			return;
		}
		children = new Octree[NUM_CHILDREN];
		int childWidth = width / 2;
		children[CHILD_PXPYPZ] = new Octree(position.add(0, 0, 0, new Vector3i()), childWidth, root);
		children[CHILD_NXPYPZ] = new Octree(position.add(childWidth, 0, 0, new Vector3i()), childWidth, root);
		children[CHILD_PXNYPZ] = new Octree(position.add(0, childWidth, 0, new Vector3i()), childWidth, root);
		children[CHILD_PXPYNZ] = new Octree(position.add(0, 0, childWidth, new Vector3i()), childWidth, root);
		children[CHILD_NXNYPZ] = new Octree(position.add(childWidth, childWidth, 0, new Vector3i()), childWidth, root);
		children[CHILD_PXNYNZ] = new Octree(position.add(0, childWidth, childWidth, new Vector3i()), childWidth, root);
		children[CHILD_NXPYNZ] = new Octree(position.add(childWidth, 0, childWidth, new Vector3i()), childWidth, root);
		children[CHILD_NXNYNZ] = new Octree(position.add(childWidth, childWidth, childWidth, new Vector3i()), childWidth, root);
	}

	/**
	 * Gets the first value in the hierarchy structure.
	 */
	@Override
	@Nullable
	public Voxel get(int x, int y, int z) {
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
		return child.get(x, y, z);
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) throws VoxelStoreException {
		// TODO: We should have internal (private) and external (public) versions. Internal needs less checks
		if (!contains(x, y, z)) {
			throw new VoxelStoreException("Voxel not contained in octree");
		}
		if (isLeaf()) {
			setValue(voxel);
			return;
		}
		if (!hasChildren()) {
			createChildren();
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
		child.set(x, y, z, voxel);
	}

	private void setValue(Voxel voxel) throws VoxelStoreException {
		if (!isLeaf()) {
			throw new VoxelStoreException("Can not set value of non leaf nodes.");
		}
		this.value = voxel;
	}

	public boolean contains(int x, int y, int z) {
		return x >= position.x && x < position.x + width && y >= position.y && y < position.y + width && z >= position.z && z < position.z + width;
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

	public boolean isLeaf() {
		return width == 1;
	}

	public boolean hasChildren() {
		return children != null;
	}

	public int getWidth() {
		return width;
	}
}
