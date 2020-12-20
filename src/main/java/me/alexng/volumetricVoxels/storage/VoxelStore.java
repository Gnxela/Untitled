package me.alexng.volumetricVoxels.storage;

import me.alexng.volumetricVoxels.Voxel;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

/**
 * A data type that can store a collections of voxels that are all contained within a region of 3d space. This space is bounded by {@link #getPosition()} to {@link #getPosition()} + {@link #getSize()}
 */
public interface VoxelStore {

	Vector3ic getPosition();

	Vector3ic getSize();

	/**
	 * Returns a voxel stored at position (x, y, z).
	 */
	@Nullable
	Voxel get(int x, int y, int z) throws VoxelStoreException;

	/**
	 * Sets a voxel stored at position (voxel.x, voxel.y, voxel.z).
	 */
	void set(Voxel voxel) throws VoxelStoreException;

	/**
	 * Returns true it the store contains a value for (x, y, z).
	 */
	boolean containsValue(int x, int y, int z);

	/**
	 * Returns true if the point (x, y, z) can be stored by this store.
	 */
	default boolean contains(int x, int y, int z) {
		Vector3i v = getPosition().sub(x, y, z, new Vector3i());
		return v.x >= 0 && v.x < getSize().x() && v.y >= 0 && v.y < getSize().y() && v.z >= 0 && v.z < getSize().z();
	}
}
