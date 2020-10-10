package me.alexng.untitled.generate;

import me.alexng.untitled.render.Texture;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public abstract class MapData {

	private final float[] data;
	private final Sampler sampler;

	public MapData(Sampler sampler) {
		this.sampler = sampler;
		this.data = new float[getSize()];
	}

	public abstract void setupGeneration(int seed);

	public abstract void generatePoint(Point point);

	public Texture toTextureRGB(Texture texture) {
		final int PIXEL_WIDTH = 3;
		ByteBuffer buffer = MemoryUtil.memAlloc(getSize() * PIXEL_WIDTH);
		for (int i = 0; i < getSize(); i++) {
			Vector3f color = toColor(i);
			buffer.put((byte) color.x);
			buffer.put((byte) color.y);
			buffer.put((byte) color.z);
		}
		buffer.flip();
		texture.load(getWidth(), getHeight(), buffer);
		buffer.clear();
		MemoryUtil.memFree(buffer);
		return texture;
	}

	public Texture toTextureRGB(Texture.Type type) {
		return toTextureRGB(new Texture(type));
	}

	public void setData(float value, Point point) {
		setData(value, point.getIndexX(), point.getIndexY());
	}

	private void setData(float value, int x, int y) {
		data[getIndex(x, y)] = value;
	}

	public Vector3f toColor(int i) {
		float normalizedData = getDataNormalized(i);
		float greyScale = normalizedData * 255;
		return new Vector3f(greyScale, greyScale, greyScale);
	}

	public Vector3f toColor(int x, int y) {
		return toColor(getIndex(x, y));
	}

	int getIndex(int x, int y) {
		return y * getWidth() + x;
	}

	float getData(int i) {
		return data[i];
	}

	float getData(int x, int y) {
		return getData(getIndex(x, y));
	}

	public float getData(Point point) {
		return getData(getIndex(point.getIndexX(), point.getIndexY()));
	}

	/**
	 * Normalized to range (0, 1) from range (-1, 1).
	 */
	public float getDataNormalized(int i) {
		return NoiseHelper.normalize(getData(i));
	}

	public float getDataNormalized(int x, int y) {
		return getDataNormalized(getIndex(x, y));
	}

	public float getDataNormalized(Point point) {
		return getDataNormalized(getIndex(point.getIndexX(), point.getIndexY()));
	}

	public Sampler getSampler() {
		return sampler;
	}

	public int getWidth() {
		return sampler.getNumPointsX();
	}

	public int getHeight() {
		return sampler.getNumPointsY();
	}

	public int getSize() {
		return sampler.getSize();
	}
}
