package me.alexng.untitled;

public class MoveComponent extends Component {

	@Override
	public void update(ComponentWorker componentWorker) {
		PositionComponent positionComponent = componentWorker.getComponent(PositionComponent.class);
		positionComponent.setX(positionComponent.getX() + 1);
	}
	
}
