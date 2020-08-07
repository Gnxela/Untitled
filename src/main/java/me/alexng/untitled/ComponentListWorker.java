package me.alexng.untitled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ComponentListWorker extends ComponentWorker {

	private List<Component> components;
	
	public ComponentListWorker() {
		this.components = new ArrayList<>();
	}
	
	public void update() {
		for (Component component : components) {
			component.update(this);
		}
	}
	
	public void addComponent(Component component) {
		components.add(component);
	}
	
	public void addComponents(Collection<Component> components) {
		components.addAll(components);
	}

	@Nullable
	public <T extends Component> T removeComponent(Class<T> clazz) {
		boolean found = false;
		int index = -1;
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			if (component.getClass().isAssignableFrom(clazz)) {
				found = true;
				index = i;
			}
		}
		if (found) {
			return clazz.cast(components.remove(index));
		}
		return null;
	}
	
	@Nullable
	public void removeComponents(Class<?> clazz) {
		LinkedList<Integer> indexs = new LinkedList<>();
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			if (component.getClass().isAssignableFrom(clazz)) {
				indexs.add(i);
			}
		}
		for (int i : indexs) {
			components.remove(i);
		}
	}

	public void removeComponent(Component component) {
		components.remove(component);
	}

	@Nullable
	public <T extends Component> T getComponent(Class<T> clazz) {
		return components.stream()
				.filter(c -> c.matches(clazz))
				.map(c -> clazz.cast(c))
				.findFirst().orElse(null);
	}
}
