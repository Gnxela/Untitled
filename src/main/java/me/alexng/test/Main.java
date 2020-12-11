package me.alexng.test;

import me.alexng.volumetricVoxels.VolumetricVoxels;

public class Main {

	public static void main(String[] args) {
		VolumetricVoxels vv = new VolumetricVoxels();
		vv.initialise();
		while (!vv.getWindow().shouldClose()) {
			vv.update();
		}
	}
}
