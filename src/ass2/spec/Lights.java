package ass2.spec;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Lights {
	private Terrain map;
	private int step;
	private static final boolean NIGHT = true;
	private static final boolean DRAWSUN = true;
	private static final float CONSTANT_A = 0.08f;
	private static final float LINEAR_A = 0.04f;
	private static final float QUADRATIC_A = 0.02f;
	private static float[] sun_pos;
	
	public Lights(Terrain t) {
		map = t;
		step = 0;
		sun_pos = map.getSunlight();
	}
	
	public void init(GL2 gl) {
		// Light 0 : SUN
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE);
	}
	
	public void drawSun(GL2 gl) {
		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_CONSTANT_ATTENUATION, CONSTANT_A);
		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_LINEAR_ATTENUATION, LINEAR_A);
		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_QUADRATIC_ATTENUATION, QUADRATIC_A);
		float[] ptLightSource = {sun_pos[0], sun_pos[1], sun_pos[2], 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, ptLightSource, 0);
		
		
		float change; // Daylight Simulator
		if (NIGHT) {
			step = step % 3600;
			change = (float) Math.sin(step++/1800.0*Math.PI);
		} else {
			change = 1f;
		}
		
		float[] spe = {0.3f, 0.3f, 0.3f, 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spe, 0);
		
		float[] amb = {0.05f+change*0.1f, 0.05f+change*0.1f, 0.05f+change*0.1f, 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, amb, 0);
		
		float[] dif = {0.5f+change*0.5f+0.4f, 0.5f+change*0.5f+0.2f, 0.5f+change*0.5f, 0.1f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, dif, 0);
		
		if (DRAWSUN) {
			
			gl.glPushMatrix();
			GLUT glut = new GLUT();
			float[] ambientCoeff = { 0.5f, 0.5f, 0.5f, 1.0f };
			float[] diffuseCoeff = { 0.7f, 0.7f, 0.7f, 1.0f };
			float[] specularCoeff = { 0.7f, 0.7f, 0.3f, 1.0f };
			float emissionCoeff = 5f;
			TextureMgr.instance.deactivate(gl);
			gl.glColor3f(1f, 0.5f, 0f);
			gl.glTranslated(sun_pos[0], sun_pos[1], sun_pos[2]);
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambientCoeff, 0);
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specularCoeff, 0);
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuseCoeff, 0);
			gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, emissionCoeff);
			// Because the light is inside the sun
			// So take negative value as radius will correct the sun face
			glut.glutSolidSphere(-0.5, 8, 8);
			gl.glPopMatrix();
		}
	}
}
