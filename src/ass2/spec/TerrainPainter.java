package ass2.spec;

import java.util.ArrayList;

import javax.media.opengl.GL2;

public class TerrainPainter {
	private ArrayList<TerrianSection> ts;
	private ArrayList<TreeSection> treeSections;
	private double maxAl;
	public TerrainPainter(Terrain t) {
		
		ts = new ArrayList<TerrianSection>();
		treeSections = new ArrayList<TreeSection>();
		
		maxAl = t.getMaxAltitude();
		read(t);
	}
	
	public void draw(GL2 gl) {
		for (TerrianSection ets: ts) {
			ets.draw(gl);
		}
		for (TreeSection currentTree: treeSections) {
			currentTree.draw(gl);
		}
	}
	
	public void read(Terrain t) {
		int width = (int) t.size().getWidth();
		int height = (int) t.size().getHeight();
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
				ts.add(new TerrianSection(p0, p1, p2, h0, h1, h2, maxAl));
				ts.add(new TerrianSection(p0, p2, p3, h0, h2, h3, maxAl));
			}
		}
		//Add reading the trees from terrain
		for (Tree currentTree: t.trees()) {
			treeSections.add(new TreeSection(currentTree.getPosition()));
		}
	}
}
