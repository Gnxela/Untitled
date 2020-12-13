package me.alexng.volumetricVoxels.shape;

import me.alexng.volumetricVoxels.DistanceFunctions;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class Sphere implements Shape {

	private Vector3f center;
	private float radius;

	public Sphere(Vector3fc center, float radius) {
		this.center = new Vector3f(center);
		this.radius = radius;
	}

	@Override
	public float signedDistance(Vector3fc point) {
		return DistanceFunctions.sphere(point, center, radius);
	}

	public Vector3f getCenter() {
		return center;
	}

	public float getRadius() {
		return radius;
	}
}
