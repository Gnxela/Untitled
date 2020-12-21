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
import me.alexng.volumetricVoxels.shape.Shape;
import me.alexng.volumetricVoxels.storage.Octree;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Main {

	public static void main(String[] args) throws VoxelStoreException, ShaderException, TextureException {
		VolumetricVoxels vv = new VolumetricVoxels();
		vv.initialise();
		Shape[] shapes = new Shape[40];
		int objectSize = 100;
		Vector3f startPoint = new Vector3f(objectSize / 2f, objectSize / 2f, objectSize / 2f);
		float pi2 = (float) Math.PI * 2 / shapes.length;
		for (int i = 0; i < shapes.length; i++) {
			shapes[i] = new Line(0xFF0000, startPoint, new Vector3f((float) Math.cos(pi2 * i) * objectSize / 2, 0, (float) Math.sin(pi2 * i) * objectSize / 2).add(startPoint));
		}
		Object lineObject = new Object(new Vector3f(objectSize), shapes);

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
