package me.alexng.untitled.render.shader;

/**
 * Shader Identifiers
 */
public enum SID {
	VOXEL_COLOR("voxel_color"),

	LIGHT_POSITION("light.position"),
	LIGHT_AMBIENT("light.ambient"),
	LIGHT_DIFFUSE("light.diffuse"),
	LIGHT_SPECULAR("light.specular"),

	VIEW_POSITION("viewPosition"),
	MODEL("model"),
	VIEW("view"),
	PROJECTION("projection");

	private String glslName;

	SID(String glslName) {
		this.glslName = glslName;
	}

	public String getGlslName() {
		return glslName;
	}
}
