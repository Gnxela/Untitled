package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.util.Colors;
import org.joml.Vector3f;

public class Rasterizer {


	// TODO: Shader exception?!?!?
	public static Octree rasterize(Object object) throws ShaderException, OctreeException {
		int octreeSize = Octree.upgradeWidth((int) Math.ceil(Math.max(object.getSize().x, Math.max(object.getSize().y, object.getSize().z))));
		Octree octree = null;
		try {
			octree = Octree.create(octreeSize);
		} catch (OctreeException e) {
			e.printStackTrace();
		}
		Vector3f point = new Vector3f();
		for (int x = 0; x < octreeSize; x++) {
			for (int y = 0; y < octreeSize; y++) {
				for (int z = 0; z < octreeSize; z++) {
					point.set(x, y, z);
					if (object.containsPoint(point)) {
						// TODO: Given that we know the order in which we are creating these. It is inefficient to have to search for the voxel's node every time.
						octree.insert(new Voxel(x, y, z, Colors.RED));
					}
				}
			}
		}

		return octree;
	}
}
