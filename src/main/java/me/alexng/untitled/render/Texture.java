package me.alexng.untitled.render;

import me.alexng.untitled.render.exceptions.TextureException;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements Cleanable {

	public enum Type {
		NONE(aiTextureType_NONE),
		DIFFUSE(aiTextureType_DIFFUSE),
		SPECULAR(aiTextureType_SPECULAR);

		private int assimpType;

		Type(int assimpType) {
			this.assimpType = assimpType;
		}

		public int getAssimpType() {
			return assimpType;
		}
	}

	private final String absolutePath;
	private final boolean transparent, isPacked;
	private final Type type;
	private final int handle;

	private boolean loaded;

	public Texture(String absolutePath, Type type, boolean transparent, boolean isPacked) {
		if (!System.getProperty("os.name").contains("Windows")) { // TODO Language/region agnostic value for 'Windows' ?
			// stbi_load requires a file system path, NOT a classpath resource path
			this.absolutePath = File.separator + absolutePath;
		} else {
			this.absolutePath = absolutePath;
		}
		this.type = type;
		this.transparent = transparent;
		this.isPacked = isPacked;
		this.handle = glGenTextures();
	}

	public Texture(String resourcePath, Type type) {
		this(resourcePath, type, false, false);
	}

	public Texture(String resourcePath, Type type, boolean transparent) {
		this(resourcePath, type, transparent, false);
	}

	public Texture(String resourcePath, boolean transparent) {
		this(resourcePath, Type.NONE, transparent, false);
	}

	public Texture(String resourcePath) {
		this(resourcePath, Type.NONE, false, true);
	}

	public void load() throws TextureException {
		if (loaded) {
			return;
		}
		stbi_set_flip_vertically_on_load(true);
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = stbi_load(absolutePath, width, height, channels, 0);
		if (image == null) {
			throw new TextureException("Could not decode image file " + absolutePath + ": " + STBImage.stbi_failure_reason());
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
		if (!transparent && channels.get(0) > 3) {
			System.err.println("Loaded texture with more than three channels but not transparent");
		}
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, transparent ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, image);
		glGenerateMipmap(GL_TEXTURE_2D);
		stbi_image_free(image);
		loaded = true;
	}

	public void bind(int textureUnit) {
		if (textureUnit < 0 || textureUnit > 31) {
			throw new IllegalArgumentException("Invalid texture unit: " + textureUnit);
		}
		glActiveTexture(GL_TEXTURE0 + textureUnit);
		glBindTexture(GL_TEXTURE_2D, handle);
	}

	public Type getType() {
		return type;
	}

	public void bind() throws TextureException {
		bind(0);
	}

	@Override
	public void cleanup() {
		glDeleteTextures(handle);
		loaded = false;
	}

	public int getHandle() {
		return handle;
	}
}
