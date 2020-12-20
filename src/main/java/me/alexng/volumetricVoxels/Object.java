package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.raster.Rasterable;
import me.alexng.volumetricVoxels.shape.Shape;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

// TODO: Rename me!
public class Object {

	private final Shape[] shapes;
	private final Vector3f size;

	public Object(Vector3fc size, Shape[] shapes) {
		this.size = new Vector3f(size);
		this.shapes = shapes;
	}

	public Rasterable[] createRasterInput(Matrix4f model) {
		Rasterable[] rasterInput = new Rasterable[shapes.length]; // Assuming every shape is one raster entry
		for (int i = 0; i < shapes.length; i++) {
			rasterInput[i] = shapes[i].toRasterInput();
		}
		return rasterInput;
	}

	public Vector3f getSize() {
		return size;
	}
}
