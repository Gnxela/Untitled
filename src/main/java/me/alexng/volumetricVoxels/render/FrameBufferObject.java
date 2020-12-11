package me.alexng.volumetricVoxels.render;

import static org.lwjgl.opengl.ARBFramebufferObject.glDeleteFramebuffers;
import static org.lwjgl.opengl.ARBFramebufferObject.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.*;

/**
 * TODO: Rewrite this class once I have a better understanding of everything frame buffers can be used for. Currently only focusing on my one use case
 */
public class FrameBufferObject implements Cleanable {

	private final int handle;

	public FrameBufferObject() {
		this.handle = glGenFramebuffers();
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, handle);
	}

	public void bindToTexture(int textureHandle) {
		bind();
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureHandle, 0);
	}

	public static void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	@Override
	public void cleanup() {
		glDeleteFramebuffers(handle);
	}

	public int getHandle() {
		return handle;
	}
}
