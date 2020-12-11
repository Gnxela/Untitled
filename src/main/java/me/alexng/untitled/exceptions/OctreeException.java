package me.alexng.untitled.exceptions;

public class OctreeException extends UntitledException {
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
