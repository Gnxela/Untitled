package me.alexng.untitled.util;

import org.joml.Vector3f;

public class ConversionUtil {

	public static Vector3f rgbIntToVector3f(int rgb) {
		return new Vector3f((rgb & 0xFF0000) >> 16, (rgb & 0xFF00) >> 8, rgb & 0xFF);
	}
}
