package ass2.spec;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Mine {

    private double[] myPos;
    
    public Mine(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    

}
