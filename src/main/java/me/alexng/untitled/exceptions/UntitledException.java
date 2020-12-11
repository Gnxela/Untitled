package me.alexng.untitled.exceptions;

public class UntitledException extends Exception {
	public UntitledException() {
	}

	public UntitledException(String message) {
		super(message);
	}

	public UntitledException(String message, Throwable cause) {
		super(message, cause);
	}

	public UntitledException(Throwable cause) {
		super(cause);
	}
}
