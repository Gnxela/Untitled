package me.alexng.untitled.render.shader;

import me.alexng.untitled.exceptions.ShaderException;
import me.alexng.untitled.render.Cleanable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements Cleanable {

	private final int handle;

	public ShaderProgram(String vertexShaderResourcePath, String fragmentShaderResourcePath) throws ShaderException {
		this.handle = glCreateProgram();
		attachShader(new Shader(vertexShaderResourcePath));
		attachShader(new Shader(fragmentShaderResourcePath));
		linkProgram();
	}

	private void attachShader(Shader shader) throws ShaderException {
		if (!shader.isLoaded()) {
			shader.load();
		}
		glAttachShader(handle, shader.getHandle());
		shader.cleanup();
	}

	private void linkProgram() throws ShaderException {
		glLinkProgram(handle);
		if (glGetProgrami(handle, GL_LINK_STATUS) == GL_FALSE) {
			String log = glGetProgramInfoLog(handle);
			throw new ShaderException("Unable to link shaders: " + log);
		}
	}

	public void use() {
		glUseProgram(handle);
	}

	public void setBool(SID shaderIdentifier, boolean value) {
		glUniform1i(glGetUniformLocation(handle, shaderIdentifier.getGlslName()), value ? 1 : 0);
	}

	public void setInt(SID shaderIdentifier, int value) {
		setInt(shaderIdentifier.getGlslName(), value);
	}

	// TODO: Maybe remove this?
	public void setInt(String name, int value) {
		glUniform1i(glGetUniformLocation(handle, name), value);
	}

	public void setFloat(SID shaderIdentifier, float value) {
		glUniform1f(glGetUniformLocation(handle, shaderIdentifier.getGlslName()), value);
	}

	public void setMatrix4f(SID shaderIdentifier, Matrix4f transform) {
		try (MemoryStack memoryStack = MemoryStack.stackPush()) {
			setMatrix4f(shaderIdentifier, transform.get(memoryStack.mallocFloat(16)));
		}
	}

	public void setMatrix4f(SID shaderIdentifier, FloatBuffer buffer) {
		glUniformMatrix4fv(glGetUniformLocation(handle, shaderIdentifier.getGlslName()), false, buffer);
	}

	public void setVec2f(SID shaderIdentifier, int x, float y) {
		glUniform2f(glGetUniformLocation(handle, shaderIdentifier.getGlslName()), x, y);
	}

	public void setVec3f(SID shaderIdentifier, float x, float y, float z) {
		glUniform3f(glGetUniformLocation(handle, shaderIdentifier.getGlslName()), x, y, z);
	}

	public void setVec3f(SID shaderIdentifier, float i) {
		glUniform3f(glGetUniformLocation(handle, shaderIdentifier.getGlslName()), i, i, i);
	}

	public void setVec3f(SID shaderIdentifier, Vector3f v) {
		setVec3f(shaderIdentifier, v.x, v.y, v.z);
	}

	public void setVec4f(SID shaderIdentifier, float x, float y, float z, float w) {
		glUniform4f(glGetUniformLocation(handle, shaderIdentifier.getGlslName()), x, y, z, w);
	}

	public int getHandle() {
		return handle;
	}

	@Override
	public void cleanup() {
		glDeleteProgram(handle);
	}
}
