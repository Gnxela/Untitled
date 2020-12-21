package me.alexng.volumetricVoxels.storage;

import me.alexng.volumetricVoxels.Voxel;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

public class ArrayGrid<T> implements VoxelStore {

	private final Vector3i size;
	private final Object[] grid;
	private final int cellWidth;

	public ArrayGrid(Vector3ic size, int cellWidth) throws VoxelStoreException {
		checkSize(size);
		this.size = new Vector3i(size);
		this.grid = new Object[size.x() * size.y() * size.z()];
		this.cellWidth = cellWidth;
	}

	private static void checkSize(Vector3ic size) throws VoxelStoreException {
		if (size.x() < 0 && size.y() < 0 && size.z() < 0) {
			throw new VoxelStoreException("Size must be positive");
		}
	}

	@Override
	public Vector3ic getPosition() {
		return null;
	}

	@Override
	public Vector3ic getSize() {
		return null;
	}

	@Override
	public boolean containsValue(int x, int y, int z) {
		return false;
	}

	@Nullable
	@Override
	public Voxel get(int x, int y, int z) throws VoxelStoreException {
		return (Voxel) grid[getIndex(x, y, z)];
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) throws VoxelStoreException {
		grid[getIndex(x, y, z)] = voxel;
	}

	private int getIndex(int x, int y, int z) {
		return x / cellWidth * size.x + y / cellWidth * size.y + z / cellWidth;
	}
}
