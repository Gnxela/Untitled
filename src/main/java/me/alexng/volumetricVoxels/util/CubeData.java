package me.alexng.volumetricVoxels.util;

public class CubeData {

	public static float[] linesVertexData = {
			// positions
			0f, 0f, 0f,
			1f, 0f, 0f,
			0f, 1f, 0f,
			1f, 1f, 0f,
			0f, 0f, 1f,
			1f, 0f, 1f,
			0f, 1f, 1f,
			1f, 1f, 1f,
	};

	public static int[] linesIndexData = {
			0, 1,
			0, 2,
			0, 4,
			1, 3,
			1, 5,
			2, 3,
			2, 6,
			3, 7,
			4, 6,
			4, 5,
			5, 7,
			6, 7,
	};

	public static float[] vertexDataPosition = {
			// positions
			1f, 1f, 1f,
			0f, 1f, 1f,
			0f, 0f, 1f,
			0f, 0f, 1f,
			1f, 0f, 1f,
			1f, 1f, 1f,
			1f, 1f, 0f,
			0f, 1f, 0f,
			0f, 0f, 0f,
			0f, 0f, 0f,
			1f, 0f, 0f,
			1f, 1f, 0f,
			1f, 0f, 0f,
			1f, 0f, 1f,
			1f, 1f, 1f,
			1f, 1f, 1f,
			1f, 1f, 0f,
			1f, 0f, 0f,
			0f, 0f, 0f,
			0f, 0f, 1f,
			0f, 1f, 1f,
			0f, 1f, 1f,
			0f, 1f, 0f,
			0f, 0f, 0f,
			1f, 1f, 1f,
			0f, 1f, 1f,
			0f, 1f, 0f,
			0f, 1f, 0f,
			1f, 1f, 0f,
			1f, 1f, 1f,
			1f, 0f, 1f,
			0f, 0f, 1f,
			0f, 0f, 0f,
			0f, 0f, 0f,
			1f, 0f, 0f,
			1f, 0f, 1f,
	};

	// TODO: Can we send these as ints?
	// TODO: Reduce the data here. We don't need 32 elements to just store positions

	public static float[] vertexDataPositionNormal = {
			// positions // normals
			1f, 1f, 1f, 0.0f, 0.0f, -1.0f,
			0f, 1f, 1f, 0.0f, 0.0f, -1.0f,
			0f, 0f, 1f, 0.0f, 0.0f, -1.0f,
			0f, 0f, 1f, 0.0f, 0.0f, -1.0f,
			1f, 0f, 1f, 0.0f, 0.0f, -1.0f,
			1f, 1f, 1f, 0.0f, 0.0f, -1.0f,
			1f, 1f, 0f, 0.0f, 0.0f, 1.0f,
			0f, 1f, 0f, 0.0f, 0.0f, 1.0f,
			0f, 0f, 0f, 0.0f, 0.0f, 1.0f,
			0f, 0f, 0f, 0.0f, 0.0f, 1.0f,
			1f, 0f, 0f, 0.0f, 0.0f, 1.0f,
			1f, 1f, 0f, 0.0f, 0.0f, 1.0f,
			1f, 0f, 0f, -1.0f, 0.0f, 0.0f,
			1f, 0f, 1f, -1.0f, 0.0f, 0.0f,
			1f, 1f, 1f, -1.0f, 0.0f, 0.0f,
			1f, 1f, 1f, -1.0f, 0.0f, 0.0f,
			1f, 1f, 0f, -1.0f, 0.0f, 0.0f,
			1f, 0f, 0f, -1.0f, 0.0f, 0.0f,
			0f, 0f, 0f, 1.0f, 0.0f, 0.0f,
			0f, 0f, 1f, 1.0f, 0.0f, 0.0f,
			0f, 1f, 1f, 1.0f, 0.0f, 0.0f,
			0f, 1f, 1f, 1.0f, 0.0f, 0.0f,
			0f, 1f, 0f, 1.0f, 0.0f, 0.0f,
			0f, 0f, 0f, 1.0f, 0.0f, 0.0f,
			1f, 1f, 1f, 0.0f, -1.0f, 0.0f,
			0f, 1f, 1f, 0.0f, -1.0f, 0.0f,
			0f, 1f, 0f, 0.0f, -1.0f, 0.0f,
			0f, 1f, 0f, 0.0f, -1.0f, 0.0f,
			1f, 1f, 0f, 0.0f, -1.0f, 0.0f,
			1f, 1f, 1f, 0.0f, -1.0f, 0.0f,
			1f, 0f, 1f, 0.0f, 1.0f, 0.0f,
			0f, 0f, 1f, 0.0f, 1.0f, 0.0f,
			0f, 0f, 0f, 0.0f, 1.0f, 0.0f,
			0f, 0f, 0f, 0.0f, 1.0f, 0.0f,
			1f, 0f, 0f, 0.0f, 1.0f, 0.0f,
			1f, 0f, 1f, 0.0f, 1.0f, 0.0f,
	};

	public static float[] vertexDataPositionNormalTexture = {
			// positions // normals // texture coords
			1f, 1f, 1f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
			0f, 1f, 1f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
			0f, 0f, 1f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
			0f, 0f, 1f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
			1f, 0f, 1f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
			1f, 1f, 1f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
			1f, 1f, 0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
			0f, 1f, 0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
			0f, 0f, 0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
			0f, 0f, 0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
			1f, 0f, 0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
			1f, 1f, 0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
			1f, 0f, 0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			1f, 0f, 1f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			1f, 1f, 1f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			1f, 1f, 1f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			1f, 1f, 0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			1f, 0f, 0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			0f, 0f, 0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			0f, 0f, 1f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			0f, 1f, 1f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			0f, 1f, 1f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			0f, 1f, 0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			0f, 0f, 0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
			1f, 1f, 1f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
			0f, 1f, 1f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
			0f, 1f, 0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
			0f, 1f, 0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
			1f, 1f, 0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
			1f, 1f, 1f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
			1f, 0f, 1f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
			0f, 0f, 1f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
			0f, 0f, 0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
			0f, 0f, 0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
			1f, 0f, 0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
			1f, 0f, 1f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
	};

	public static int[] indexData = {
			0, 1, 2, 3, 4, 5,
			6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 17,
			18, 19, 20, 21, 22, 23,
			24, 25, 26, 27, 28, 29,
			30, 31, 32, 33, 34, 35
	};
}