package me.alexng.untitled.exceptions;

public class TextureException extends UntitledException {
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
