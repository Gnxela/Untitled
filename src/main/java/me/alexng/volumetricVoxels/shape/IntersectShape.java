package me.alexng.volumetricVoxels.shape;

import org.joml.Vector3fc;

public class IntersectShape implements Shape {

	private final Shape shape1;
	private final Shape shape2;

	public IntersectShape(Shape shape1, Shape shape2) {
		this.shape1 = shape1;
		this.shape2 = shape2;
	}

	@Override
	public float signedDistance(Vector3fc point) {
		return Math.max(shape1.signedDistance(point), shape2.signedDistance(point));
	}
}
