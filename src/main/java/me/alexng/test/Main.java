package me.alexng.test;

import me.alexng.volumetricVoxels.Entity;
import me.alexng.volumetricVoxels.Object;
import me.alexng.volumetricVoxels.Tessellater;
import me.alexng.volumetricVoxels.VolumetricVoxels;
import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.raster.Rasterizer;
import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.shape.Line;
import me.alexng.volumetricVoxels.shape.Shape;
import me.alexng.volumetricVoxels.storage.Octree;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Main {

	public static void main(String[] args) throws OctreeException, ShaderException, TextureException {
		VolumetricVoxels vv = new VolumetricVoxels();
		vv.initialise();
		Object lineObject = new Object(new Vector3f(1000), new Shape[]{
				new Line(0x000000, new Vector3f(0, 0, 1), new Vector3f(1000, 1000, 1001)),
				new Line(0x222222, new Vector3f(0, 0, 2), new Vector3f(1000, 1000, 1002)),
				new Line(0x444444, new Vector3f(0, 0, 3), new Vector3f(1000, 1000, 1003)),
				new Line(0x666666, new Vector3f(0, 0, 4), new Vector3f(1000, 1000, 1004))
		});

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
