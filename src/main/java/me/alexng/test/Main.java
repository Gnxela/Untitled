package me.alexng.test;

import me.alexng.volumetricVoxels.Entity;
import me.alexng.volumetricVoxels.ObjectTemplate;
import me.alexng.volumetricVoxels.Tessellater;
import me.alexng.volumetricVoxels.VolumetricVoxels;
import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.exceptions.VoxelStoreException;
import me.alexng.volumetricVoxels.raster.Rasterizer;
import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.shape.Line;
import me.alexng.volumetricVoxels.shape.Shape;
import me.alexng.volumetricVoxels.storage.OctreeArrayGrid;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Main {

	public static void main(String[] args) throws VoxelStoreException, ShaderException, TextureException {
		VolumetricVoxels vv = new VolumetricVoxels();
		vv.initialise();

		Shape[] shapes = new Shape[1];
		shapes[0] = new Line(0x00FF00, new Vector3f(0, 5, 5), new Vector3f(50, 5, 5));
		ObjectTemplate lineObjectTemplate = new ObjectTemplate(new Vector3f(100), shapes);

		// int octreeSize = Octree.upgradeWidth((int) Math.ceil(Math.max(lineObjectTemplate.getSize().x, Math.max(lineObjectTemplate.getSize().y, lineObjectTemplate.getSize().z))));
		// Octree octree = Octree.create(octreeSize);
		OctreeArrayGrid octreeArrayGrid = new OctreeArrayGrid(new Vector3i(3, 2, 3), 32);

		long start = System.nanoTime();
		Rasterizer.rasterize(new Matrix4f(), lineObjectTemplate, octreeArrayGrid);
		System.out.println("Rasterization time: " + (System.nanoTime() - start) / 1000000 + "ms");

		start = System.nanoTime();
		Mesh mesh = Tessellater.tessellateOctreeArrayGrid(octreeArrayGrid);
		System.out.println("Tesselation time: " + (System.nanoTime() - start) / 1000000 + "ms");
		System.out.println("Num triangles: " + mesh.getNumTriangles());
		System.out.println("Vertex data length: " + mesh.getVertexDataLength());

		Entity entity = vv.addEntity(octreeArrayGrid, mesh);
		while (!vv.getWindow().shouldClose()) {
			vv.update();
		}
	}
}
