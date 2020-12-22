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
	private final Vector3i cells;
	private final Octree[] grid;
	private final int octreeWidth;

	/**
	 * @param cells Size in number of cells.
	 */
	public OctreeArrayGrid(Vector3ic cells, int octreeWidth) throws VoxelStoreException {
		checkSize(cells);
		this.size = new Vector3i(cells).mul(octreeWidth);
		this.cells = new Vector3i(cells);
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
		System.out.println("Get " + cellX + cellY + cellZ);
		return cell.get(x - cellX * octreeWidth, y - cellY * octreeWidth, z - cellZ * octreeWidth);
	}

	@Nullable
	public Octree getCell(int x, int y, int z) {
		return grid[getIndex(x, y, z)];
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) throws VoxelStoreException {
		if (!contains(x, y, z)) {
			throw new VoxelStoreException("Voxel not contained: " + x + ":" + y + ":" + z);
		}
		int cellX = x / octreeWidth;
		int cellY = y / octreeWidth;
		int cellZ = z / octreeWidth;
		int index = getIndex(cellX, cellY, cellZ);
		Octree cell = grid[index];
		if (cell == null) {
			cell = Octree.create(octreeWidth);
			grid[index] = cell;
		}
		// x % octreeWidth faster?
		cell.set(x - cellX * octreeWidth, y - cellY * octreeWidth, z - cellZ * octreeWidth, voxel);
	}

	private int getIndex(int cellX, int cellY, int cellZ) {
		return cellZ * cells.x * cells.y + cellY * cells.x + cellX;
	}

	public Octree[] getChildren() {
		return grid;
	}

	public int getOctreeWidth() {
		return octreeWidth;
	}

	public Vector3i getGridSize() {
		return cells;
	}
}
