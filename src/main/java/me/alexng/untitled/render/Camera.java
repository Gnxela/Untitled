package me.alexng.untitled.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class Camera implements GLFWCursorPosCallbackI {

	private final Vector3f position, direction, up;
	private float pitch, yaw;
	private boolean firstUpdate = true;
	private double lastMouseX = -1, lastMouseY = -1;

	public Camera(Vector3f position, float pitch, float yaw) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.up = new Vector3f(0.0f, 1.0f, 0.0f);
		this.direction = new Vector3f();
		updateDirection();
	}

	@Override
	public void invoke(long handle, double x, double y) {
		if (firstUpdate) {
			lastMouseX = x;
			lastMouseY = y;
			firstUpdate = false;
		}
		float xOffset = (float) (x - lastMouseX);
		float yOffset = (float) (lastMouseY - y);
		lastMouseX = x;
		lastMouseY = y;
		float sensitivity = 0.1f;
		xOffset *= sensitivity;
		yOffset *= sensitivity;
		yaw += xOffset;
		pitch += yOffset;
		if (pitch > 89.0f)
			pitch = 89.0f;
		if (pitch < -89.0f)
			pitch = -89.0f;

		updateDirection();
	}

	private void updateDirection() {
		Vector3f direction = new Vector3f();
		direction.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		direction.y = (float) Math.sin(Math.toRadians(pitch));
		direction.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		this.direction.set(direction.normalize());
	}

	public Matrix4f createViewMatrix() {
		return new Matrix4f().lookAt(position, position.add(direction, new Vector3f()), up);
	}

	public void processInput(Window window) {
		if (glfwGetKey(window.getHandle(), GLFW_KEY_W) == GLFW_PRESS) {
			position.add(direction);
		}
		if (glfwGetKey(window.getHandle(), GLFW_KEY_S) == GLFW_PRESS) {
			position.sub(direction);
		}
		if (glfwGetKey(window.getHandle(), GLFW_KEY_A) == GLFW_PRESS) {
			position.sub(direction.cross(up, new Vector3f()).normalize());
		}
		if (glfwGetKey(window.getHandle(), GLFW_KEY_D) == GLFW_PRESS) {
			position.add(direction.cross(up, new Vector3f()).normalize());
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public Vector3f getUp() {
		return up;
	}
}
