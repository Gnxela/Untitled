package me.alexng.untitled;

import me.alexng.untitled.generate.CombinedMap;
import me.alexng.untitled.generate.Sampler;
import me.alexng.untitled.render.*;
import me.alexng.untitled.render.exceptions.UntitledException;
import me.alexng.untitled.render.util.AttributeStore;
import me.alexng.untitled.render.util.CubeData;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

import static me.alexng.untitled.render.UntitledConstants.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class Main {

	public static void main(String[] args) throws IOException, UntitledException {
		Window window = Window.create(WIDTH, HEIGHT, TITLE);

		Vector3f lightPosition = new Vector3f(0, 0, -2);

		// TODO: Update view matrix when window changes
		Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 5000);

		ShaderProgram defaultShaderProgram = new ShaderProgram();
		defaultShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.vert"));
		defaultShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.frag"));
		defaultShaderProgram.linkProgram();
		defaultShaderProgram.use();
		defaultShaderProgram.setMatrix4f("projection", projection);
		defaultShaderProgram.setVec3f("light.ambient", 0.2f);
		defaultShaderProgram.setVec3f("light.diffuse", 0.5f);
		defaultShaderProgram.setVec3f("light.specular", 1);

		Mesh cubeMesh = new Mesh(CubeData.indexData, CubeData.vertexData, new Texture[]{}, AttributeStore.VEC3F_VEC3F_VEC2F);

		ShaderProgram lightShaderProgram = new ShaderProgram();
		lightShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/light.vert"));
		lightShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/light.frag"));
		lightShaderProgram.linkProgram();
		lightShaderProgram.use();
		lightShaderProgram.setMatrix4f("projection", projection);

		glEnable(GL_DEPTH_TEST);

		Model backpack = new Model("me/alexng/untitled/temp/backpack.obj");
		backpack.load();

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

		CombinedMap combinedMap = new CombinedMap(new Sampler(10000, 10000));
		CombinedMap sampledMap = combinedMap.sample(500, 500);
		sampledMap.generate();
		Texture heightMapTexture = sampledMap.getHeightMap().toTextureRGB(Texture.Type.DIFFUSE);
		Texture temperatureMapTexture = sampledMap.getTemperatureMap().toTextureRGB(Texture.Type.DIFFUSE);


		/*
		HeightMap heightMap = new HeightMap(new Sampler(0, 0, 1000, 1000));
		TemperatureMap temperatureMap = new TemperatureMap(heightMap);
		long start = System.currentTimeMillis();
		heightMap.generate();
		System.out.println("Height map: " + (System.currentTimeMillis() - start) / 1000f + "s");
		long startTemp = System.currentTimeMillis();
		temperatureMap.generate();
		System.out.println("Temperature map: " + (System.currentTimeMillis() - startTemp) / 1000f + "s");
		System.out.println("Map generation: " + (System.currentTimeMillis() - start) / 1000f + "s");

		Texture heightMapTexture = heightMap.toTextureRGB(Texture.Type.DIFFUSE);
		Texture temperatureMapTexture = temperatureMap.toTextureRGB(Texture.Type.DIFFUSE);
		 */

		float x = -10, dx = 5;
		float z = -10, dz = 5;
		Mesh heightMapTextureMesh = new Mesh(new int[]{0, 1, 2, 1, 3, 2}, new float[]{x, 0, z, 0, 0, x + dx, 0, z, 1, 0, x, 0, z + dz, 0, 1, x + dx, 0, z + dz, 1, 1}, new Texture[]{heightMapTexture}, AttributeStore.VEC3F_VEC2F);
		x = -5;
		z = -10;
		Mesh temperatureTextureMesh = new Mesh(new int[]{0, 1, 2, 1, 3, 2}, new float[]{x, 0, z, 0, 0, x + dx, 0, z, 1, 0, x, 0, z + dz, 0, 1, x + dx, 0, z + dz, 1, 1}, new Texture[]{temperatureMapTexture}, AttributeStore.VEC3F_VEC2F);

		//Mesh terrainMesh = TerrainGenerator.generateMeshFromHeightMap(1000, 1000, 2000, 2000, 100, heightMap);

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		window.show();
		window.hideAndCaptureCursor();
		Camera camera = new Camera(new Vector3f(0, 0, 3), 0, -90);
		window.setCursorPositionCallback(camera);

		while (!window.shouldClose()) {
			camera.processInput(window);
			Matrix4f view = camera.createViewMatrix();
			window.clear();

			float radius = 13f;
			lightPosition.x = (float) (Math.cos(glfwGetTime()) * radius);
			lightPosition.y = (float) (Math.cos(glfwGetTime() / 3) * radius);
			lightPosition.z = (float) (Math.sin(glfwGetTime()) * radius);

			defaultShaderProgram.use();
			defaultShaderProgram.setVec3f("light.position", lightPosition);
			defaultShaderProgram.setVec3f("viewPosition", camera.getPosition());
			defaultShaderProgram.setMatrix4f("view", view);
			int numCircles = 3;
			int numModels = 20;
			radius = 8;
			for (int j = 0; j < numCircles; j++) {
				for (int i = 0; i < numModels; i++) {
					Matrix4f model = new Matrix4f().identity()
							.translate((float) Math.cos(Math.PI * 2 / numModels * i) * radius, j * 3 - (numCircles - 1) / 2f * 3, (float) Math.sin(Math.PI * 2 / numModels * i) * radius)
							.rotate((float) (-Math.PI * 2 / numModels * i - Math.PI / 2), 0, 1, 0)
							.scale(0.5f);
					defaultShaderProgram.setMatrix4f("model", model);
					backpack.draw(defaultShaderProgram);
				}
			}

			lightShaderProgram.use();
			Matrix4f model = new Matrix4f().identity()
					.translate(lightPosition)
					.scale(0.25f);
			lightShaderProgram.setMatrix4f("view", view);
			lightShaderProgram.setMatrix4f("model", model);
			cubeMesh.draw(lightShaderProgram);

			terrainShaderProgram.use();
			terrainShaderProgram.setVec3f("light.position", camera.getPosition());
			terrainShaderProgram.setVec3f("viewPosition", camera.getPosition());
			terrainShaderProgram.setMatrix4f("view", view);
			terrainShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(10, 0, 0));
			//terrainMesh.draw(terrainShaderProgram);

			texturedShaderProgram.use();
			texturedShaderProgram.setMatrix4f("view", view);
			texturedShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(5, 0, 5));
			heightMapTextureMesh.draw(texturedShaderProgram);
			temperatureTextureMesh.draw(texturedShaderProgram);

			window.update();
		}
		defaultShaderProgram.cleanup();
		cubeMesh.cleanup();
		window.cleanup();
	}
}
