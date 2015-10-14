package ass2.spec;

import javax.media.opengl.GL2;

public class TreeSection {
	private double[] treeRootPosition;
	
	private static final float[] diffuseCoeffientStump = {0.3f, 0.1f, 0.0f, 1.0f};
	private static final float[] specularCoeffientStump = {0.5f, 0.5f, 0.5f, 1.0f};
	private static final float[] diffuseCoeffientLeaves = {0.0f, 0.5f, 0.0f, 1.0f};
	private static final float[] specularCoeffientLeaves = {0.5f, 0.5f, 0.5f, 1.0f};
	
	public TreeSection(double[] position) {
		treeRootPosition = position;
	}
	
	//Draws the tree
	public void draw(GL2 gl) {
		final int numVertices = 32;
		final double radius = 0.1;
		double increment = 2*Math.PI/numVertices;
		double[] currentNormal = new double[3];
		double[] currentP0 = new double[3];
		double[] currentP1 = new double[3];
		double[] currentP2 = new double[3];
		double[] currentP3 = new double[3];
		
		//Sets the material for the stump to be brown
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeffientStump, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeffientStump, 0);
		//Draws the stump of the tree
		for (int i = 0; i < numVertices; i++) {
			currentP0[0] = treeRootPosition[0] + radius*Math.cos(i*increment); currentP0[1] = treeRootPosition[1]; currentP0[2] = treeRootPosition[2] + radius*Math.sin(i*increment);
			currentP1[0] = treeRootPosition[0] + radius*Math.cos(i*increment); currentP1[1] = treeRootPosition[1] + 1; currentP1[2] = treeRootPosition[2] + radius*Math.sin(i*increment);
			currentP2[0] = treeRootPosition[0] + radius*Math.cos((i+1)*increment); currentP2[1] = treeRootPosition[1] + 1; currentP2[2] = treeRootPosition[2] + radius*Math.sin((i+1)*increment);
			currentP3[0] = treeRootPosition[0] + radius*Math.cos((i+1)*increment); currentP3[1] = treeRootPosition[1]; currentP3[2] = treeRootPosition[2] + radius*Math.sin((i+1)*increment);
			currentNormal = MathUtil.getNormal(currentP0, currentP1, currentP2);
			currentNormal = MathUtil.normalise(currentNormal);
			
			gl.glBegin(GL2.GL_POLYGON);
			gl.glNormal3dv(currentNormal, 0);
			gl.glVertex3d(treeRootPosition[0] + radius*Math.cos(i*increment), treeRootPosition[1], treeRootPosition[2] + radius*Math.sin(i*increment));
			gl.glVertex3d(treeRootPosition[0] + radius*Math.cos(i*increment), treeRootPosition[1] + 1, treeRootPosition[2] + radius*Math.sin(i*increment));
			gl.glVertex3d(treeRootPosition[0] + radius*Math.cos((i+1)*increment), treeRootPosition[1] + 1, treeRootPosition[2] + radius*Math.sin((i+1)*increment));
			gl.glVertex3d(treeRootPosition[0] + radius*Math.cos((i+1)*increment), treeRootPosition[1], treeRootPosition[2] + radius*Math.sin((i+1)*increment));
			gl.glEnd();
		}
		
		//Sets the colour of the leaves
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeffientLeaves, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeffientLeaves, 0);
		
		//Draws a sphere out of squares
		final int numVerticesSphere = 16;
		final double sphereRadius = 0.3;
		double sphereIncrement = 2*Math.PI/numVerticesSphere;
		
		for (int i = 0; i <= numVerticesSphere; i++) {
			//Using spherical coordinates based on i, which controls phi and j, which controls theta
			//x = r*cos(theta)*sin(phi)
			//z = r*sin(theta)*sin(phi)
			//y = r*cos(phi)
			//theta: [0,360]
			//phi: [0,180]
			for (int j = 0; j < numVerticesSphere; j++) {
				currentP0[0] = treeRootPosition[0] + sphereRadius*Math.cos(j*sphereIncrement)*Math.sin(i*sphereIncrement/2); 
				currentP0[1] = treeRootPosition[1] + 1 + sphereRadius + sphereRadius*Math.cos(i*sphereIncrement/2); 
				currentP0[2] = treeRootPosition[2] + sphereRadius*Math.sin(j*sphereIncrement)*Math.sin(i*sphereIncrement/2);
				
				currentP1[0] = treeRootPosition[0] + sphereRadius*Math.cos((j+1)*sphereIncrement)*Math.sin(i*sphereIncrement/2); 
				currentP1[1] = treeRootPosition[1] + 1 + sphereRadius + sphereRadius*Math.cos(i*sphereIncrement/2); 
				currentP1[2] = treeRootPosition[2] + sphereRadius*Math.sin((j+1)*sphereIncrement)*Math.sin(i*sphereIncrement/2);
				
				currentP2[0] = treeRootPosition[0] + sphereRadius*Math.cos((j+1)*sphereIncrement)*Math.sin((i+1)*sphereIncrement/2); 
				currentP2[1] = treeRootPosition[1] + 1 + sphereRadius + sphereRadius*Math.cos((i+1)*sphereIncrement/2); 
				currentP2[2] = treeRootPosition[2] + sphereRadius*Math.sin((j+1)*sphereIncrement)*Math.sin((i+1)*sphereIncrement/2);
				
				currentP3[0] = treeRootPosition[0] + sphereRadius*Math.cos(j*sphereIncrement)*Math.sin((i+1)*sphereIncrement/2); 
				currentP3[1] = treeRootPosition[1] + 1 + sphereRadius + sphereRadius*Math.cos((i+1)*sphereIncrement/2); 
				currentP3[2] = treeRootPosition[2] + sphereRadius*Math.sin(j*sphereIncrement)*Math.sin((i+1)*sphereIncrement/2);
				
				currentNormal = MathUtil.getNormal(currentP0, currentP2, currentP3);
				currentNormal = MathUtil.normalise(currentNormal);
				
				gl.glBegin(GL2.GL_POLYGON);
				gl.glNormal3dv(currentNormal, 0);
				gl.glVertex3d(currentP0[0], currentP0[1], currentP0[2]);
				gl.glVertex3d(currentP1[0], currentP1[1], currentP1[2]);
				gl.glVertex3d(currentP2[0], currentP2[1], currentP2[2]);
				gl.glVertex3d(currentP3[0], currentP3[1], currentP3[2]);
				gl.glEnd();
			}
			
		}
	}
	
}
