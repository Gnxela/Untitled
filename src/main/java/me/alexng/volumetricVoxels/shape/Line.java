package me.alexng.volumetricVoxels.shape;

import me.alexng.volumetricVoxels.raster.RLine;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;

public class Line extends Shape {

	private Vector3f start, end;

	public Line(int color, Vector3fc start, Vector3fc end) {
		super(color);
		this.start = new Vector3f(start);
		this.end = new Vector3f(end);
	}

	@Override
	public RLine toRasterInput(Matrix4f model) {
		return RLine.floor(new Vector4f(start, 1).mul(model), new Vector4f(end, 1).mul(model), color);
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
