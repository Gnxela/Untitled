package me.alexng.volumetricVoxels;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public class Voxel {

	private final Vector3i position;
	private final Vector3f color;

	public Voxel(int x, int y, int z, Vector3f color) {
		this.position = new Vector3i(x, y, z);
		this.color = color;
	}

	public Vector3ic getPosition() {
		return position;
	}

	public Vector3f getColor() {
		return color;
	}
}
