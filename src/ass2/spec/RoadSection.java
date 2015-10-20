package ass2.spec;

import javax.media.opengl.GL2;

public class RoadSection {
	
	Road road;
	Terrain terrain;
	
	private static final float[] diffuseCoeffient = {0.2f, 0.2f, 0.2f, 1.0f};
	private static final float[] specularCoeffient = {0.5f, 0.5f, 0.5f, 1.0f};
	
	public RoadSection(Road r, Terrain t) {
		road = r;
		terrain = t;
	}
	
	public void draw(GL2 gl) {
		
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeffient, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeffient, 0);
		
		TextureMgr.instance.activate(gl, "Road");
		
		gl.glBegin(GL2.GL_QUAD_STRIP);
		double prevPointX = road.controlPoint(0)[0];
		double prevPointZ = road.controlPoint(0)[1];
		double prevPointY = terrain.altitude(prevPointX, prevPointZ);
		double currPointX;
		double currPointZ;
		double currPointY;
		double[] currentNormal = new double[3];
		double textureRatio = 0;
		for (double i = 0; i < 1; i += 0.05) {
			currPointX = road.point(i)[0];
			currPointZ = road.point(i)[1];
			currPointY = terrain.altitude(currPointX, currPointZ);
			double[] quadPoints = calculateQuad(prevPointX, prevPointZ, currPointX, currPointZ, road.width());
			double[] v0 = {quadPoints[0], prevPointY, quadPoints[1]};
			double[] v1 = {quadPoints[2], currPointY, quadPoints[3]};
			double[] v2 = {quadPoints[4], currPointY, quadPoints[5]};
			
			currentNormal = MathUtil.getNormal(v0, v1, v2);
			currentNormal = MathUtil.normalise(currentNormal);
			
			gl.glNormal3dv(currentNormal, 0);
			
			//The quad strips are off the surface by 0.01 in order to not overlap with the terrain.
			//This is for the first two vertices of the starting quad
			if (i == 0) {
				gl.glTexCoord2d(0,0);
				gl.glVertex3d(quadPoints[0], prevPointY + 0.01, quadPoints[1]);
				gl.glTexCoord2d(0,1);
				gl.glVertex3d(quadPoints[2], currPointY + 0.01, quadPoints[3]);
			}
			
			//Determines the ratio of the current quad's length to its width to draw the texture on properly
			textureRatio += Math.sqrt((currPointX - prevPointX)*(currPointX - prevPointX) + (currPointZ - prevPointZ)*(currPointZ - prevPointZ))/road.width();

			gl.glTexCoord2d(textureRatio,1);
			gl.glVertex3d(quadPoints[4], currPointY + 0.01, quadPoints[5]);
			gl.glTexCoord2d(textureRatio,0);
			gl.glVertex3d(quadPoints[6], prevPointY + 0.01, quadPoints[7]);
			
			prevPointX = currPointX;
			prevPointY = currPointY;
			prevPointZ = currPointZ;
			
		}
		gl.glEnd();
	}
	
	public double[] calculateQuad(double previousX, double previousZ, double currentX, double currentZ, double width) {
		double normalX = -(currentZ - previousZ);
		double normalZ = (currentX - previousX);
		double modulusNormal = Math.sqrt(normalX*normalX + normalZ*normalZ);
		normalX /= modulusNormal;
		normalZ /= modulusNormal;
		
		double point0X = previousX - width*normalX;
		double point0Z = previousZ - width*normalZ;
		double point1X = previousX + width*normalX;
		double point1Z = previousZ + width*normalZ;
		double point2X = currentX - width*normalX;
		double point2Z = currentZ - width*normalZ;
		double point3X = currentX + width*normalX;
		double point3Z = currentZ + width*normalZ;
		
		double[] quadPoints = {point0X, point0Z, point1X, point1Z, point2X, point2Z, point3X, point3Z};
		return quadPoints;
	}
}
