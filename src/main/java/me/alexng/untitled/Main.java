package me.alexng.untitled;

public class Main {

	public static void main(String[] args) {
		ComponentWorker worker = new ComponentListWorker();
		PositionComponent pos = new PositionComponent();
		worker.addComponent(pos);
		worker.addComponent(new MoveComponent());
		while (pos.getX() < 20) {
			worker.update();
		}
		worker.removeComponent(MoveComponent.class);
		worker.update();
		worker.update();
		worker.update();
	}
	
}
