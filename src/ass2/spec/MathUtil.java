package ass2.spec;

public class MathUtil {
	
	/**
     * Normalise an angle to the range [-180, 180)
     * 
     * @param angle 
     * @return
     */
    static public double normaliseAngle(double angle) {
    	return ((angle + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
    }
    
    /**
     * Get magnitude for a given 3D vector
     * @param n 3D vector
     * @return
     */
	public static double getMagnitude(double [] n) {
    	double mag = n[0]*n[0] + n[1]*n[1] + n[2]*n[2];
    	mag = Math.sqrt(mag);
    	return mag;
    }
    
	/**
	 * Normailse a 3D vector
	 * @param n 3D vector
	 * @return
	 */
	public static double [] normalise(double [] n) {
    	double  mag = getMagnitude(n);
    	double norm[] = {n[0]/mag,n[1]/mag,n[2]/mag};
    	return norm;
    }
    
	/**
	 * Cross Product
	 * @param u vector on lhs
	 * @param v vector on rhs
	 * @return
	 */
	public static double [] cross(double u [], double v[]) {
    	double crossProduct[] = new double[3];
    	crossProduct[0] = u[1]*v[2] - u[2]*v[1];
    	crossProduct[1] = u[2]*v[0] - u[0]*v[2];
    	crossProduct[2] = u[0]*v[1] - u[1]*v[0];
    	//System.out.println("CP " + crossProduct[0] + " " +  crossProduct[1] + " " +  crossProduct[2]);
    	return crossProduct;
    }
    
	/**
	 * Get a normal from 3 vertexes in anti-clockwise order
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double [] getNormal(double[] p0, double[] p1, double[] p2) {
    	double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
    	double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
    	
    	return cross(u,v);
    }
}
