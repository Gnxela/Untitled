package me.alexng.untitled;

import me.alexng.untitled.render.Cleanable;
import me.alexng.untitled.render.Shader;
import me.alexng.untitled.render.exceptions.ShaderException;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements Cleanable {

	private final int handle;

	public ShaderProgram() {
		this.handle = glCreateProgram();
	}

	public void attachShader(Shader shader) throws IOException, ShaderException {
		if (!shader.isLoaded()) {
			shader.load();
		}
		glAttachShader(handle, shader.getHandle());
	}

	public void linkProgram() {
		glLinkProgram(handle);
	}

	public int getHandle() {
		return handle;
	}

	@Override
	public void cleanup() {
		glDeleteProgram(handle);
	}
}
