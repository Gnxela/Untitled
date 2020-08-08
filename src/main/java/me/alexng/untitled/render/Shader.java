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
	private int handler;

	public Shader(String sourcePath) throws ShaderException {
		checkPath(sourcePath);
		this.sourcePath = sourcePath;
		this.isFragment = sourcePath.endsWith(".frag");
	}

	public void load() throws IOException, ShaderException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sourcePath);
		if (inputStream == null) {
			cleanup();
			throw new ShaderException(sourcePath + " not found.");
		}
		String source = new String(ByteStreams.toByteArray(inputStream));
		System.out.println(source);
		handler = glCreateShader(isFragment ? GL_FRAGMENT_SHADER : GL_VERTEX_SHADER);
		glShaderSource(handler, source);
		glCompileShader(handler);
		if (glGetShaderi(handler, GL_COMPILE_STATUS) == GL_FALSE) {
			String log = glGetShaderInfoLog(handler);
			cleanup();
			throw new ShaderException(sourcePath + " failed to compile: " + log);
		}
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

	@Override
	public void cleanup() {
		glDeleteShader(handler);
	}

	public int getHandler() {
		return handler;
	}
}
