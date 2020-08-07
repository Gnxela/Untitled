package me.alexng.untitled.util;

public class Component {
	
	public void update(ComponentWorker componentWorker) {
		
	}
	
	public boolean matches(Class<?> c) {
		return getClass().isAssignableFrom(c);
	}
}
