package me.alexng.test;

import me.alexng.volumetricVoxels.Entity;
import me.alexng.volumetricVoxels.Object;
import me.alexng.volumetricVoxels.Tessellater;
import me.alexng.volumetricVoxels.VolumetricVoxels;
import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import me.alexng.volumetricVoxels.raster.Rasterizer;
import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.shape.Line;
import me.alexng.volumetricVoxels.shape.Polygon;
import me.alexng.volumetricVoxels.shape.Shape;
import me.alexng.volumetricVoxels.storage.Octree;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Main {

	public static void main(String[] args) throws VoxelStoreException, ShaderException, TextureException {
		VolumetricVoxels vv = new VolumetricVoxels();
		vv.initialise();
		Shape[] shapes = new Shape[1];
		// TODO: Why are we using hex here and vectors later. To reduce memory I suppose.
		Line[] lines = new Line[]{
				new Line(0x00FF00, new Vector3f(0), new Vector3f(0, 0, 10)),
				new Line(0x00FF00, new Vector3f(0, 0, 10), new Vector3f(10, 0, 10)),
				new Line(0x00FF00, new Vector3f(10, 0, 10), new Vector3f(0)),
		};
		shapes[0] = new Polygon(0xFF0000, lines, new float[]{0, 1, 0, 0}, true);
		Object lineObject = new Object(new Vector3f(100), shapes);

		long start = System.nanoTime();
		Octree octree = Rasterizer.rasterize(new Matrix4f(), lineObject);
		System.out.println("Rasterization time: " + (System.nanoTime() - start) / 1000000 + "ms");

		start = System.nanoTime();
		Mesh mesh = Tessellater.tessellate(octree);
		System.out.println("Tesselation time: " + (System.nanoTime() - start) / 1000000 + "ms");
		System.out.println("Num triangles: " + mesh.getNumTriangles());
		System.out.println("Vertex data length: " + mesh.getVertexDataLength());

		Entity entity = vv.addEntity(octree, mesh);
		while (!vv.getWindow().shouldClose()) {
			vv.update();
		}
	}
}
