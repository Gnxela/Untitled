package me.alexng.volumetricVoxels.storage;

import me.alexng.volumetricVoxels.Voxel;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;

import javax.annotation.Nullable;

public interface VoxelStore {

	/**
	 * Returns a voxel stored at position (x, y, z).
	 */
	@Nullable
	Voxel get(int x, int y, int z) throws VoxelStoreException;

	/**
	 * Sets a voxel stored at position (voxel.x, voxel.y, voxel.z).
	 */
	void set(Voxel voxel) throws VoxelStoreException;

	// boolean contains(int x, int y, int z);
	// boolean containsValue(int x, int y, int z);
}
