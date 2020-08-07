package me.alexng.untitled;

import me.alexng.untitled.util.Component;
import me.alexng.untitled.util.ComponentWorker;

public class MoveComponent extends Component {

	@Override
	public void update(ComponentWorker componentWorker) {
		PositionComponent positionComponent = componentWorker.getComponent(PositionComponent.class);
		positionComponent.setX(positionComponent.getX() + 1);
	}
	
}
