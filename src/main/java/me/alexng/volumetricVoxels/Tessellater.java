package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.render.AttributeStore;
import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.render.Texture;
import me.alexng.volumetricVoxels.storage.Octree;
import me.alexng.volumetricVoxels.storage.OctreeArrayGrid;
import me.alexng.volumetricVoxels.util.ConversionUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

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
		tessellateOctree(octree, indicesList, vertexData, new Matrix4f().identity());
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
		List<Integer> indicesList = new LinkedList<>();
		List<Float> vertexData = new LinkedList<>();
		Vector3i gridSize = arrayGrid.getGridSize();
		int width = arrayGrid.getOctreeWidth();
		for (int x = 0; x < gridSize.x; x++) {
			for (int y = 0; y < gridSize.y; y++) {
				for (int z = 0; z < gridSize.z; z++) {
					Octree octree = arrayGrid.getCell(x, y, z);
					if (octree != null) {
						// TODO: We need to pass a trnasform matrix in here.
						Matrix4f model = new Matrix4f().identity().translate(x * width, y * width, z * width);
						tessellateOctree(octree, indicesList, vertexData, model);
					}
				}
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
	private static void tessellateOctree(Octree octree, List<Integer> indices, List<Float> vertexData, Matrix4f model) {
		if (octree.isLeaf()) {
			Voxel voxel = octree.getValue();
			if (voxel == null) {
				return;
			}
			Vector4f position4 = new Vector4f(octree.getPosition(), 1);
			position4.mul(model);
			float x = position4.x;
			float y = position4.y;
			float z = position4.z;
			int ix = octree.getPosition().x;
			int iy = octree.getPosition().y;
			int iz = octree.getPosition().z;
			int width = octree.getWidth();
			if (isBlocked(octree, ix - width, iy, iz)) {
				Vector3f normal = new Vector3f(-1, 0, 0);
				addVertexData(vertexData, voxel, normal, x, y, z); // -4
				addVertexData(vertexData, voxel, normal, x, y + width, z); // -3
				addVertexData(vertexData, voxel, normal, x, y, z + width); // -2
				addVertexData(vertexData, voxel, normal, x, y + width, z + width); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 1);
			}
			if (isBlocked(octree, ix + width, iy, iz)) {
				Vector3f normal = new Vector3f(1, 0, 0);
				addVertexData(vertexData, voxel, normal, x + width, y, z); // -4
				addVertexData(vertexData, voxel, normal, x + width, y + width, z); // -3
				addVertexData(vertexData, voxel, normal, x + width, y, z + width); // -2
				addVertexData(vertexData, voxel, normal, x + width, y + width, z + width); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 1);
				indices.add(size - 2);
			}
			if (isBlocked(octree, ix, iy - width, iz)) {
				Vector3f normal = new Vector3f(0, -1, 0);
				addVertexData(vertexData, voxel, normal, x, y, z); // -4
				addVertexData(vertexData, voxel, normal, x + width, y, z); // -3
				addVertexData(vertexData, voxel, normal, x, y, z + width); // -2
				addVertexData(vertexData, voxel, normal, x + width, y, z + width); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 1);
				indices.add(size - 2);
			}
			if (isBlocked(octree, ix, iy + width, iz)) {
				Vector3f normal = new Vector3f(0, 1, 0);
				addVertexData(vertexData, voxel, normal, x, y + width, z); // -4
				addVertexData(vertexData, voxel, normal, x + width, y + width, z); // -3
				addVertexData(vertexData, voxel, normal, x, y + width, z + width); // -2
				addVertexData(vertexData, voxel, normal, x + width, y + width, z + width); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 1);
			}
			if (isBlocked(octree, ix, iy, iz - width)) {
				Vector3f normal = new Vector3f(0, 0, -1);
				addVertexData(vertexData, voxel, normal, x, y, z); // -4
				addVertexData(vertexData, voxel, normal, x + width, y, z); // -3
				addVertexData(vertexData, voxel, normal, x, y + width, z); // -2
				addVertexData(vertexData, voxel, normal, x + width, y + width, z); // -1
				int size = vertexData.size() / STRIDE;
				indices.add(size - 4);
				indices.add(size - 2);
				indices.add(size - 3);
				indices.add(size - 2);
				indices.add(size - 1);
				indices.add(size - 3);
			}
			if (isBlocked(octree, ix, iy, iz + width)) {
				Vector3f normal = new Vector3f(0, 0, 1);
				addVertexData(vertexData, voxel, normal, x, y, z + width); // -4
				addVertexData(vertexData, voxel, normal, x + width, y, z + width); // -3
				addVertexData(vertexData, voxel, normal, x, y + width, z + width); // -2
				addVertexData(vertexData, voxel, normal, x + width, y + width, z + width); // -1
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
				tessellateOctree(child, indices, vertexData, model);
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
