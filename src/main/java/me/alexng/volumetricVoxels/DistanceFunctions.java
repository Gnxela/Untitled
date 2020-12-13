package me.alexng.volumetricVoxels;

import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * All distance functions are assumed to be signed unless otherwise stated.
 */
public class DistanceFunctions {

	// TODO: Creating and destroying Vectors this quickly is bad. Have a working vector pool? Remove static and have instance working vectors?

	public static final float sphere(Vector3fc point, Vector3fc sphereCenter, float radius) {
		return sphereCenter.sub(point, new Vector3f()).length() - radius;
	}

}
