package me.alexng.volumetricVoxels;

import me.alexng.volumetricVoxels.render.Mesh;
import me.alexng.volumetricVoxels.storage.VoxelStore;

public class Entity {

	private VoxelStore voxelStore;
	private Mesh mesh;

	Entity(VoxelStore voxelStore, Mesh mesh) {
		this.voxelStore = voxelStore;
		this.mesh = mesh;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public VoxelStore getVoxelStore() {
		return voxelStore;
	}
}
