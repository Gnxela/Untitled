package me.alexng.volumetricVoxels.raster;

import me.alexng.volumetricVoxels.ObjectTemplate;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import me.alexng.volumetricVoxels.storage.VoxelStore;
import org.joml.Matrix4f;

public class Rasterizer {

	public static void rasterize(Matrix4f model, ObjectTemplate objectTemplate, VoxelStore voxelStore) throws VoxelStoreException {
		Rasterable[] rasterInput = objectTemplate.createRasterInput(model);
		for (Rasterable rasterable : rasterInput) {
			rasterable.rasterize(voxelStore);
		}
	}
}
