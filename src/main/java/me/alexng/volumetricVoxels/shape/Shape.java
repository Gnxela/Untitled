package me.alexng.volumetricVoxels.shape;

import org.joml.Vector3fc;

public interface Shape {

	float signedDistance(Vector3fc point);
}
