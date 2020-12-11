package me.alexng.volumetricVoxels.exceptions;

public class VolumetricVoxelsException extends Exception {
	public VolumetricVoxelsException() {
	}

	public VolumetricVoxelsException(String message) {
		super(message);
	}

	public VolumetricVoxelsException(String message, Throwable cause) {
		super(message, cause);
	}

	public VolumetricVoxelsException(Throwable cause) {
		super(cause);
	}
}
