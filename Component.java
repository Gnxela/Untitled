package me.alexng.untitled;

public class Component {
	
	public void update(ComponentWorker componentWorker) {
		
	}
	
	public boolean matches(Class<?> c) {
		return getClass().isAssignableFrom(c);
	}
}
