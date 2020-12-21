package me.alexng.volumetricVoxels.storage;

import me.alexng.volumetricVoxels.Voxel;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import javax.annotation.Nullable;

/**
 * A VoxelStore that accepts only positive coordinates.
 */
public class OctreeArrayGrid implements VoxelStore {

	private final Vector3i size;
	private final Octree[] grid;
	private final int cellWidth;

	/**
	 * @param cells Size in number of cells.
	 */
	public OctreeArrayGrid(Vector3ic cells, int cellWidth) throws VoxelStoreException {
		checkSize(cells);
		this.size = new Vector3i(cells).mul(cellWidth);
		this.grid = new Octree[cells.x() * cells.y() * cells.z()];
		this.cellWidth = cellWidth;
	}

	private static void checkSize(Vector3ic size) throws VoxelStoreException {
		if (size.x() < 0 && size.y() < 0 && size.z() < 0) {
			throw new VoxelStoreException("Size must be positive");
		}
	}

	@Override
	public Vector3ic getSize() {
		return size;
	}

	@Nullable
	@Override
	public Voxel get(int x, int y, int z) throws VoxelStoreException {
		Octree cell = grid[getIndex(x, y, z)];
		if (cell == null) {
			return null;
		}
		return cell.get(x, y, z);
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) throws VoxelStoreException {
		int index = getIndex(x, y, z);
		Octree cell = grid[index];
		if (cell == null) {
			grid[index] = cell = Octree.create(cellWidth);
		}
		cell.set(x, y, z, voxel);
	}

	private int getIndex(int x, int y, int z) {
		return x / cellWidth * size.x * size.y + y / cellWidth * size.y + z / cellWidth;
	}

	public Octree[] getChildren() {
		return grid;
	}

	public int getCellWidth() {
		return cellWidth;
	}
}
