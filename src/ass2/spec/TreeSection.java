package ass2.spec;


import java.util.HashMap;

import javax.media.opengl.GL2;

public class TreeSection {
	private double[] treeRootPosition;
	private int LSystemIterations = 3;
	private LSystem branchLSystem;
	
	private static final float[] diffuseCoeffientStump = {0.3f, 0.1f, 0.0f, 1.0f};
	private static final float[] specularCoeffientStump = {0.5f, 0.5f, 0.5f, 1.0f};
	private static final float[] diffuseCoeffientLeaves = {0.0f, 0.5f, 0.0f, 1.0f};
	private static final float[] specularCoeffientLeaves = {0.5f, 0.5f, 0.5f, 1.0f};
	
	public TreeSection(double[] position, LSystem ls) {
		treeRootPosition = position;
		branchLSystem = ls;
	}
	
	public void increaseIteration() {
		if (LSystemIterations < Integer.MAX_VALUE) {
			LSystemIterations++;
		}
	}
	
	public void decreaseIteration() {
		if (LSystemIterations > 0) {
			LSystemIterations--;
		}
	}
	
	//Draws the tree
	public void draw(GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(treeRootPosition[0], treeRootPosition[1], treeRootPosition[2]);
		gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
				
		//Generates a tree using L system
		
		String treeStart = "B";
		HashMap<Character, String> treeRules = new HashMap<Character, String>();
		treeRules.put('B', "AM[TB]R[TB]R[TD]");
		treeRules.put('D', "C[MUB]N[TB]R[TB]R[TB]");
		//LSystem branchLSystem = new LSystem(treeStart, treeRules);
		branchLSystem.addFirst(treeStart);
		branchLSystem.addRules(treeRules);
		String instructions = branchLSystem.getIteration(LSystemIterations);
		
		for (int i = 0; i < instructions.length(); i++) {
			switch (instructions.charAt(i)) {
			case 'A':
				//Draws only a branch of relative length 1
				drawCylinder(gl, 1);
				break;
			case 'B':
				//Draws a branch of length 1 with leaves around it
				drawCylinder(gl, 1);
				drawLeaves(gl, 1);
				break;
			case 'C':
				//Draws only a branch of relative length 1.5
				drawCylinder(gl, 1.5);
				break;
			case 'D':
				//Draws a branch of length 1.5 with leaves around it
				drawCylinder(gl, 1.5);
				drawLeaves(gl, 1.5);
				break;
			case 'M':
				//Translates and scales so that next branch can be drawn on properly
				gl.glTranslated(0, 1, 0);
				gl.glScaled(0.5, 0.5, 0.5);
				break;
			case 'N':
				//Same as Q, but for C/D branches
				gl.glTranslated(0, 1.5, 0);
				gl.glScaled(0.5, 0.5, 0.5);
				break;
			case 'R':
				//Rotates by 120 degrees on y axis
				gl.glRotated(120, 0, 1, 0);
				break;
			case 'T':
				//Tilts branch outwards on x axis and aligns it into previous branch
				gl.glTranslated(-0.1, -0.1, 0);
				gl.glRotated(20, 0, 0, 1);
				break;
			case 'U':
				//Skews branch for a C/D branch
				gl.glRotated(-40, 0, 0, 1);
				break;
			case '[':
				gl.glPushMatrix();
				break;
			case ']':
				gl.glPopMatrix();
			}
		}
		gl.glPopMatrix();
	}
	
