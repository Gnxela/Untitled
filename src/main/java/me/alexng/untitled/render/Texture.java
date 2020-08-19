package me.alexng.untitled.render;

import me.alexng.untitled.Main;
import me.alexng.untitled.render.exceptions.TextureException;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements Cleanable {

	private final String resourcePath;
	private final boolean transparent;
	private final int handle;
	private boolean loaded, isPacked;

	public Texture(String resourcePath, boolean transparent) {
		this.resourcePath = resourcePath;
		this.transparent = transparent;
		this.handle = glGenTextures();
	}

	public Texture(String resourcePath, boolean transparent, boolean isPacked) {
		this.resourcePath = resourcePath;
		this.transparent = transparent;
		this.isPacked = isPacked;
		this.handle = glGenTextures();
	}

	public Texture(String resourcePath) {
		this(resourcePath, false, true);
	}

	public void load() throws TextureException {
		if (loaded) {
			return;
		}
		String absolutePath = Main.class.getClassLoader().getResource(resourcePath).getPath().substring(1);
		if (!System.getProperty("os.name").contains("Windows")) { // TODO Language/region agnostic value for 'Windows' ?
			// stbi_load requires a file system path, NOT a classpath resource path
			absolutePath = File.separator + absolutePath;
		}
		stbi_set_flip_vertically_on_load(true);
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = stbi_load(absolutePath, width, height, channels, 0);
		if (image == null) {
			throw new TextureException("Could not decode image file " + resourcePath + ": " + STBImage.stbi_failure_reason());
		}
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		// glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		if (isPacked) {
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		}
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, transparent ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, image);
		glGenerateMipmap(GL_TEXTURE_2D);
		stbi_image_free(image);
		loaded = true;
	}

	public void bind(int textureUnit) throws TextureException {
		if (textureUnit < 0 || textureUnit > 31) {
			throw new IllegalArgumentException("Invalid texture unit: " + textureUnit);
		}
		glActiveTexture(GL_TEXTURE0 + textureUnit);
		glBindTexture(GL_TEXTURE_2D, handle);
	}

	public void bind() throws TextureException {
		bind(0);
	}

	@Override
	public void cleanup() {
		glDeleteTextures(handle);
	}

	public int getHandle() {
		return handle;
	}
}
