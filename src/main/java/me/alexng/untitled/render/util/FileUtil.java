package me.alexng.untitled.render.util;

import java.io.InputStream;
import java.util.Objects;

public class FileUtil {

	public static InputStream getResourceAsStream(String resourcePath) {
		return FileUtil.class.getClassLoader().getResourceAsStream(resourcePath);
	}

	public static String getAbsolutePath(String resourcePath) {
		return Objects.requireNonNull(FileUtil.class.getClassLoader().getResource(resourcePath)).getPath().substring(1);
	}
}
