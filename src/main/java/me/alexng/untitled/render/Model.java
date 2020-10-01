package me.alexng.untitled.render;

import me.alexng.untitled.render.exceptions.ModelException;
import me.alexng.untitled.render.exceptions.TextureException;
import me.alexng.untitled.util.FileUtil;
import org.lwjgl.assimp.*;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.lwjgl.assimp.Assimp.*;


public class Model {

	private static final int TEXTURE_COORDS_PER_MESH = 1;

	private final String resourcePath;
	private final Set<String> loadedTextures;
	private Mesh[] meshes;
	private String directory;

	public Model(String resourcePath) {
		this.resourcePath = resourcePath;
		this.loadedTextures = new HashSet<>();
	}

	public void draw(ShaderProgram shaderProgram) throws TextureException {
		for (Mesh mesh : meshes) {
			mesh.draw(shaderProgram);
		}
	}

	public void load() throws ModelException, TextureException {
		// aiProcess_GenNormals, aiProcess_SplitLargeMeshes, aiProcess_OptimizeMeshes
		String absolutePath = FileUtil.getAbsolutePath(resourcePath);
		directory = absolutePath.substring(0, absolutePath.lastIndexOf("/")) + '/';
		AIScene scene = aiImportFile(absolutePath, aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_OptimizeMeshes | aiProcess_GenNormals);
		if (scene == null /*|| (scene.mFlags() & AI_SCENE_FLAGS_INCOMPLETE) == 0*/ || scene.mRootNode() == null) {
			throw new ModelException("Failed to load model " + resourcePath + ". Scene: " + scene);
		}

		List<Mesh> tempMeshList = new LinkedList<>();
		processNode(scene, scene.mRootNode(), tempMeshList);

		meshes = new Mesh[tempMeshList.size()];
		int index = 0;
		for (Mesh mesh : tempMeshList) {
			meshes[index++] = mesh;
		}
		System.out.println("Loaded: " + absolutePath + ". " + meshes.length + " meshes.");
	}

	private void processNode(AIScene scene, AINode node, List<Mesh> tempMeshList) throws TextureException {
		int numMeshes = node.mNumMeshes();
		for (int i = 0; i < numMeshes; i++) {
			int meshIndex = node.mMeshes().get(i);
			long meshAddress = scene.mMeshes().get(meshIndex);
			AIMesh aiMesh = AIMesh.create(meshAddress);
			tempMeshList.add(processMesh(scene, aiMesh));
		}
		for (int i = 0; i < node.mNumChildren(); i++) {
			AINode child = AINode.createSafe(node.mChildren().get(i));
			processNode(scene, child, tempMeshList);
		}
	}

	private Mesh processMesh(AIScene scene, AIMesh mesh) throws TextureException {
		float[] vertices = new float[mesh.mNumVertices() * Mesh.STRIDE];
		for (int i = 0; i < mesh.mNumVertices(); i++) {
			int baseIndex = i * Mesh.STRIDE;
			// Position
			AIVector3D vector = mesh.mVertices().get(i);
			vertices[baseIndex + 0] = vector.x();
			vertices[baseIndex + 1] = vector.y();
			vertices[baseIndex + 2] = vector.z();
			// Normal
			AIVector3D normal = mesh.mNormals().get(i);
			vertices[baseIndex + 3] = normal.x();
			vertices[baseIndex + 4] = normal.y();
			vertices[baseIndex + 5] = normal.z();

			// Texture coord
			for (int j = 0; j < TEXTURE_COORDS_PER_MESH; j++) {
				int textureIndexBase = 6 + j * 2;
				AIVector3D.Buffer textureCoordBuffer = mesh.mTextureCoords(j);
				if (textureCoordBuffer == null) {
					vertices[baseIndex + textureIndexBase + 0] = 0;
					vertices[baseIndex + textureIndexBase + 1] = 0;
				} else {
					AIVector3D textureCoord = textureCoordBuffer.get(i);
					vertices[baseIndex + textureIndexBase + 0] = textureCoord.x();
					vertices[baseIndex + textureIndexBase + 1] = textureCoord.y();
				}
			}
		}

		int[] indices = new int[mesh.mNumFaces() * 3];
		for (int i = 0; i < mesh.mNumFaces(); i++) {
			AIFace face = mesh.mFaces().get(i);
			for (int j = 0; j < face.mNumIndices(); j++) {
				indices[i * 3 + j] = face.mIndices().get(j);
			}
		}

		List<Texture> tempTextureList = new LinkedList<>();
		if (mesh.mMaterialIndex() >= 0) {
			AIMaterial material = AIMaterial.createSafe(scene.mMaterials().get(mesh.mMaterialIndex()));
			loadMaterialTextures(material, Texture.Type.DIFFUSE, tempTextureList);
			loadMaterialTextures(material, Texture.Type.SPECULAR, tempTextureList);
		}

		Texture[] textures = new Texture[tempTextureList.size()];
		int index = 0;
		for (Texture texture : tempTextureList) {
			textures[index++] = texture;
		}

		return new Mesh(indices, vertices, textures);
	}

	private void loadMaterialTextures(AIMaterial material, Texture.Type type, List<Texture> tempTextureList) throws TextureException {
		int numMaterials = aiGetMaterialTextureCount(material, type.getAssimpType());
		for (int i = 0; i < numMaterials; i++) {
			AIString path = AIString.calloc();
			aiGetMaterialTexture(material, type.getAssimpType(), i, path, (IntBuffer) null, null, null, null, null, null);
			String texturePath = directory + path.dataString();
			if (loadedTextures.contains(texturePath)) {
				continue;
			}
			Texture texture = new Texture(texturePath, type);
			texture.load();
			System.out.println("Loaded: " + texturePath);
			loadedTextures.add(texturePath);
			tempTextureList.add(texture);
		}
	}
}
