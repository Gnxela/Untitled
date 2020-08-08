package me.alexng.untitled.render;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window implements Cleanable {

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

		try (MemoryStack memoryStack = MemoryStack.stackPush()) {
			IntBuffer bWidth = memoryStack.mallocInt(1);
			IntBuffer bHeight = memoryStack.mallocInt(1);
			glfwGetWindowSize(windowHandler, bWidth, bHeight);
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(windowHandler, (vidMode.width() - bWidth.get(0)) / 2, (vidMode.height() - bHeight.get(0)) / 2);
		}

		glfwSetKeyCallback(windowHandler, (window, key, scanCode, action, mode) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(windowHandler, true);
			}
		});

		glfwMakeContextCurrent(windowHandler);
		createCapabilities();
		// TODO: Do we want this?
		glfwSwapInterval(1);
		return new Window(windowHandler);
	}

	public void update() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glfwSwapBuffers(windowHandler);
		glfwPollEvents();
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(windowHandler);
	}

	public void hide() {
		glfwHideWindow(windowHandler);
	}

	public void show() {
		glfwShowWindow(windowHandler);
	}

	@Override
	public void cleanup() {
		glfwFreeCallbacks(windowHandler);
		glfwDestroyWindow(windowHandler);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}
