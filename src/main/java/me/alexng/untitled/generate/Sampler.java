package me.alexng.untitled.generate;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that samples points from a field. All coordinates are positive (subject to change).
 */
public class Sampler {

	private final int x, y, width, height, numPointsX, numPointsY, totalWidth, totalHeight;

	public Sampler(int x, int y, int width, int height, int numPointsX, int numPointsY, int totalWidth, int totalHeight) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.numPointsX = numPointsX;
		this.numPointsY = numPointsY;
		this.totalWidth = totalWidth;
		this.totalHeight = totalHeight;
	}

	public Sampler(int x, int y, int width, int height, int totalWidth, int totalHeight) {
		this(x, y, width, height, width, height, totalWidth, totalHeight);
	}

	public Sampler(int width, int height) {
		this(0, 0, width, height, width, height, width, height);
	}

	public List<Point> generatePoints() {
		// TODO: This is very very slow.
		List<Point> points = new ArrayList<>(numPointsX * numPointsY);
		float sampleDistanceX = width / (float) numPointsX;
		float sampleDistanceY = height / (float) numPointsY;
		float dy = 0;
		int indexY = 0;
		while (dy < height) {
			float dx = 0;
			int indexX = 0;
			while (dx < width) {
				points.add(new Point((int) (x + dx), (int) (y + dy), indexX, indexY));
				dx += sampleDistanceX;
				indexX++;
			}
			indexY++;
			dy += sampleDistanceY;
		}
		return points;
	}

	public boolean containsIndex(Point point) {
		return point.getIndexX() >= 0 && point.getIndexY() >= 0 && point.getIndexX() < numPointsX && point.getIndexY() < numPointsY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getNumPointsX() {
		return numPointsX;
	}

	public int getNumPointsY() {
		return numPointsY;
	}

	public int getTotalWidth() {
		return totalWidth;
	}

	public int getTotalHeight() {
		return totalHeight;
	}

	/**
	 * Returns the number of points that will be sampled by this Sampler.
	 */
	public int getSize() {
		return numPointsX * numPointsY;
	}
}
