package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.shape.Shape;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Arrays;

// TODO: Rename me!
public class Object {

	private final Shape[] shapes;
	private final Vector3f size;

	public Object(Vector3fc size, Shape[] shapes) {
		this.size = new Vector3f(size);
		this.shapes = shapes;
	}

	public boolean containsPoint(Vector3fc point) {
		return Arrays.stream(shapes).anyMatch(s -> s.signedDistance(point) <= 0);
	}

	public Vector3f getSize() {
		return size;
	}
}
