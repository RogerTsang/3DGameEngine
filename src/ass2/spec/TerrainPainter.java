package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

public class TerrainPainter {
	private ArrayList<TerrianSection> terrainSections;
	private ArrayList<TreeSection> treeSections;
	private ArrayList<RoadSection> roadSections;
	private ArrayList<SlimeSection> slimeSections;
	private double maxAl;
	private int terrainWidth;
	private int terrainHeight;
	
	public TerrainPainter(Terrain t) {
		terrainSections = new ArrayList<TerrianSection>();
		treeSections = new ArrayList<TreeSection>();
		roadSections = new ArrayList<RoadSection>();
		slimeSections = new ArrayList<SlimeSection>();
		
		maxAl = t.getMaxAltitude();
		terrainWidth = (int) t.size().getWidth();
		terrainHeight = (int) t.size().getHeight();		
		read(t);
	}
	
	public void draw(GL2 gl) {
		for (TerrianSection currentTerrian: terrainSections) {
			currentTerrian.draw(gl, true, terrainWidth, terrainHeight);
		}	
		for (TreeSection currentTree: treeSections) {
			currentTree.draw(gl);
		}
		for (RoadSection currentRoad: roadSections) {
			currentRoad.draw(gl);
		}
		for (SlimeSection currentSlime: slimeSections) {
			currentSlime.draw(gl);
		}
	}
	
	public void init(GL2 gl) {
		SlimeSection d = new SlimeSection();
		d.init(gl);
	}
	
	public void read(Terrain t) {
		int width = (int) t.size().getWidth();
		int height = (int) t.size().getHeight();
		// Calculate the each triangle position base on their (x,z) position
		for (int x = 0; x < width-1; x++) {
			for (int z = 0; z < height-1; z++) {
				double h0 = t.getGridAltitude(x+1, z);
				double h1 = t.getGridAltitude(x, z);
				double h2 = t.getGridAltitude(x, z+1);
				double h3 = t.getGridAltitude(x+1, z+1);
				double[] p0 = {x+1, h0, z};
				double[] p1 = {x, h1, z};
				double[] p2 = {x, h2, z+1};
				double[] p3 = {x+1, h3, z+1};
				terrainSections.add(new TerrianSection(p0, p1, p2, h0, h1, h2, maxAl));
				terrainSections.add(new TerrianSection(p0, p2, p3, h0, h2, h3, maxAl));
			}
		}
		//Add reading the trees from terrain
		for (Tree currentTree: t.trees()) {
			treeSections.add(new TreeSection(currentTree.getPosition()));
		}
		//Add reading the roads from terrain
		for (Road currentRoad: t.roads()) {
			roadSections.add(new RoadSection(currentRoad, t));
		}
		//Add a diamond into the game
		for (Slime currentSlime: t.slimes()) {
			slimeSections.add(new SlimeSection(currentSlime.getPosition()));
		}
	}
}
