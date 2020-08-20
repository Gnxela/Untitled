package me.alexng.untitled.render.exceptions;

public class ModelException extends UntitledException {
	public ModelException() {
	}

	public ModelException(String message) {
		super(message);
	}

	public ModelException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelException(Throwable cause) {
		super(cause);
	}
}
