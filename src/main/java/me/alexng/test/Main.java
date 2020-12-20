package me.alexng.test;

import me.alexng.volumetricVoxels.Object;
import me.alexng.volumetricVoxels.*;
import me.alexng.volumetricVoxels.exceptions.OctreeException;
import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.shape.CutShape;
import me.alexng.volumetricVoxels.shape.IntersectShape;
import me.alexng.volumetricVoxels.shape.Shape;
import me.alexng.volumetricVoxels.shape.Sphere;
import me.alexng.volumetricVoxels.storage.Octree;
import org.joml.Vector3f;

public class Main {

	public static void main(String[] args) throws OctreeException, ShaderException, TextureException {
		VolumetricVoxels vv = new VolumetricVoxels();
		vv.initialise();
		Object sphereObject = new Object(new Vector3f(100), new Shape[]{
				new CutShape(
						new Sphere(new Vector3f(50), 50),
						new IntersectShape(
								new Sphere(new Vector3f(30, 45, 30), 25),
								new Sphere(new Vector3f(30, 30, 30), 25)))
		});

		long start = System.nanoTime();
		Octree octree = Rasterizer.rasterize(sphereObject);
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
