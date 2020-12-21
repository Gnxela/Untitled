package me.alexng.volumetricVoxels.storage;

import me.alexng.volumetricVoxels.Voxel;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

/**
 * A data type that can store a collections of voxels that are all locally bound. Can store in range (0, 0, 0) -> getSize()
 */
public interface VoxelStore {

	Vector3ic getSize();

	/**
	 * Returns a voxel stored at position (x, y, z).
	 */
	@Nullable
	Voxel get(int x, int y, int z) throws VoxelStoreException;

	/**
	 * Sets a voxel stored at position (voxel.x, voxel.y, voxel.z).
	 */
	void set(int x, int y, int z, Voxel voxel) throws VoxelStoreException;

	/**
	 * Returns true if the point (x, y, z) can be stored by this store.
	 */
	default boolean contains(int x, int y, int z) {
		return x >= 0 && x < getSize().x() && y >= 0 && y < getSize().y() && z >= 0 && z < getSize().z();
	}
}
