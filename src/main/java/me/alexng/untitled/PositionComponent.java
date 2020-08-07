package me.alexng.untitled;

import me.alexng.untitled.util.Component;
import me.alexng.untitled.util.ComponentWorker;

public class PositionComponent extends Component {

	private int x, y;
	
	@Override
	public void update(ComponentWorker componentWorker) {
		System.out.println("Pos[" + x + ":" + y + "]");
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
