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
	public void rasterize(VoxelStore store) {
		// 3D Scan-Conversion Algorithms for Voxel-Based Graphics by Arie Kaufman and Eyal Shimony.
		int x = start.x(), xDelta = end.x() - start.x();
		int y = start.y(), yDelta = Math.abs(end.y() - start.y()), ysign = (end.y() - start.y()) / yDelta;
		int dy = 2 * yDelta - xDelta, yinc1 = 2 * yDelta, yinc2 = 2 * (yDelta - xDelta);
		int z = start.z(), zDelta = Math.abs(end.z() - start.z()), zsign = (end.z() - start.z()) / zDelta;
		int dz = 2 * zDelta - xDelta, zinc1 = 2 * zDelta, zinc2 = 2 * (zDelta - xDelta);
		while (x < end.x()) {
			x++;
			if (dy < 0) {
				dy += yinc1;
			} else {
				dy += yinc2;
				y += ysign;
			}
			if (dz < 0) {
				dz += zinc1;
			} else {
				dz += yinc2;
				z += zsign;
			}

			try {
				store.set(new Voxel(x, y, z, ConversionUtil.rgbIntToVector3f(color)));
			} catch (VoxelStoreException e) {
				e.printStackTrace();
			}
		}
	}
}
