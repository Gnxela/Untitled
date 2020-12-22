package me.alexng.volumetricVoxels.util;

import org.joml.Vector3f;

import java.awt.*;

public class ConversionUtil {

	public static Vector3f rgbIntToVector3f(int rgb) {
		return new Vector3f(((rgb & 0xFF0000) >> 16) / 255f, ((rgb & 0xFF00) >> 8) / 255f, (rgb & 0xFF) / 255f);
	}

	/**
	 * @param h [0, 1]
	 * @param s [0, 1]
	 * @param v [0, 1]
	 */
	public static int hsvToRgb(float h, float s, float v) {
		return Color.HSBtoRGB(h, s, v);
	}
}
