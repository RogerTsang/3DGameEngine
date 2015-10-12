package ass2.spec;

import javax.media.opengl.GL2;

public class TerrianSection {
	private static final double MAX_ALTITUDE = 2.0;
	private double[] p0, p1, p2;
	private double h0, h1, h2; // Altitude Test
	private double[] normal;   //So far, we use face normals
	
	public TerrianSection (double[] v0, double[] v1, double[] v2, double h0, double h1, double h2) {
		this.p0 = v0;	this.p1 = v1;	this.p2 = v2;
		this.h0 = h0;	this.h1 = h1;	this.h2 = h2; 
		normal = MathUtil.getNormal(p0, p1, p2);
		normal = MathUtil.normalise(normal);
	}
	
	public void draw(GL2 gl) {
		// Draw lower layer triangles
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glBegin(GL2.GL_TRIANGLES);	{
			gl.glNormal3dv(normal,0);
			gl.glColor4d(h0/MAX_ALTITUDE, h0/MAX_ALTITUDE, h0/MAX_ALTITUDE, 1);
			gl.glVertex3dv(p0, 0);
			gl.glColor4d(h1/MAX_ALTITUDE, h1/MAX_ALTITUDE, h1/MAX_ALTITUDE, 1);
			gl.glVertex3dv(p1, 0);
			gl.glColor4d(h2/MAX_ALTITUDE, h2/MAX_ALTITUDE, h2/MAX_ALTITUDE, 1);
			gl.glVertex3dv(p2, 0);
		}
		gl.glEnd();

		// Test the Color if it is null
		// Draw upper layer outline
		gl.glBegin(GL2.GL_LINE_STRIP); {
			gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
			gl.glVertex3dv(p0, 0);
			gl.glVertex3dv(p1, 0);
			gl.glVertex3dv(p2, 0);
		}
		gl.glEnd();
	}
}
