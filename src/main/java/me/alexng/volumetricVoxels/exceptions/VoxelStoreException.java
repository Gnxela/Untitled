package me.alexng.volumetricVoxels.exceptions;

public class VoxelStoreException extends VolumetricVoxelsException {
	public VoxelStoreException() {
	}

	public VoxelStoreException(String message) {
		super(message);
	}

	public VoxelStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public VoxelStoreException(Throwable cause) {
		super(cause);
	}
}
