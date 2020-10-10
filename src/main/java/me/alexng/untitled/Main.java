package me.alexng.untitled;

import me.alexng.untitled.generate.CombinedMap;
import me.alexng.untitled.generate.NoiseHelper;
import me.alexng.untitled.generate.Sampler;
import me.alexng.untitled.generate.TerrainGenerator;
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

	private static CombinedMap worldMap, worldMapLowRes, sampledMap;
	private static Texture worldMapLandmassTexture, worldMapHeightMapTexture, worldMapTemperatureMapTexture, worldMapMoistureMapTexture, worldMapBiomeMapTexture, sampledLandmassTexture, sampledHeightMapTexture, sampledTemperatureMapTexture, sampledMoistureMapTexture, sampledBiomeMapTexture;
	private static Mesh terrainMesh;

	public static void main(String[] args) throws IOException, UntitledException {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);

		Vector3f lightPosition = new Vector3f(0, 0, -2);

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

		worldMap = new CombinedMap(new Sampler(10000, 10000));
		worldMapLowRes = worldMap.sample(1000, 1000);
		sampledMap = worldMap.sample(4500, 4500, 1000, 1000);
		int seed = NoiseHelper.getSeed();
		worldMapLowRes.generate(seed);
		// sampledMap.generate(seed);
		worldMapLandmassTexture = worldMapLowRes.getLandmassMap().toTextureRGB(Texture.Type.DIFFUSE);
		worldMapHeightMapTexture = worldMapLowRes.getHeightMap().toTextureRGB(Texture.Type.DIFFUSE);
		worldMapTemperatureMapTexture = worldMapLowRes.getTemperatureMap().toTextureRGB(Texture.Type.DIFFUSE);
		worldMapMoistureMapTexture = worldMapLowRes.getMoistureMap().toTextureRGB(Texture.Type.DIFFUSE);
		worldMapBiomeMapTexture = worldMapLowRes.getBiomeMap().toTextureRGB(Texture.Type.DIFFUSE);
		sampledLandmassTexture = sampledMap.getLandmassMap().toTextureRGB(Texture.Type.DIFFUSE);
		sampledHeightMapTexture = sampledMap.getHeightMap().toTextureRGB(Texture.Type.DIFFUSE);
		sampledTemperatureMapTexture = sampledMap.getTemperatureMap().toTextureRGB(Texture.Type.DIFFUSE);
		sampledMoistureMapTexture = sampledMap.getMoistureMap().toTextureRGB(Texture.Type.DIFFUSE);
		sampledBiomeMapTexture = sampledMap.getBiomeMap().toTextureRGB(Texture.Type.DIFFUSE);

		Mesh landmassTextureMesh = generateTextureMesh(-15, -10, worldMapLandmassTexture);
		Mesh landmassTextureMesh2 = generateTextureMesh(-15, -5, sampledLandmassTexture);
		Mesh heightMapTextureMesh = generateTextureMesh(-10, -10, worldMapHeightMapTexture);
		Mesh heightMapTextureMesh2 = generateTextureMesh(-10, -5, sampledHeightMapTexture);
		Mesh biomeTextureMesh = generateTextureMesh(-5, -10, worldMapBiomeMapTexture);
		Mesh biomeTextureMesh2 = generateTextureMesh(-5, -5, sampledBiomeMapTexture);
		Mesh moistureTextureMesh = generateTextureMesh(0, -10, worldMapMoistureMapTexture);
		Mesh moistureTextureMesh2 = generateTextureMesh(0, -5, sampledMoistureMapTexture);
		Mesh temperatureTextureMesh = generateTextureMesh(5, -10, worldMapTemperatureMapTexture);
		Mesh temperatureTextureMesh2 = generateTextureMesh(5, -5, sampledTemperatureMapTexture);

		terrainMesh = TerrainGenerator.generateMeshFromMap(1000, 1000, 100, sampledMap);

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		window.setKeyCallback((windowHandle, key, scanCode, action, mode) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window.getHandle(), true);
			} else if (key == GLFW_KEY_R && action == GLFW_RELEASE) {
				regenerateMaps();
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

			terrainShaderProgram.use();
			terrainShaderProgram.setVec3f("light.position", camera.getPosition());
			terrainShaderProgram.setVec3f("viewPosition", camera.getPosition());
			terrainShaderProgram.setMatrix4f("view", view);
			terrainShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(10, 0, 0));
			terrainMesh.draw(terrainShaderProgram);

			texturedShaderProgram.use();
			texturedShaderProgram.setMatrix4f("view", view);
			texturedShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(5, 0, 5));
			landmassTextureMesh.draw(terrainShaderProgram);
			heightMapTextureMesh.draw(texturedShaderProgram);
			temperatureTextureMesh.draw(texturedShaderProgram);
			moistureTextureMesh.draw(texturedShaderProgram);
			biomeTextureMesh.draw(texturedShaderProgram);
			landmassTextureMesh2.draw(texturedShaderProgram);
			heightMapTextureMesh2.draw(texturedShaderProgram);
			temperatureTextureMesh2.draw(texturedShaderProgram);
			moistureTextureMesh2.draw(texturedShaderProgram);
			biomeTextureMesh2.draw(texturedShaderProgram);

			window.update();
		}
		window.cleanup();
	}

	private static Mesh generateTextureMesh(int x, int z, Texture texture) {
		final int dx = 5, dz = 5, y = 0;
		return new Mesh(new int[]{0, 1, 2, 1, 3, 2}, new float[]{x, y, z, 0, 0, x + dx, y, z, 1, 0, x, y, z + dz, 0, 1, x + dx, y, z + dz, 1, 1}, new Texture[]{texture}, AttributeStore.VEC3F_VEC2F);
	}

	private static void regenerateMaps() {
		System.out.println("Regenerating maps");
		int seed = NoiseHelper.getSeed();
		worldMapLowRes.generate(seed);
		sampledMap.generate(seed);
		worldMapLowRes.getLandmassMap().toTextureRGB(worldMapLandmassTexture);
		worldMapLowRes.getHeightMap().toTextureRGB(worldMapHeightMapTexture);
		worldMapLowRes.getTemperatureMap().toTextureRGB(worldMapTemperatureMapTexture);
		worldMapLowRes.getMoistureMap().toTextureRGB(worldMapMoistureMapTexture);
		worldMapLowRes.getBiomeMap().toTextureRGB(worldMapBiomeMapTexture);
		sampledMap.getLandmassMap().toTextureRGB(sampledLandmassTexture);
		sampledMap.getHeightMap().toTextureRGB(sampledHeightMapTexture);
		sampledMap.getTemperatureMap().toTextureRGB(sampledTemperatureMapTexture);
		sampledMap.getMoistureMap().toTextureRGB(sampledMoistureMapTexture);
		sampledMap.getBiomeMap().toTextureRGB(sampledBiomeMapTexture);
		terrainMesh = TerrainGenerator.generateMeshFromMap(1000, 1000, 100, sampledMap);
	}
}
