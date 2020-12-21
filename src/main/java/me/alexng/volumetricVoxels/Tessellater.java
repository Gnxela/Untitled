package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.render.AttributeStore;
import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.render.Texture;
import me.alexng.volumetricVoxels.storage.Octree;
import me.alexng.volumetricVoxels.storage.OctreeArrayGrid;
import me.alexng.volumetricVoxels.util.ConversionUtil;
import org.joml.Vector3f;

import java.util.LinkedList;
import java.util.List;

// TODO: Tessellator?
public class Tessellater {

	private static final AttributeStore VERTEX_DATA_TYPE = AttributeStore.VEC3F_VEC3F_VEC3F;
	private static final int STRIDE = VERTEX_DATA_TYPE.getTotalSize();

	// TODO: Convert to VoxelStore
	public static Mesh tessellateOctree(Octree octree) {
		// TODO: To avoid creating and destroying large Lists we could make this non-static and synchronised.
		List<Integer> indicesList = new LinkedList<>();
		List<Float> vertexData = new LinkedList<>();
		tessellateOctree(octree, indicesList, vertexData);
		int[] indices = new int[indicesList.size()];
		int index = 0;
		for (Integer i : indicesList) {
			indices[index++] = i;
		}
		float[] vertices = new float[vertexData.size()];
		index = 0;
		for (Float f : vertexData) {
			vertices[index++] = f;
		}
		return new Mesh(indices, vertices, new Texture[]{}, VERTEX_DATA_TYPE);
	}

	public static Mesh tessellateOctreeArrayGrid(OctreeArrayGrid arrayGrid) {
		// TODO: To avoid creating and destroying large Lists we could make this non-static and synchronised.
		List<Integer> indicesList = new LinkedList<>();
		List<Float> vertexData = new LinkedList<>();
		for (Octree octree : arrayGrid.getChildren()) {
			if (octree != null) {
				tessellateOctree(octree, indicesList, vertexData);
			}
		}
		int[] indices = new int[indicesList.size()];
		int index = 0;
		for (Integer i : indicesList) {
			indices[index++] = i;
		}
		float[] vertices = new float[vertexData.size()];
		index = 0;
		for (Float f : vertexData) {
			vertices[index++] = f;
		}
		return new Mesh(indices, vertices, new Texture[]{}, VERTEX_DATA_TYPE);
	}

	/**
	 * Can only tessellate octrees of uniform LOD! Terribly optimised. Neighbor search?
	 *
	 * @param octree     The current octree being tessellated.
	 * @param indices    List of indices.
	 * @param vertexData Ordered map of vertex data.
	 */
	// TODO: We are not sharing vertexData between voxels that are adjacent. If distinct vertex data (color, etc.) is not needed we can combine them.
	private static void tessellateOctree(Octree octree, List<Integer> indices, List<Float> vertexData) {
		if (octree.isLeaf()) {
			Voxel voxel = octree.getValue();
			if (voxel == null) {
				return;
			}
			int x = octree.getPosition().x();
			int y = octree.getPosition().y();
			int z = octree.getPosition().z();
			int width = octree.getWidth();
			// TODO: Check indices order (clockwise / anticlockwise)
			if (isBlocked(octree, x - width, y, z)) {
				Vector3f normal = new Vector3f(-1, 0, 0);
				addVertexData(vertexData, voxel, normal, (float) x, (float) y, (float) z); // -4
				addVertexData(vertexData, voxel, normal, (float) x, (float) y + width, (float) z); // -3
				addVertexData(vertexData, voxel, normal, (float) x, (float) y, (float) z + width); // -2
				addVertexData(vertexData, voxel, normal, (float) x, (float) y + width, (float) z + width); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 1);
				indices.add(size - 2);
			}
			if (isBlocked(octree, x + width, y, z)) {
				Vector3f normal = new Vector3f(1, 0, 0);
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y, (float) z); // -4
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y + width, (float) z); // -3
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y, (float) z + width); // -2
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y + width, (float) z + width); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 1);
				indices.add(size - 2);
			}
			if (isBlocked(octree, x, y - width, z)) {
				Vector3f normal = new Vector3f(0, -1, 0);
				addVertexData(vertexData, voxel, normal, (float) x, (float) y, (float) z); // -4
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y, (float) z); // -3
				addVertexData(vertexData, voxel, normal, (float) x, (float) y, (float) z + width); // -2
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y, (float) z + width); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 1);
				indices.add(size - 2);
			}
			if (isBlocked(octree, x, y + width, z)) {
				Vector3f normal = new Vector3f(0, 1, 0);
				addVertexData(vertexData, voxel, normal, (float) x, (float) y + width, (float) z); // -4
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y + width, (float) z); // -3
				addVertexData(vertexData, voxel, normal, (float) x, (float) y + width, (float) z + width); // -2
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y + width, (float) z + width); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 1);
				indices.add(size - 2);
			}
			if (isBlocked(octree, x, y, z - width)) {
				Vector3f normal = new Vector3f(0, 0, -1);
				addVertexData(vertexData, voxel, normal, (float) x, (float) y, (float) z); // -4
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y, (float) z); // -3
				addVertexData(vertexData, voxel, normal, (float) x, (float) y + width, (float) z); // -2
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y + width, (float) z); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 1);
				indices.add(size - 2);
			}
			if (isBlocked(octree, x, y, z + width)) {
				Vector3f normal = new Vector3f(0, 0, 1);
				addVertexData(vertexData, voxel, normal, (float) x, (float) y, (float) z + width); // -4
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y, (float) z + width); // -3
				addVertexData(vertexData, voxel, normal, (float) x, (float) y + width, (float) z + width); // -2
				addVertexData(vertexData, voxel, normal, (float) x + width, (float) y + width, (float) z + width); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 1);
				indices.add(size - 2);
			}
		} else if (octree.hasChildren()) {
			for (Octree child : octree.getChildren()) {
				tessellateOctree(child, indices, vertexData);
			}
		}
	}

	private static boolean isBlocked(Octree octree, int x, int y, int z) {
		// For every voxel, 3/6 neighbors are guaranteed to be in the parents children.
		return !octree.getRoot().contains(x, y, z) || octree.getRoot().get(x, y, z) == null;
	}

	private static void addVertexData(List<Float> vertexData, Voxel voxel, Vector3f normal, float x, float y, float z) {
		vertexData.add(x);
		vertexData.add(y);
		vertexData.add(z);
		vertexData.add(normal.x);
		vertexData.add(normal.y);
		vertexData.add(normal.z);
		Vector3f color = ConversionUtil.rgbIntToVector3f(voxel.getColor());
		vertexData.add(color.x);
		vertexData.add(color.y);
		vertexData.add(color.z);
	}
}
