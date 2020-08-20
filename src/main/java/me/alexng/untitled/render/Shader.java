package me.alexng.untitled.render;

import com.google.common.io.ByteStreams;
import me.alexng.untitled.render.exceptions.ShaderException;

import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader implements Cleanable {

	private static final String FRAG_SUFFIX = ".frag";
	private static final String VERT_SUFFIX = ".vert";

	private final String sourcePath;
	private final boolean isFragment;
	private final int handle;
	private boolean loaded;

	public Shader(String resourcePath) throws ShaderException {
		checkPath(resourcePath);
		this.sourcePath = resourcePath;
		this.isFragment = resourcePath.endsWith(".frag");
		this.handle = glCreateShader(isFragment ? GL_FRAGMENT_SHADER : GL_VERTEX_SHADER);
	}

	public void load() throws IOException, ShaderException {
		if (loaded) {
			throw new ShaderException(sourcePath + " already loaded");
		}
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sourcePath);
		if (inputStream == null) {
			cleanup();
			throw new ShaderException(sourcePath + " not found");
		}
		glShaderSource(handle, new String(ByteStreams.toByteArray(inputStream)));
		glCompileShader(handle);
		if (glGetShaderi(handle, GL_COMPILE_STATUS) == GL_FALSE) {
			String log = glGetShaderInfoLog(handle);
			cleanup();
			throw new ShaderException(sourcePath + " failed to compile: " + log);
		}
		System.out.println("Loaded: " + sourcePath);
		loaded = true;
	}

	private void checkPath(String sourcePath) throws ShaderException {
		if (!sourcePath.endsWith(FRAG_SUFFIX) && !sourcePath.endsWith(VERT_SUFFIX)) {
			throw new ShaderException(String.format("Invalid name '%s'", sourcePath));
		}
	}

	public boolean isFragment() {
		return isFragment;
	}

	public boolean isVertex() {
		return !isFragment;
	}

	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public void cleanup() {
		glDeleteShader(handle);
	}

	public int getHandle() {
		return handle;
	}
}