	private void drawCylinder(GL2 gl, double height) {
		final int numVertices = 32;
		final double radius = 0.1;
		double increment = 2*Math.PI/numVertices;
		double[] currentNormal = new double[3];
		double[] currentP0 = new double[3];
		double[] currentP1 = new double[3];
		double[] currentP2 = new double[3];
		double[] currentP3 = new double[3];
		
		//Initialises the bark texture for the stump of the tree
		TextureMgr.instance.activate(gl, "TreeBark");
		
		//Sets the material for the stump to be brown
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeffientStump, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeffientStump, 0);
		//Draws the stump of the tree
		for (int i = 0; i < numVertices; i++) {
			currentP0[0] = radius*Math.cos(i*increment); currentP0[1] = 0; currentP0[2] = radius*Math.sin(i*increment);
			currentP1[0] = radius*Math.cos(i*increment); currentP1[1] = height; currentP1[2] = radius*Math.sin(i*increment);
			currentP2[0] = radius*Math.cos((i+1)*increment); currentP2[1] = height; currentP2[2] = radius*Math.sin((i+1)*increment);
			currentP3[0] = radius*Math.cos((i+1)*increment); currentP3[1] = 0; currentP3[2] = radius*Math.sin((i+1)*increment);
			currentNormal = MathUtil.getNormal(currentP0, currentP1, currentP2);
			currentNormal = MathUtil.normalise(currentNormal);
			
			TextureMgr.instance.activate(gl, "TreeBark");
			gl.glBegin(GL2.GL_POLYGON);
			gl.glNormal3dv(currentNormal, 0);
			gl.glTexCoord2d((double) i/numVertices,0);
			gl.glVertex3d(currentP0[0], currentP0[1], currentP0[2]);
			gl.glTexCoord2d((double) i/numVertices, 1);
			gl.glVertex3d(currentP1[0], currentP1[1], currentP1[2]);
			gl.glTexCoord2d((double) (i+1)/numVertices, 1);
			gl.glVertex3d(currentP2[0], currentP2[1], currentP2[2]);
			gl.glTexCoord2d((double) (i+1)/numVertices, 0);
			gl.glVertex3d(currentP3[0], currentP3[1], currentP3[2]);
			gl.glEnd();
		}
		drawCircle(gl, height);
	}
	
	private void drawCircle(GL2 gl, double height) {
		final int numVertices = 32;
		final double radius = 0.1;
		double increment = 2*Math.PI/numVertices;
		double[] currentNormal = new double[3];
		currentNormal[0] = 0; currentNormal[1] = 1; currentNormal[2] = 0;
		
		gl.glBegin(GL2.GL_POLYGON);
		gl.glNormal3dv(currentNormal, 0);
		for (int i = 0; i < numVertices; i++) {
			gl.glTexCoord2d(Math.cos(i*increment), Math.sin(i*increment));
			gl.glVertex3d(radius*Math.cos(i*increment), height, radius*Math.sin(i*increment));
		}
		gl.glEnd();
	}
	
	/*
	 * Draws leaves onto a branch
	 */
	private void drawLeaves (GL2 gl, double height) {
		gl.glPushMatrix();
		gl.glTranslated(0.1, height, 0);
		
		double[] normal = {1,0,0};
		
		TextureMgr.instance.activate(gl, "TreeLeaves");
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeffientLeaves, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeffientLeaves, 0);
		for (int i = 0; i < 3; i++) {
			gl.glRotated(-20, 0, 0, 1);
			
			//All normals for each leaf is the same, as the coordinate frame is being modified such that n = (1,0,0), facing out from the x axis
			//whenever a leaf is drawn.
			gl.glBegin(GL2.GL_POLYGON);
			gl.glNormal3dv(normal, 0);
			gl.glTexCoord2d(0.5, 0);
			gl.glVertex3d(0, 0, 0);
			gl.glTexCoord2d(0.3333, 0.1667);
			gl.glVertex3d(0, 0.1, -0.1);
			gl.glTexCoord2d(0.3333, 0.6667);
			gl.glVertex3d(0, 0.4, -0.1);
			gl.glTexCoord2d(0.5, 1);
			gl.glVertex3d(0, 0.6, 0);
			gl.glTexCoord2d(0.6667, 0.6667);
			gl.glVertex3d(0, 0.4, 0.1);
			gl.glTexCoord2d(0.6667, 0.1667);
			gl.glVertex3d(0, 0.1, 0.1);
			gl.glEnd();
			
			gl.glRotated(20, 0, 0, 1);
			
			int j = 0;
			while (j < 3) {
				gl.glTranslated(-0.1, 0, -0.1);
				gl.glRotated(90, 0, 1, 0);
				gl.glRotated(-20, 0, 0, 1);
				gl.glBegin(GL2.GL_POLYGON);
				gl.glTexCoord2d(0.5, 0);
				gl.glVertex3d(0, 0, 0);
				gl.glTexCoord2d(0.3333, 0.1667);
				gl.glVertex3d(0, 0.1, -0.1);
				gl.glTexCoord2d(0.3333, 0.6667);
				gl.glVertex3d(0, 0.4, -0.1);
				gl.glTexCoord2d(0.5, 1);
				gl.glVertex3d(0, 0.6, 0);
				gl.glTexCoord2d(0.6667, 0.6667);
				gl.glVertex3d(0, 0.4, 0.1);
				gl.glTexCoord2d(0.6667, 0.1667);
				gl.glVertex3d(0, 0.1, 0.1);
				gl.glEnd();
				gl.glRotated(20, 0, 0, 1);
				j++;
			}
			
			//Move the coordinate frame down the trunk by 0.4 to draw the next layer of leaves
			gl.glTranslated(0,-0.4,0);
		}
		
		gl.glPopMatrix();
	}
}
