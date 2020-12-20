package me.alexng.volumetricVoxels.shape;

import me.alexng.volumetricVoxels.raster.RLine;
import org.joml.Vector3f;

public class Line extends Shape {

	private Vector3f start, end;

	public Line(int color, Vector3f start, Vector3f end) {
		super(color);
		this.start = start;
		this.end = end;
	}

	@Override
	public RLine toRasterInput() {
		return RLine.floor(start, end, color);
	}

	public Vector3f getStart() {
		return start;
	}

	public void setStart(Vector3f start) {
		this.start = start;
	}

	public Vector3f getEnd() {
		return end;
	}

	public void setEnd(Vector3f end) {
		this.end = end;
	}
}
