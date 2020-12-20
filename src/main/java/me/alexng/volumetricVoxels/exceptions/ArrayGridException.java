package me.alexng.volumetricVoxels.exceptions;

public class ArrayGridException extends VoxelStoreException {
	public ArrayGridException() {
	}

	public ArrayGridException(String message) {
		super(message);
	}

	public ArrayGridException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArrayGridException(Throwable cause) {
		super(cause);
	}
}
