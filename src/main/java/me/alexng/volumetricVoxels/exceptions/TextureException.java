package me.alexng.volumetricVoxels.exceptions;

public class TextureException extends VolumetricVoxelsException {
	public TextureException() {
	}

	public TextureException(String message) {
		super(message);
	}

	public TextureException(String message, Throwable cause) {
		super(message, cause);
	}

	public TextureException(Throwable cause) {
		super(cause);
	}
}
