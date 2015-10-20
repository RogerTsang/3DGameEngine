package ass2.spec;

import javax.media.opengl.GL2;

public class Avatar {
	private static final double[] normalUp = {0, 1, 0};
	private static final double[] normalDown = {0, -1, 0};
	private static final double[] t0 = {0.5, 0.5, -0.5};
	private static final double[] t1 = {-0.5, 0.5, -0.5};
	private static final double[] t2 = {-0.5, 0.5, 0.5};
	private static final double[] t3 = {0.5, 0.5, 0.5};
	private static final double[] b0 = {0.5, 0, -0.5};
	private static final double[] b1 = {-0.5, 0, -0.5};
	private static final double[] b2 = {-0.5, 0, 0.5};
	private static final double[] b3 = {0.5, 0, 0.5};
	private static double[] nface1;
	private static double[] nface2;
	private static double[] nface3;
	private static double[] nface4;

	private static final float[] diffuseCoeff = { 0.74f, 0.51f, 0.1f, 1.0f };
	private static final float[] specularCoeff = { 0.2f, 0.2f, 0.2f, 1.0f };

	public Avatar() {
		// Calculate the normal each face normal
		nface1 = MathUtil.getNormal(t1, t0, b0);
		nface2 = MathUtil.getNormal(t2, t1, b1);
		nface3 = MathUtil.getNormal(t3, t2, b2);
		nface4 = MathUtil.getNormal(t0, t3, b3);
		nface1 = MathUtil.normalise(nface1);
		nface2 = MathUtil.normalise(nface2);
		nface3 = MathUtil.normalise(nface3);
		nface4 = MathUtil.normalise(nface4);
	}

	public void draw(GL2 gl, double[] position, double yRotateAngle) {
		gl.glPushMatrix();
		
		gl.glTranslated(position[0], position[1], position[2]);
		gl.glRotated(yRotateAngle, 0, 1, 0);
		
		gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
		
		//Activate texture
		TextureMgr.instance.activate(gl, "Cardboard");
		
		drawface1(gl);
		drawface2(gl);
		drawface3(gl);
		drawface4(gl);
		drawfacetop(gl);
		drawfacedown(gl);
		
		gl.glPopMatrix();
	}
	
	public void drawMaterial(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeff, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeff, 0);
	}
	
	private void drawface1(GL2 gl) {
		gl.glBegin(GL2.GL_POLYGON);
		{
			// Set up material
			drawMaterial(gl);

			// Set up surface normal
			gl.glNormal3dv(nface1, 0);
			
			// Draw surface
			gl.glTexCoord2d(1, 0.75);
			gl.glVertex3dv(b1, 0);
			gl.glTexCoord2d(1, 1);
			gl.glVertex3dv(t1, 0);
			gl.glTexCoord2d(0, 1);
			gl.glVertex3dv(t0, 0);
			gl.glTexCoord2d(0, 0.75);
			gl.glVertex3dv(b0, 0);
		}
		gl.glEnd();
	}
	
	private void drawface2(GL2 gl) {
		gl.glBegin(GL2.GL_POLYGON);
		{
			// Set up material
			drawMaterial(gl);

			// Set up surface normal
			gl.glNormal3dv(nface2, 0);
			
			// Draw surface
			gl.glTexCoord2d(1, 0.5);
			gl.glVertex3dv(b2, 0);
			gl.glTexCoord2d(1, 0.75);
			gl.glVertex3dv(t2, 0);
			gl.glTexCoord2d(0, 0.75);
			gl.glVertex3dv(t1, 0);
			gl.glTexCoord2d(0, 0.5);
			gl.glVertex3dv(b1, 0);
		}
		gl.glEnd();
	}
	
	private void drawface3(GL2 gl) {
		gl.glBegin(GL2.GL_POLYGON);
		{
			// Set up material
			drawMaterial(gl);

			// Set up surface normal
			gl.glNormal3dv(nface3, 0);
			
			// Draw surface
			gl.glTexCoord2d(1, 0.75);
			gl.glVertex3dv(b3, 0);
			gl.glTexCoord2d(1, 1);
			gl.glVertex3dv(t3, 0);
			gl.glTexCoord2d(0, 1);
			gl.glVertex3dv(t2, 0);
			gl.glTexCoord2d(0, 0.75);
			gl.glVertex3dv(b2, 0);
		}
		gl.glEnd();
	}
	
	private void drawface4(GL2 gl) {
		gl.glBegin(GL2.GL_POLYGON);
		{
			// Set up material
			drawMaterial(gl);

			// Set up surface normal
			gl.glNormal3dv(nface2, 0);
			
			// Draw surface
			gl.glTexCoord2d(1, 0.75);
			gl.glVertex3dv(b0, 0);
			gl.glTexCoord2d(1, 1);
			gl.glVertex3dv(t0, 0);
			gl.glTexCoord2d(0, 1);
			gl.glVertex3dv(t3, 0);
			gl.glTexCoord2d(0, 0.75);
			gl.glVertex3dv(b3, 0);
		}
		gl.glEnd();
	}
	
	private void drawfacetop(GL2 gl) {
		gl.glBegin(GL2.GL_TRIANGLES);
		{
			// Set up material
			drawMaterial(gl);

			// Set up surface normal
			gl.glNormal3dv(normalUp, 0);
			
			// Draw surface
			gl.glTexCoord2d(1, 0);
			gl.glVertex3dv(t2, 0);
			gl.glTexCoord2d(0, 0.5);
			gl.glVertex3dv(t0, 0);
			gl.glTexCoord2d(0, 0);
			gl.glVertex3dv(t1, 0);
		}
		gl.glEnd();

		gl.glBegin(GL2.GL_TRIANGLES);
		{
			// Set up material
			drawMaterial(gl);

			// Set up surface normal
			gl.glNormal3dv(normalUp, 0);
			
			// Draw surface
			gl.glTexCoord2d(1, 0.5);
			gl.glVertex3dv(t3, 0);
			gl.glTexCoord2d(0, 0.5);
			gl.glVertex3dv(t0, 0);
			gl.glTexCoord2d(1, 0);
			gl.glVertex3dv(t2, 0);
		}
		gl.glEnd();
	}
	
	private void drawfacedown(GL2 gl) {
		gl.glBegin(GL2.GL_TRIANGLES);
		{
			// Set up material
			drawMaterial(gl);

			// Set up surface normal
			gl.glNormal3dv(normalDown, 0);
			
			// Draw surface
			gl.glTexCoord2d(0, 0.5);
			gl.glVertex3dv(b2, 0);
			gl.glTexCoord2d(1, 0);
			gl.glVertex3dv(b0, 0);
			gl.glTexCoord2d(1, 0.5);
			gl.glVertex3dv(b1, 0);
		}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_TRIANGLES);
		{
			// Set up material
			drawMaterial(gl);

			// Set up surface normal
			gl.glNormal3dv(normalDown, 0);
			
			// Draw surface
			gl.glTexCoord2d(0, 0);
			gl.glVertex3dv(b3, 0);
			gl.glTexCoord2d(1, 0);
			gl.glVertex3dv(b0, 0);
			gl.glTexCoord2d(0, 0.5);
			gl.glVertex3dv(b2, 0);
		}
		gl.glEnd();
	}
}
