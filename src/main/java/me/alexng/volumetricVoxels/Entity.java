package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.render.shader.SID;
import me.alexng.volumetricVoxels.render.shader.ShaderProgram;
import me.alexng.volumetricVoxels.storage.Octree;
import me.alexng.volumetricVoxels.storage.OctreeArrayGrid;
import me.alexng.volumetricVoxels.storage.VoxelStore;
import me.alexng.volumetricVoxels.util.Colors;
import me.alexng.volumetricVoxels.util.DebugRenderer;
import org.joml.Matrix4f;

public class Entity {

	private VoxelStore voxelStore;
	private Mesh mesh;
	private boolean debugVoxelStore;
	private boolean debugMeshWireframe;

	Entity(VoxelStore voxelStore, Mesh mesh) {
		this.voxelStore = voxelStore;
		this.mesh = mesh;
		this.debugVoxelStore = false;
		this.debugMeshWireframe = false;
	}

	void render(ShaderProgram shaderProgram, Matrix4f view, Matrix4f projection) {
		shaderProgram.setMatrix4f(SID.MODEL, new Matrix4f().identity());
		if (debugMeshWireframe) {
			mesh.drawOutline(shaderProgram);
		} else {
			mesh.draw(shaderProgram);
		}

		if (debugVoxelStore) {
			if (voxelStore instanceof Octree) {
				DebugRenderer.drawOctree((Octree) voxelStore, Colors.RED, new Matrix4f(), view, projection);
			} else if (voxelStore instanceof OctreeArrayGrid) {
				DebugRenderer.drawOctreeArrayGrid((OctreeArrayGrid) voxelStore, Colors.RED, new Matrix4f(), view, projection);
			}
		}
	}

	public Mesh getMesh() {
		return mesh;
	}

	public VoxelStore getVoxelStore() {
		return voxelStore;
	}

	public void setDebugVoxelStore(boolean debugVoxelStore) {
		this.debugVoxelStore = debugVoxelStore;
	}

	public void setDebugMeshWireframe(boolean debugMeshWireframe) {
		this.debugMeshWireframe = debugMeshWireframe;
	}
}
