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
import me.alexng.volumetricVoxels.util.ConversionUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Main {

	public static void main(String[] args) throws VoxelStoreException, ShaderException, TextureException {
		VolumetricVoxels vv = new VolumetricVoxels();
		vv.initialise();

		ObjectTemplate lineObjectTemplate = createObjectTemplate();

		// int octreeSize = Octree.upgradeWidth((int) Math.ceil(Math.max(lineObjectTemplate.getSize().x, Math.max(lineObjectTemplate.getSize().y, lineObjectTemplate.getSize().z))));
		// Octree octree = Octree.create(octreeSize);
		OctreeArrayGrid octreeArrayGrid = new OctreeArrayGrid(new Vector3i(4, 4, 4), 32);

		long start = System.nanoTime();
		Rasterizer.rasterize(new Matrix4f(), lineObjectTemplate, octreeArrayGrid);
		System.out.println("Rasterization time: " + (System.nanoTime() - start) / 1000000 + "ms");

		start = System.nanoTime();
		Mesh mesh = Tessellater.tessellateOctreeArrayGrid(octreeArrayGrid);
		System.out.println("Tesselation time: " + (System.nanoTime() - start) / 1000000 + "ms");
		System.out.println("Num triangles: " + mesh.getNumTriangles());
		System.out.println("Num voxels: " + mesh.getNumTriangles() / 8);
		System.out.println("Vertex data length: " + mesh.getVertexDataLength());

		Entity entity = vv.createEntity(octreeArrayGrid, mesh);
		Entity entity1 = vv.createEntity(octreeArrayGrid, mesh);
		entity1.getRotation().rotateX((float) Math.PI / 2);
		entity1.setDebugVoxelStore(true);
		int frames = 0;
		long lastUpdate = System.nanoTime();
		while (!vv.getWindow().shouldClose()) {
			if (System.nanoTime() - lastUpdate > 1000000000) {
				vv.getWindow().setTitle("FPS: " + frames);
				frames = 0;
				lastUpdate = System.nanoTime();
			}
			frames++;
			vv.render();
		}
	}

	private static ObjectTemplate createObjectTemplate() {
		Vector3f currentLineCenter = new Vector3f(50, 0, 50);
		Shape[] shapes = new Shape[100];
		double length = shapes.length;
		for (int i = 0; i < shapes.length; i++) {
			float cos = (float) Math.cos(i / length * Math.PI * 2) * 50, sin = (float) Math.sin(i / length * Math.PI * 2) * 50;
			shapes[i] = new Line(ConversionUtil.hsvToRgb((float) (i / length), 0.8f, 0.8f), new Vector3f(currentLineCenter).sub(cos, 0, sin), new Vector3f(currentLineCenter).add(cos, 0, sin));
			currentLineCenter.add(0, 1, 0);
		}
		return new ObjectTemplate(new Vector3f(100), shapes);
	}
}
