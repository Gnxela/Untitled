package me.alexng.volumetricVoxels.exceptions;

public class ShaderException extends VolumetricVoxelsException {
	public ShaderException() {
	}

	public ShaderException(String message) {
		super(message);
	}

	public ShaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShaderException(Throwable cause) {
		super(cause);
	}
}
