package me.alexng.volumetricVoxels.raster;

import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import me.alexng.volumetricVoxels.storage.VoxelStore;

public class RPolygon implements Rasterable {

	// Lines that define the polygon and that reside on the plane defined below. Lines must be start/end linked. l1.end = l2.start, l2.end = l3.start, ..., ln.end = l1.start.
	private RLine[] lines;
	// The 3 coefficients and constant of a plane. Ax + By + Cz + D = 0
	private float[] plane;
	private int color;
	private boolean filled;

	public RPolygon(RLine[] lines, float[] plane, int color, boolean filled) {
		this.lines = lines;
		this.plane = plane;
		this.color = color;
		this.filled = filled;
	}

	@Override
	public void rasterize(VoxelStore store) throws VoxelStoreException {
		for (RLine line : lines) {
			line.rasterize(store);
		}
	}
}
