package ass2.spec;

/**
 * COMMENT: I like slime(s) but they don't like me 
 *
 * @author Hanzhang Zeng
 */
public class Slime {

    private double[] myPos;
    
    public Slime(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    

}
