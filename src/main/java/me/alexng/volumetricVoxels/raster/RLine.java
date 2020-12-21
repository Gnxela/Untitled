package me.alexng.volumetricVoxels.raster;

import me.alexng.volumetricVoxels.Voxel;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import me.alexng.volumetricVoxels.storage.VoxelStore;
import me.alexng.volumetricVoxels.util.ConversionUtil;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public class RLine implements Rasterable {

	private Vector3ic start, end;
	private int color;

	private RLine(Vector3ic start, Vector3ic end, int color) {
		this.start = start;
		this.end = end;
		this.color = color;
	}

	public static RLine floor(Vector3f start, Vector3f end, int color) {
		return new RLine(new Vector3i((int) start.x, (int) start.y, (int) start.z), new Vector3i((int) end.x, (int) end.y, (int) end.z), color);
	}

	@Override
	public void rasterize(VoxelStore store) throws VoxelStoreException {
		// 3D Scan-Conversion Algorithms for Voxel-Based Graphics by Arie Kaufman and Eyal Shimony.
		if (end.equals(start.x(), start.y(), start.z())) {
			return;
		}
		if (start.x() > end.x()) { // Swap the vectors so that start.x < end.x
			System.out.println("asd");
			Vector3ic temp = start;
			start = end;
			end = temp;
		}
		Vector3f c = ConversionUtil.rgbIntToVector3f(color);
		int x = start.x(), xDelta = Math.abs(end.x() - start.x()), xsign = (end.x() - start.x()) / (xDelta == 0 ? 1 : xDelta);
		int y = start.y(), yDelta = Math.abs(end.y() - start.y()), ysign = (end.y() - start.y()) / (yDelta == 0 ? 1 : yDelta);
		int z = start.z(), zDelta = Math.abs(end.z() - start.z()), zsign = (end.z() - start.z()) / (zDelta == 0 ? 1 : zDelta);
		int current = 0, end = Math.max(Math.max(xDelta, yDelta), zDelta);
		int dx = 2 * xDelta - end, xinc1 = 2 * xDelta, xinc2 = 2 * (xDelta - end);
		int dy = 2 * yDelta - end, yinc1 = 2 * yDelta, yinc2 = 2 * (yDelta - end);
		int dz = 2 * zDelta - end, zinc1 = 2 * zDelta, zinc2 = 2 * (zDelta - end);
		store.set(new Voxel(start.x(), start.y(), start.z(), c));
		while (current < end) {
			current++;

			if (dx < 0) {
				dx += xinc1;
			} else {
				dx += xinc2;
				x += xsign;
			}
			if (dy < 0) {
				dy += yinc1;
			} else {
				dy += yinc2;
				y += ysign;
			}
			if (dz < 0) {
				dz += zinc1;
			} else {
				dz += zinc2;
				z += zsign;
			}

			store.set(new Voxel(x, y, z, c));
		}
	}
}
