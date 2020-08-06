package me.alexng.untitled;

// TODO: Please rename me
public abstract class ComponentWorker {
	public abstract void addComponent(Component c);	
	public abstract <T extends Component> T removeComponent(Class<T> c);
	public abstract void removeComponent(Component c);
	public abstract void removeComponents(Class<?> clazz);
	public abstract <T extends Component> T getComponent(Class<T> c);
	public abstract void update();
}
