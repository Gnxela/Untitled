package me.alexng.volumetricVoxels.shape;

import me.alexng.volumetricVoxels.raster.RLine;
import me.alexng.volumetricVoxels.raster.RPolygon;
import me.alexng.volumetricVoxels.raster.Rasterable;
import org.joml.Matrix4f;

public class Polygon extends Shape {

	// Lines that define the polygon and that reside on the plane defined below. Lines must be start/end linked. l1.end = l2.start, l2.end = l3.start, ..., ln.end = l1.start.
	private Line[] lines;
	// The 3 coefficients and constant of a plane. Ax + By + Cz + D = 0
	private float[] plane;
	private boolean filled;

	public Polygon(int color, Line[] lines, float[] plane, boolean filled) {
		super(color);
		this.lines = lines;
		this.plane = plane;
		this.filled = filled;
	}

	@Override
	public Rasterable toRasterInput(Matrix4f model) {
		RLine[] rlines = new RLine[lines.length];
		for (int i = 0; i < lines.length; i++) {
			rlines[i] = lines[i].toRasterInput(model);
		}
		return new RPolygon(rlines, plane, color, filled);
	}
}
