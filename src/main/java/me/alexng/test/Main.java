package me.alexng.test;

import me.alexng.volumetricVoxels.Object;
import me.alexng.volumetricVoxels.VolumetricVoxels;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.shape.Sphere;
import org.joml.Vector3f;

public class Main {

	public static void main(String[] args) throws TextureException {
		VolumetricVoxels vv = new VolumetricVoxels();
		vv.initialise();
		Object sphereObject = new Object(new Vector3f(10, 10, 10), new Sphere[]{new Sphere(new Vector3f(5, 5, 5), 5)});
		vv.createEntity(sphereObject);
		while (!vv.getWindow().shouldClose()) {
			vv.update();
		}
	}
}
