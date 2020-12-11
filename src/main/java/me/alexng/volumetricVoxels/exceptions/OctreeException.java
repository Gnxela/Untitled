package me.alexng.volumetricVoxels.exceptions;

public class OctreeException extends VolumetricVoxelsException {
	public OctreeException() {
	}

	public OctreeException(String message) {
		super(message);
	}

	public OctreeException(String message, Throwable cause) {
		super(message, cause);
	}

	public OctreeException(Throwable cause) {
		super(cause);
	}
}
