package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.raster.Rasterable;
import me.alexng.volumetricVoxels.shape.Shape;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * A reusable template that creates raster input for a shape/model/object.
 * This object exists in object space while raster output exists in voxel space. A transformation matrix is applied when creating raster input.
 */
public class ObjectTemplate {

	private final Shape[] shapes;
	private final Vector3f size;

	public ObjectTemplate(Vector3fc size, Shape[] shapes) {
		this.size = new Vector3f(size);
		this.shapes = shapes;
	}

	public Rasterable[] createRasterInput(Matrix4f model) {
		Rasterable[] rasterInput = new Rasterable[shapes.length]; // Assuming every shape is one raster entry
		for (int i = 0; i < shapes.length; i++) {
			rasterInput[i] = shapes[i].toRasterInput(model);
		}
		return rasterInput;
	}

	public Vector3f getSize() {
		return size;
	}
}
