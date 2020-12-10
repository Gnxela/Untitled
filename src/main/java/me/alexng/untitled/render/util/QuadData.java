package me.alexng.untitled.render.util;

/**
 * Contains vertex and index data for quads.
 * quadXZ gives a unit quad lying flat on the x and z planes.
 */
public class QuadData {

	public static float[] quadXZVertexData = new float[]{
			// position
			0, 0, 0,
			1, 0, 0,
			1, 0, 1,
			0, 0, 1
	};

	public static int[] indices = new int[]{
			0, 1, 3,
			1, 2, 3,
	};
}
