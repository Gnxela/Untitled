package me.alexng.untitled.render.util;

import com.google.common.io.ByteStreams;
import me.alexng.untitled.render.exceptions.ShaderException;

import java.io.IOException;
import java.io.InputStream;

public class ShaderLoader {

	public static final String INCLUDE_PREFIX = "#include ";

	public static String load(String resourcePath) throws ShaderException, IOException {
		InputStream inputStream = FileUtil.getResourceAsStream(resourcePath);
		if (inputStream == null) {
			throw new ShaderException(resourcePath + " not found");
		}
		String source = new String(ByteStreams.toByteArray(inputStream));
		inputStream.close();
		String[] lines = source.split("\\r?\\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (!line.startsWith(INCLUDE_PREFIX)) {
				continue;
			}
			String includedPath = line.substring(INCLUDE_PREFIX.length() + 1, line.lastIndexOf('"'));
			lines[i] = load(includedPath);
		}
		return String.join("\n", lines);
	}
}
