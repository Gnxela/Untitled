package me.alexng.untitled;

import me.alexng.untitled.generate.ColorMaps;
import me.alexng.untitled.generate.CombinedMap;
import me.alexng.untitled.generate.NoiseHelper;
import me.alexng.untitled.generate.Sampler;
import me.alexng.untitled.render.*;
import me.alexng.untitled.render.exceptions.UntitledException;
import me.alexng.untitled.render.util.AttributeStore;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

import static me.alexng.untitled.render.UntitledConstants.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class Main {

	public static void main(String[] args) throws IOException, UntitledException {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);

		// TODO: Update view matrix when window changes
		Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 5000);

		glEnable(GL_DEPTH_TEST);

		ShaderProgram terrainShaderProgram = new ShaderProgram();
		terrainShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/terrain.frag"));
		terrainShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/terrain.vert"));
		terrainShaderProgram.linkProgram();
		terrainShaderProgram.use();
		terrainShaderProgram.setMatrix4f("projection", projection);
		terrainShaderProgram.setVec3f("light.ambient", 0.2f);
		terrainShaderProgram.setVec3f("light.diffuse", 0.5f);
		terrainShaderProgram.setVec3f("light.specular", 0.05f);


		ShaderProgram texturedShaderProgram = new ShaderProgram();
		texturedShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/textured.frag"));
		texturedShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/textured.vert"));
		texturedShaderProgram.linkProgram();
		texturedShaderProgram.use();
		texturedShaderProgram.setMatrix4f("projection", projection);

		CombinedMap worldMap = new CombinedMap(new Sampler(10000, 10000));
		CombinedMap worldMapLowRes = worldMap.sample(1000, 1000);
		int seed = NoiseHelper.getSeed();
		worldMapLowRes.generate(seed);
		Texture mapTexture1 = worldMapLowRes.getGenerationPipeline().getBiomePipe().getStoredData().toTextureRGB(Texture.Type.DIFFUSE, ColorMaps.BIOME_MAP);
		Texture mapTexture2 = worldMapLowRes.getGenerationPipeline().getTemperaturePipe().getStoredData().toTextureRGB(Texture.Type.DIFFUSE, ColorMaps.TEMPERATURE_MAP);
		Mesh mapTextureMesh1 = generateTextureMesh(-5, -10, mapTexture1);
		Mesh mapTextureMesh2 = generateTextureMesh(-10, -10, mapTexture2);

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		window.setKeyCallback((windowHandle, key, scanCode, action, mode) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window.getHandle(), true);
			} else if (key == GLFW_KEY_R && action == GLFW_RELEASE) {

			}
		});

		window.show();
		window.hideAndCaptureCursor();
		Camera camera = new Camera(new Vector3f(0, 0, 0), -90, -90);
		window.setCursorPositionCallback(camera);

		while (!window.shouldClose()) {
			camera.processInput(window);
			Matrix4f view = camera.createViewMatrix();
			window.clear();

			texturedShaderProgram.use();
			texturedShaderProgram.setMatrix4f("view", view);
			texturedShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(5, 0, 5));
			mapTextureMesh1.draw(texturedShaderProgram);
			mapTextureMesh2.draw(texturedShaderProgram);

			window.update();
		}
		window.cleanup();
	}

	private static Mesh generateTextureMesh(int x, int z, Texture texture) {
		final int dx = 5, dz = 5, y = 0;
		return new Mesh(new int[]{0, 1, 2, 1, 3, 2}, new float[]{x, y, z, 0, 0, x + dx, y, z, 1, 0, x, y, z + dz, 0, 1, x + dx, y, z + dz, 1, 1}, new Texture[]{texture}, AttributeStore.VEC3F_VEC2F);
	}
}
