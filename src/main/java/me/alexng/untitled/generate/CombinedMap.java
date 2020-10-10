package me.alexng.untitled.generate;

public class CombinedMap {

	private final Sampler sampler;
	private final LandmassMap landmassMap;
	private final HeightMap heightMap;
	private final TemperatureMap temperatureMap;
	private final MoistureMap moistureMap;
	private final BiomeMap biomeMap;

	public CombinedMap(Sampler sampler) {
		this.sampler = sampler;
		this.landmassMap = new LandmassMap(sampler);
		this.heightMap = new HeightMap(landmassMap);
		this.temperatureMap = new TemperatureMap(heightMap);
		this.moistureMap = new MoistureMap(heightMap);
		this.biomeMap = new BiomeMap(heightMap, temperatureMap, moistureMap);
	}

	public void generate(int seed) {
		System.out.println("Generating map");
		long start = System.nanoTime();
		long subStart = start;
		landmassMap.generate(seed);
		System.out.println("Landmass: " + (System.nanoTime() - subStart) / 1000000000f);
		subStart = System.nanoTime();
		heightMap.generate(seed);
		System.out.println("Height: " + (System.nanoTime() - subStart) / 1000000000f);
		subStart = System.nanoTime();
		temperatureMap.generate(seed);
		System.out.println("Temperature: " + (System.nanoTime() - subStart) / 1000000000f);
		subStart = System.nanoTime();
		moistureMap.generate(seed);
		System.out.println("Moisture: " + (System.nanoTime() - subStart) / 1000000000f);
		subStart = System.nanoTime();
		biomeMap.generate(seed);
		System.out.println("Biome: " + (System.nanoTime() - subStart) / 1000000000f);
		System.out.println("Total: " + (System.nanoTime() - start) / 1000000000f);
	}

	public CombinedMap sample(int x, int y, int width, int height, int numPointsX, int numPointsY) {
		return new CombinedMap(new Sampler(x, y, width, height, numPointsX, numPointsY, getSampler().getTotalWidth(), getSampler().getTotalHeight()));
	}

	public CombinedMap sample(int x, int y, int width, int height) {
		return new CombinedMap(new Sampler(x, y, width, height, width, height, getSampler().getTotalWidth(), getSampler().getTotalHeight()));
	}

	public CombinedMap sample(int numPointsX, int numPointsY) {
		return new CombinedMap(new Sampler(0, 0, getWidth(), getHeight(), numPointsX, numPointsY, getSampler().getTotalWidth(), getSampler().getTotalHeight()));
	}

	public Sampler getSampler() {
		return sampler;
	}

	public LandmassMap getLandmassMap() {
		return landmassMap;
	}

	public HeightMap getHeightMap() {
		return heightMap;
	}

	public TemperatureMap getTemperatureMap() {
		return temperatureMap;
	}

	public MoistureMap getMoistureMap() {
		return moistureMap;
	}

	public BiomeMap getBiomeMap() {
		return biomeMap;
	}

	public int getWidth() {
		return sampler.getNumPointsX();
	}

	public int getHeight() {
		return sampler.getNumPointsY();
	}
}
