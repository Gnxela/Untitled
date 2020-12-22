package me.alexng.volumetricVoxels.util;

import org.joml.Vector3f;

public class ConversionUtil {

	public static Vector3f rgbIntToVector3f(int rgb) {
		return new Vector3f(((rgb & 0xFF0000) >> 16) / 255f, ((rgb & 0xFF00) >> 8) / 255f, (rgb & 0xFF) / 255f);
	}
}