package me.alexng.untitled;

import me.alexng.untitled.generate.HeightMap;
import me.alexng.untitled.generate.MapData;
import me.alexng.untitled.generate.TerrainGenerator;
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
		Matrix4f projection = new Matrix4f().perspective(FOV, ((float) WIDTH) / ((float) HEIGHT), 0.1f, 1000);

		ShaderProgram defaultShaderProgram = new ShaderProgram();
		defaultShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.vert"));
		defaultShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/basic.frag"));
		defaultShaderProgram.linkProgram();
		defaultShaderProgram.use();
		defaultShaderProgram.setMatrix4f("projection", projection);
		defaultShaderProgram.setFloat("material.shininess", 32);
		defaultShaderProgram.setVec3f("light.ambient", 0.2f, 0.2f, 0.2f);
		defaultShaderProgram.setVec3f("light.diffuse", 0.5f, 0.5f, 0.5f);
		defaultShaderProgram.setVec3f("light.specular", 1, 1, 1);

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
		terrainShaderProgram.setVec3f("terrainColor", 0.05f, 0.2f, 0f);

		Mesh flatMesh = TerrainGenerator.generateFlatMesh(40, 40, 5, 5);
		Mesh flatMesh2 = TerrainGenerator.generateFlatMesh(40, 40, 10, 20);
		Mesh flatMesh3 = TerrainGenerator.generateFlatMesh(40, 40, 20, 20);
		Mesh flatMesh4 = TerrainGenerator.generateFlatMesh(40, 40, 40, 40);

		ShaderProgram texturedShaderProgram = new ShaderProgram();
		texturedShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/textured.frag"));
		texturedShaderProgram.attachShader(new Shader("me/alexng/untitled/shaders/textured.vert"));
		texturedShaderProgram.linkProgram();
		texturedShaderProgram.use();
		texturedShaderProgram.setMatrix4f("projection", projection);

		MapData mapData = new HeightMap(1000, 1000);
		Texture mapTexture = mapData.toTextureRGB(Texture.Type.DIFFUSE);
		float x = -10, dx = 5;
		float z = -10, dz = 5;
		Mesh texMesh = new Mesh(new int[]{0, 1, 2, 1, 3, 2}, new float[]{x, 0, z, 0, 0, x + dx, 0, z, 1, 0, x, 0, z + dz, 0, 1, x + dx, 0, z + dz, 1, 1}, new Texture[]{mapTexture}, AttributeStore.VEC3F_VEC2F);

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
			defaultShaderProgram.setMatrix4f("view", view);
			defaultShaderProgram.setVec3f("viewPosition", camera.getPosition());
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
			terrainShaderProgram.setMatrix4f("view", view);
			terrainShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(10, 0, 0));
			flatMesh.draw(terrainShaderProgram);
			terrainShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(50, 0, 0));
			flatMesh2.draw(terrainShaderProgram);
			terrainShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(90, 0, 0));
			flatMesh3.draw(terrainShaderProgram);
			terrainShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(130, 0, 0));
			flatMesh4.draw(terrainShaderProgram);

			texturedShaderProgram.use();
			texturedShaderProgram.setMatrix4f("view", view);
			texturedShaderProgram.setMatrix4f("model", new Matrix4f().identity().translate(5, 0, 5));
			texMesh.draw(texturedShaderProgram);

			window.update();
		}
		defaultShaderProgram.cleanup();
		cubeMesh.cleanup();
		window.cleanup();
	}
}
