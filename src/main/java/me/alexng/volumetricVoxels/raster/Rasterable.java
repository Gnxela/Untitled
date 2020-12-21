package me.alexng.volumetricVoxels.raster;

import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import me.alexng.volumetricVoxels.storage.VoxelStore;

// Is this even a word...?
public interface Rasterable {

	void rasterize(VoxelStore store) throws VoxelStoreException;

}
