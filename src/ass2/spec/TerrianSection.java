package ass2.spec;

import javax.media.opengl.GL2;

public class TerrianSection {
	private double[] p0, p1, p2;
	private double c0, c1, c2; // Altitude Test
	private double[] normal;   //So far, we use face normals
	
	public TerrianSection (double[] v0, double[] v1, double[] v2, 
			double h0, double h1, double h2, double maxAl) {
		this.p0 = v0;
		this.p1 = v1;
		this.p2 = v2;
		this.c0 = h0*1.0/maxAl;
		this.c1 = h1*1.0/maxAl;
		this.c2 = h2*1.0/maxAl; 
		normal = MathUtil.getNormal(p0, p1, p2);
		normal = MathUtil.normalise(normal);
		
	}
	
	public void draw(GL2 gl) {
		// Draw lower layer triangles
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glBegin(GL2.GL_TRIANGLES);	{
			gl.glNormal3dv(normal,0);
			gl.glColor4d(c0, c0, c0, 1);
			gl.glVertex3dv(p0, 0);
			gl.glColor4d(c1, c1, c1, 1);			
			gl.glVertex3dv(p1, 0);
			gl.glColor4d(c2, c2, c2, 1);			
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
