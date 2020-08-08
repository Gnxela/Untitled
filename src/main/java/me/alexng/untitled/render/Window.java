package me.alexng.untitled.render;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {

	private long windowHandler;

	public Window(long windowHandler) {
		this.windowHandler = windowHandler;
	}

	public static Window create(int width, int height, String title) {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new RuntimeException("Unable to initialize GLFW");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		long windowHandler = glfwCreateWindow(width, height, title, NULL, NULL);
		if (windowHandler == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwMakeContextCurrent(windowHandler);
		// TODO: Do we want this?
		glfwSwapInterval(1);

		return new Window(windowHandler);
	}

	public void hide() {
		glfwHideWindow(windowHandler);
	}

	public void show() {
		glfwShowWindow(windowHandler);
	}
}
