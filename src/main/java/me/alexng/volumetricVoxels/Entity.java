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
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity {

	private boolean debugVoxelStore;
	private boolean debugMeshWireframe;

	private VoxelStore voxelStore;
	private Mesh mesh;
	private Vector3f position;
	private Quaternionf rotation;

	Entity(VoxelStore voxelStore, Mesh mesh) {
		this.voxelStore = voxelStore;
		this.mesh = mesh;
		this.position = new Vector3f();
		this.rotation = new Quaternionf();
	}

	void render(ShaderProgram shaderProgram, Matrix4f view, Matrix4f projection) {
		// TODO: Rotate -> Translate? Translate -> Rotate? How can we rotate around the center
		//  How can I ensure there are no scales?
		Matrix4f model = new Matrix4f().identity().translate(position).rotate(rotation);
		shaderProgram.setMatrix4f(SID.MODEL, model);
		if (debugMeshWireframe) {
			mesh.drawOutline(shaderProgram);
		} else {
			mesh.draw(shaderProgram);
		}

		if (debugVoxelStore) {
			if (voxelStore instanceof Octree) {
				DebugRenderer.drawOctree((Octree) voxelStore, Colors.RED, model, view, projection);
			} else if (voxelStore instanceof OctreeArrayGrid) {
				DebugRenderer.drawOctreeArrayGrid((OctreeArrayGrid) voxelStore, Colors.RED, model, view, projection);
			}
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public Quaternionf getRotation() {
		return rotation;
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
