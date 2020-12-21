package me.alexng.volumetricVoxels.raster;

import me.alexng.volumetricVoxels.Object;
import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import me.alexng.volumetricVoxels.storage.Octree;
import org.joml.Matrix4f;

public class Rasterizer {

	public static Octree rasterize(Matrix4f model, Object object) throws VoxelStoreException {
		// TODO: Always rasterizing to a potentially large octree sucks. Allow for user defined storage.
		int octreeSize = Octree.upgradeWidth((int) Math.ceil(Math.max(object.getSize().x, Math.max(object.getSize().y, object.getSize().z))));
		Octree octree = null;
		try {
			octree = Octree.create(octreeSize);
		} catch (OctreeException e) {
			e.printStackTrace();
		}

		Rasterable[] rasterInput = object.createRasterInput(model);
		for (Rasterable rasterable : rasterInput) {
			rasterable.rasterize(octree);
		}
		return octree;
	}
}
