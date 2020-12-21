package me.alexng.volumetricVoxels.raster;

import me.alexng.volumetricVoxels.ObjectTemplate;
import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import me.alexng.volumetricVoxels.storage.Octree;
import org.joml.Matrix4f;

public class Rasterizer {

	public static Octree rasterize(Matrix4f model, ObjectTemplate objectTemplate) throws VoxelStoreException {
		// TODO: Always rasterizing to a potentially large octree sucks. Allow for user defined storage.
		int octreeSize = Octree.upgradeWidth((int) Math.ceil(Math.max(objectTemplate.getSize().x, Math.max(objectTemplate.getSize().y, objectTemplate.getSize().z))));
		Octree octree = null;
		try {
			octree = Octree.create(octreeSize);
		} catch (OctreeException e) {
			e.printStackTrace();
		}

		Rasterable[] rasterInput = objectTemplate.createRasterInput(model);
		for (Rasterable rasterable : rasterInput) {
			rasterable.rasterize(octree);
		}
		return octree;
	}
}
