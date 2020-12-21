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
	private final int octreeWidth;

	/**
	 * @param cells Size in number of cells.
	 */
	public OctreeArrayGrid(Vector3ic cells, int octreeWidth) throws VoxelStoreException {
		checkSize(cells);
		this.size = new Vector3i(cells).mul(octreeWidth);
		this.grid = new Octree[cells.x() * cells.y() * cells.z()];
		this.octreeWidth = octreeWidth;
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
		int cellX = x / octreeWidth;
		int cellY = y / octreeWidth;
		int cellZ = z / octreeWidth;
		Octree cell = grid[getIndex(cellX, cellY, cellZ)];
		if (cell == null) {
			return null;
		}
		return cell.get(x - cellX, y - cellY, z - cellZ);
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) throws VoxelStoreException {
		int cellX = x / octreeWidth;
		int cellY = y / octreeWidth;
		int cellZ = z / octreeWidth;
		int index = getIndex(cellX, cellY, cellZ);
		Octree cell = grid[index];
		if (cell == null) {
			cell = Octree.create(octreeWidth);
			grid[index] = cell;
		}
		cell.set(x - cellX * octreeWidth, y - cellY * octreeWidth, z - cellZ, voxel);
	}

	private int getIndex(int cellX, int cellY, int cellZ) {
		return cellZ * size.x * size.y + cellY * size.x + cellX;
	}

	public Octree[] getChildren() {
		return grid;
	}

	public int getOctreeWidth() {
		return octreeWidth;
	}
}
