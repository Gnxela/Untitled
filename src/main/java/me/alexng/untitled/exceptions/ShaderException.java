package me.alexng.untitled.exceptions;

public class ShaderException extends UntitledException {
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
