package ass2.spec;

public class TerrainPainter {
	private double[] p0;
	private double[] p1;
	private double[] p2;
	
	public TerrainPainter() {
		
	}
	
	double getMagnitude(double [] n) {
    	double mag = n[0]*n[0] + n[1]*n[1] + n[2]*n[2];
    	mag = Math.sqrt(mag);
    	return mag;
    }
    
    double [] normalise(double [] n) {
    	double  mag = getMagnitude(n);
    	double norm[] = {n[0]/mag,n[1]/mag,n[2]/mag};
    	return norm;
    }
    
    double [] cross(double u [], double v[]) {
    	double crossProduct[] = new double[3];
    	crossProduct[0] = u[1]*v[2] - u[2]*v[1];
    	crossProduct[1] = u[2]*v[0] - u[0]*v[2];
    	crossProduct[2] = u[0]*v[1] - u[1]*v[0];
    	//System.out.println("CP " + crossProduct[0] + " " +  crossProduct[1] + " " +  crossProduct[2]);
    	return crossProduct;
    }
    
    double [] getNormal(double[] p0, double[] p1, double[] p2) {
    	double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
    	double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
    	
    	return cross(u,v);
    }
    
    
}
