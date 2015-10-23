package ass2.spec;

import javax.media.opengl.GL2;

public class RoadSection {
	
	Road road;
	Terrain terrain;
	Boolean extrusionDisabled;
	
	private static final float[] diffuseCoeffient = {0.2f, 0.2f, 0.2f, 1.0f};
	private static final float[] specularCoeffient = {0.5f, 0.5f, 0.5f, 1.0f};
	
	public RoadSection(Road r, Terrain t) {
		road = r;
		terrain = t;
		extrusionDisabled = false;
	}
	
	public void changeExtrusion() {
		extrusionDisabled = !extrusionDisabled;
	}
	
	public void draw(GL2 gl) {
		
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeffient, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeffient, 0);
		
		TextureMgr.instance.activate(gl, "Road");
		
		double prevPointX = road.controlPoint(0)[0];
		double prevPointZ = road.controlPoint(0)[1];
		double prevPointY = terrain.altitude(prevPointX, prevPointZ);
		double currPointX;
		double currPointZ;
		double currPointY;
		
		double[] prevQuadPoint0 = new double[2];
		double[] prevQuadPoint1 = new double[2];
		
		double[] currentNormal = new double[3];
		double textureRatio = 0;
		if (!extrusionDisabled) {
			gl.glBegin(GL2.GL_QUAD_STRIP);
			for (double i = 0.05; i <= 1; i += 0.05) {
				//Set current point as the next point in the Bezier curve
				currPointX = road.point(i)[0];
				currPointZ = road.point(i)[1];
				currPointY = terrain.altitude(currPointX, currPointZ);
				//Calculates the 4 points which consists of the rectangle between the two points on the Bezier curve
				double[] quadPoints = calculateQuad(prevPointX, prevPointZ, currPointX, currPointZ, road.width());
				double[] v0 = {quadPoints[0], prevPointY, quadPoints[1]};
				double[] v1 = {quadPoints[2], currPointY, quadPoints[3]};
				double[] v2 = {quadPoints[4], currPointY, quadPoints[5]};
				
				currentNormal = MathUtil.getNormal(v0, v1, v2);
				currentNormal = MathUtil.normalise(currentNormal);
				gl.glNormal3dv(currentNormal, 0);
				
				//The quad strips are off the surface by 0.01 in order to not overlap with the terrain.
				//This is for the first two vertices of the starting quad
				if (i == 0.05) {
					gl.glTexCoord2d(0,0);
					gl.glVertex3d(quadPoints[0], prevPointY + 0.01, quadPoints[1]);
					gl.glTexCoord2d(0,1);
					gl.glVertex3d(quadPoints[2], currPointY + 0.01, quadPoints[3]);
				}
				
				//Determines the ratio of the current quad's length to its width to draw the texture on properly
				textureRatio += Math.sqrt((currPointX - prevPointX)*(currPointX - prevPointX) + (currPointZ - prevPointZ)*(currPointZ - prevPointZ))/road.width();

				//Adds the next two points for the quad strip and applies texture map
				gl.glTexCoord2d(textureRatio,0);
				gl.glVertex3d(quadPoints[4], currPointY + 0.01, quadPoints[5]);
				gl.glTexCoord2d(textureRatio,1);
				gl.glVertex3d(quadPoints[6], prevPointY + 0.01, quadPoints[7]);
				
				prevPointX = currPointX;
				prevPointY = currPointY;
				prevPointZ = currPointZ;
			}		
			gl.glEnd();
		} else {
			for (double i = 0.05; i < 1; i += 0.05) {
				//Set current point as the next point in the Bezier curve
				currPointX = road.point(i)[0];
				currPointZ = road.point(i)[1];
				currPointY = terrain.altitude(currPointX, currPointZ);
				double[] quadPoints = calculateQuad(prevPointX, prevPointZ, currPointX, currPointZ, road.width());
				
				gl.glNormal3dv(currentNormal, 0);
				
				//if it is the first iteration, set points for the rectangles, otherwise use the last two points instead
				if (i == 0.05) {
					double[] vector0 = {quadPoints[0], quadPoints[1]};
					double[] vector1 = {quadPoints[2], quadPoints[3]};
					double[] vector2 = {quadPoints[4], quadPoints[5]};
					double[] vector3 = {quadPoints[6], quadPoints[7]};
					drawSection(gl, vector0, vector1, vector2, vector3);
				} else {
					double[] vector0 = prevQuadPoint0;
					double[] vector1 = prevQuadPoint1;
					double[] vector2 = {quadPoints[4], quadPoints[5]};
					double[] vector3 = {quadPoints[6], quadPoints[7]};
					drawSection(gl, vector0, vector1, vector2, vector3);
				}
				
				prevQuadPoint0[0] = quadPoints[0];
				prevQuadPoint0[1] = quadPoints[1];
				prevQuadPoint1[0] = quadPoints[2];
				prevQuadPoint1[1] = quadPoints[3];
				
				prevPointX = currPointX;
				prevPointY = currPointY;
				prevPointZ = currPointZ;
			}		
		}
		
		
	}
	
	private void drawSection(GL2 gl, double[] quadPoint0, double[] quadPoint1, double[] quadPoint2, double[] quadPoint3) {
		//P0 = (x0, z0), P1 = (x1, z1), P2 = (x2, z2), P3 = (x3, z3)
		//distance between P0 and P1: sqrt((x1-x0)^2 + (z1-z0)^2)
		double width = Math.sqrt((quadPoint1[0]-quadPoint0[0])*(quadPoint1[0]-quadPoint0[0]) + (quadPoint1[1]-quadPoint0[1])*(quadPoint1[1]-quadPoint0[1]));
		
		//distance between P0 and P2: sqrt((x2-x0)^2 + (z2-z0)^2)
		double length1 = Math.sqrt((quadPoint2[0]-quadPoint0[0])*(quadPoint2[0]-quadPoint0[0]) + (quadPoint2[1]-quadPoint0[1])*(quadPoint2[1]-quadPoint0[1]));
		//distance between P1 and P3: sqrt((x3-x1)^2 + (z3-z1)^2)
		double length2 = Math.sqrt((quadPoint3[0]-quadPoint1[0])*(quadPoint3[0]-quadPoint1[0]) + (quadPoint3[1]-quadPoint1[1])*(quadPoint3[1]-quadPoint1[1]));
		
		final int widthIncrements = 10*((int)width+1);
		final int lengthIncrements = 10*((int)Math.max(length1, length2)+1);
		
		//widthVector is a vector
		double[] widthVector = {(quadPoint1[0] - quadPoint0[0])/widthIncrements, (quadPoint1[1] - quadPoint0[1])/widthIncrements};
		double[] lengthVector = {(quadPoint2[0] - quadPoint0[0])/lengthIncrements, (quadPoint2[1] - quadPoint0[1])/lengthIncrements};
		double[] previousPoint = new double[3];
		for (int i = 0; i < lengthIncrements; i++) {
			//Draw a quad strip for each small segment of the length
			gl.glBegin(GL2.GL_QUAD_STRIP);
			previousPoint[0] = quadPoint0[0] + i*lengthVector[0];
			previousPoint[2] = quadPoint0[1] + i*lengthVector[1];
			previousPoint[1] = terrain.altitude(previousPoint[0], previousPoint[2]);
			
			for (int j = 0; j <= widthIncrements; j++) {
				//Calculates the next two points as a linear combination of 
				//Interpolates the length as the length of each segment may not be equal (width is equal, no need to interpolate)
				double lengthFactor = ((1-j/widthIncrements)*length1 + (j/widthIncrements)*length2)/length1;
				double currPoint0X = quadPoint0[0] + j*widthVector[0] + i*lengthFactor*lengthVector[0];
				double currPoint0Z = quadPoint0[1] + j*widthVector[1] + i*lengthFactor*lengthVector[1];
				double currPoint0Y = terrain.altitude(currPoint0X, currPoint0Z) + 0.1;
				double currPoint1X = quadPoint0[0] + j*widthVector[0] + (i+1)*lengthFactor*lengthVector[0];
				double currPoint1Z = quadPoint0[1] + j*widthVector[1] + (i+1)*lengthFactor*lengthVector[1];
				double currPoint1Y = terrain.altitude(currPoint1X, currPoint1Z) + 0.1;
				
				double[] currentPoint0 = {currPoint0X, currPoint0Y, currPoint0Z};
				double[] currentPoint1 = {currPoint1X, currPoint1Y, currPoint1Z};
				double[] currentNormal = MathUtil.getNormal(previousPoint, currentPoint0, currentPoint1);
				currentNormal = MathUtil.normalise(currentNormal);
				
				//Add the next two points to the quad strip
				gl.glNormal3dv(currentNormal, 0);
				gl.glTexCoord2d((double)i*length1*lengthFactor/(width*lengthIncrements), (double) j/widthIncrements);
				gl.glVertex3d(currPoint0X, currPoint0Y, currPoint0Z);
				gl.glTexCoord2d(((double)i+1)*length1*lengthFactor/(width*lengthIncrements), (double) j/widthIncrements);
				gl.glVertex3d(currPoint1X, currPoint1Y, currPoint1Z);
				
				previousPoint[0] = currPoint0X;
				previousPoint[1] = currPoint0Y;
				previousPoint[2] = currPoint0Z;
			}
			gl.glEnd();
		}
	}
	
	private double[] calculateQuad(double previousX, double previousZ, double currentX, double currentZ, double width) {
		//Obtain the normal vector of the line from (previousX, previouZ) to (currentX, currentZ)
		double normalX = -(currentZ - previousZ);
		double normalZ = (currentX - previousX);
		double modulusNormal = Math.sqrt(normalX*normalX + normalZ*normalZ);
		normalX /= modulusNormal;
		normalZ /= modulusNormal;
		
		//All these points are simply vector additions of the two points in the Bezier curve
		//added to the normal vector, which has half the length of the width
		double point0X = previousX - width*normalX/2;
		double point0Z = previousZ - width*normalZ/2;
		double point1X = previousX + width*normalX/2;
		double point1Z = previousZ + width*normalZ/2;
		double point2X = currentX - width*normalX/2;
		double point2Z = currentZ - width*normalZ/2;
		double point3X = currentX + width*normalX/2;
		double point3Z = currentZ + width*normalZ/2;

		double[] quadPoints = {point0X, point0Z, point1X, point1Z, point2X, point2Z, point3X, point3Z};
		return quadPoints;
	}
}
