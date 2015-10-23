package ass2.spec;

/**
 * COMMENT: Pond without fish
 *
 * @author Hanzhang Zeng
 */
public class Pond {

	/*
	 * p0 ----------- 
	 *  |             |
	 *  |             |
	 *  |             |
	 *  |             |
	 *  |             |
	 *  |             |
	 *    ----------- p1
	 */
    private double[] p0, p1 = new double[3];
    
    public Pond(double[] p0, double[] p1) {
        this.p0 = p0;
        this.p1 = p1;
    }
    
    public double[] getPosition() {
    	double[] p = {p0[0], p0[1], p0[2], p1[0], p1[1], p1[2]};
        return p;
    }
    
}
