package me.alexng.volumetricVoxels.storage;

import me.alexng.volumetricVoxels.Voxel;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

public class ArrayGrid<T> implements VoxelStore {

	private final Vector3i size;
	// TODO: Is a generic object array better than initializing a array of T?
	private final Object[] grid;

	public ArrayGrid(Vector3ic size) {
		this.size = new Vector3i(size);
		this.grid = new Object[size.x() * size.y() * size.z()];
	}

	@Nullable
	@Override
	public Voxel get(int x, int y, int z) throws VoxelStoreException {
		return (Voxel) grid[getIndex(x, y, z)];
	}

	@Override
	public void set(Voxel voxel) throws VoxelStoreException {
		grid[getIndex(voxel.getPosition())] = voxel;
	}

	private int getIndex(Vector3ic position) {
		return getIndex(position.x(), position.y(), position.z());
	}

	private int getIndex(int x, int y, int z) {
		return x * size.x + y * size.y + z;
	}
}
