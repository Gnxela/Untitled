package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.util.Colors;
import me.alexng.volumetricVoxels.util.DebugRenderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import javax.annotation.Nullable;

public class Octree {

	private static final int NUM_CHILDREN = 8;
	private static final float LOG_OF_2 = (float) Math.log(2);

	private final int width;
	private final Octree root;
	private final Octree parent;

	private Vector3f position;
	private int depth;
	@Nullable private Octree[] children;
	@Nullable private Voxel value;

	private Octree(Vector3fc position, int width, Octree root, Octree parent) {
		this.position = new Vector3f(position);
		this.width = width;
		this.root = root;
		this.parent = parent;
		this.depth = parent.depth + 1;
	}

	private Octree(Vector3fc position, int width) {
		this.position = new Vector3f(position);
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

	public static Octree create(Vector3fc position, int width) throws OctreeException {
		if (!isPowerOf2(width)) {
			throw new OctreeException("Width not divisible by 4");
		}
		return new Octree(position, width);
	}

	public void draw(Matrix4f view, Matrix4f projection) throws TextureException {
		DebugRenderer.drawCubeOutline(Colors.BLUE, new Matrix4f().translate(position).scale(width), view, projection);
		if (value != null) {
			value.draw(new Vector3f(), view, projection);
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
		children[0] = new Octree(position.add(0, 0, 0, new Vector3f()), childWidth, root, this);
		children[1] = new Octree(position.add(childWidth, 0, 0, new Vector3f()), childWidth, root, this);
		children[2] = new Octree(position.add(0, childWidth, 0, new Vector3f()), childWidth, root, this);
		children[3] = new Octree(position.add(0, 0, childWidth, new Vector3f()), childWidth, root, this);
		children[4] = new Octree(position.add(childWidth, childWidth, 0, new Vector3f()), childWidth, root, this);
		children[5] = new Octree(position.add(0, childWidth, childWidth, new Vector3f()), childWidth, root, this);
		children[6] = new Octree(position.add(childWidth, 0, childWidth, new Vector3f()), childWidth, root, this);
		children[7] = new Octree(position.add(childWidth, childWidth, childWidth, new Vector3f()), childWidth, root, this);
	}

	public void insert(Voxel voxel) throws OctreeException {
		if (isLeaf()) {
			System.out.println("set");
			setValue(voxel);
			return;
		}
		if (!hasChildren()) {
			createChildren();
		}
		Vector3f distance = position.sub(voxel.getPosition().x(), voxel.getPosition().y(), voxel.getPosition().z(), new Vector3f()).mul(-1);
		int childWidth = width / 2;
		boolean x = distance.x < childWidth, y = distance.y < childWidth, z = distance.z < childWidth;
		Octree child;
		if (x & y & z) {
			child = children[0];
		} else if (!x & y & z) {
			child = children[1];
		} else if (x & !y & z) {
			child = children[2];
		} else if (x & y & !z) {
			child = children[3];
		} else if (!x & !y & z) {
			child = children[4];
		} else if (x & !y) {
			child = children[5];
		} else if (y) {
			child = children[6];
		} else {
			child = children[7];
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

	public boolean isRoot() {
		return this == root; // or parent == null
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
