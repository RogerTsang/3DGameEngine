package ass2.spec;

import javax.media.opengl.GL2;

public class Lights {
	private Terrain map;
	
	public Lights(Terrain t) {
		map = t;
	}
	
	public void init(GL2 gl) {
		// Light 0 : SUN
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_NORMALIZE);
	}
	
	public void drawSun(GL2 gl) {
		float[] pos1 = map.getSunlight();
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos1, 0);
		
		float[] spe = {0.0f, 0.0f, 0.0f, 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spe, 0);
		
		float[] dif = {1.0f, 1.0f, 1.0f, 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, dif, 0);
	}
}
