package me.alexng.untitled.render;

/**
 * Shader Identifiers
 */
public enum SID {
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
