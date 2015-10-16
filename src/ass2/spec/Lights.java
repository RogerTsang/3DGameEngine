package ass2.spec;

import javax.media.opengl.GL2;

public class Lights {
	private Terrain map;
	private int step;
	private static final boolean NIGHT = true;
	
	public Lights(Terrain t) {
		map = t;
		step = 0;
	}
	
	public void init(GL2 gl) {
		// Light 0 : SUN
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_NORMALIZE);
	}
	
	public void drawSun(GL2 gl) {
		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_CONSTANT_ATTENUATION, 2F);
		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_LINEAR_ATTENUATION, 1F);
		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_QUADRATIC_ATTENUATION, 0.5F);
		
		float[] pos0 = map.getSunlight();
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos0, 0);
		
		float[] spe = {0.0f, 0.0f, 0.0f, 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spe, 0);
		
		
		float change; // Daylight Simulator
		if (NIGHT) {
			step = step % 3600;
			change = (float) Math.sin(step++/1800.0*Math.PI);
		} else {
			change = 1f;
		}
		
		float[] amb = {0.05f+change*0.1f, 0.05f+change*0.1f, 0.05f+change*0.1f, 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, amb, 0);
		
		float[] dif = {0.5f+change*0.5f+0.4f, 0.5f+change*0.5f+0.2f, 0.5f+change*0.5f, 0.1f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, dif, 0);
	}
}
