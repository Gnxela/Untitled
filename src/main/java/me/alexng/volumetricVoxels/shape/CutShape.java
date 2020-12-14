package me.alexng.volumetricVoxels.shape;

import org.joml.Vector3fc;

public class CutShape implements Shape {

	private final Shape positiveShape;
	private final Shape negativeShape;

	public CutShape(Shape positiveShape, Shape negativeShape) {
		this.positiveShape = positiveShape;
		this.negativeShape = negativeShape;
	}

	@Override
	public float signedDistance(Vector3fc point) {
		return Math.max(positiveShape.signedDistance(point), -negativeShape.signedDistance(point));
	}
}
