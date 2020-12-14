package me.alexng.test;

import me.alexng.volumetricVoxels.Object;
import me.alexng.volumetricVoxels.VolumetricVoxels;
import me.alexng.volumetricVoxels.exceptions.ShaderException;
import me.alexng.volumetricVoxels.exceptions.TextureException;
import me.alexng.volumetricVoxels.shape.CutShape;
import me.alexng.volumetricVoxels.shape.IntersectShape;
import me.alexng.volumetricVoxels.shape.Shape;
import me.alexng.volumetricVoxels.shape.Sphere;
import org.joml.Vector3f;

public class Main {

	public static void main(String[] args) throws ShaderException, TextureException {
		VolumetricVoxels vv = new VolumetricVoxels();
		vv.initialise();
		Object sphereObject = new Object(new Vector3f(100), new Shape[]{
				new CutShape(
						new Sphere(new Vector3f(50), 50),
						new IntersectShape(
								new Sphere(new Vector3f(30, 45, 30), 25),
								new Sphere(new Vector3f(30, 30, 30), 25)))
		});
		vv.createEntity(sphereObject);
		while (!vv.getWindow().shouldClose()) {
			vv.update();
		}
	}
}
