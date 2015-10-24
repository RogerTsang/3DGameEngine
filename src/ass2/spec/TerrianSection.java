package ass2.spec;

import javax.media.opengl.GL2;

public class TerrianSection {
	private double[] p0, p1, p2;
	private double c0, c1, c2; // Altitude Test
	private double[] normal; // So far, we use face normals

	private static final float[] diffuseCoeff = { 0.2f, 0.6f, 0.3f, 1.0f };
	private static final float[] specularCoeff = { 0.0f, 0.0f, 0.0f, 1.0f };

	public TerrianSection(double[] v0, double[] v1, double[] v2, double h0, double h1, double h2, double maxAl) {
		this.p0 = v0;
		this.p1 = v1;
		this.p2 = v2;
		// Before applying texture onto terrain
		// I render it with grey scale depends on their altitude
		// to test my terrain painter;
		this.c0 = h0 * 1.0 / maxAl;
		this.c1 = h1 * 1.0 / maxAl;
		this.c2 = h2 * 1.0 / maxAl;
		normal = MathUtil.getNormal(p0, p1, p2);
		normal = MathUtil.normalise(normal);
	}

	public void draw(GL2 gl, boolean texture, int terrainWidth, int terrainHeight) {
		// Draw lower layer triangles
		gl.glPushMatrix();
		gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
		
		//Activate texture
		TextureMgr.instance.activate(gl, "Grass");
		
		gl.glBegin(GL2.GL_TRIANGLES);
		{
			// Set up material
			drawMaterial(gl);

			// Set up surface
			drawSurface(gl, texture, terrainWidth, terrainHeight);
		}
		gl.glEnd();
		gl.glPopMatrix();
	}
	
	public void drawMaterial(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeff, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeff, 0);
	}
	
	public void drawSurface(GL2 gl, boolean texture, int tw, int th) {
		int textureWidth = 0;
		int textureHeight = 0;
		if (texture) {
			textureWidth = TextureMgr.instance.getWidth("Grass");
			textureHeight = TextureMgr.instance.getHeight("Grass");
		}
		
		gl.glNormal3dv(normal, 0);
		
		if (!texture) {
			gl.glColor4d(c0, c0, c0, 1);
		} else {
			double w = p0[0] / textureWidth * tw;
			double h = p0[2] / textureHeight * th;
			gl.glTexCoord2d(w, h);
		}
		gl.glVertex3dv(p0, 0);
		
		if (!texture) {
			gl.glColor4d(c1, c1, c1, 1);
		} else {
			double w = p1[0] / textureWidth * tw;
			double h = p1[2] / textureHeight * th;
			gl.glTexCoord2d(w, h);
		}
		
		gl.glVertex3dv(p1, 0);
		
		if (!texture) {
			gl.glColor4d(c2, c2, c2, 1);
		} else {
			double w = p2[0] / textureWidth * tw;
			double h = p2[2] / textureHeight * th;
			gl.glTexCoord2d(w, h);
		}
		gl.glVertex3dv(p2, 0);
	}
}
