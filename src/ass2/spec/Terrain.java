package ass2.spec;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private List<Diamond> myDiamonds;
    private float[] mySunlight;
    private double maxAltitude;

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        myDiamonds = new ArrayList<Diamond>();
        mySunlight = new float[3];
        maxAltitude = 0;
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }
    
    public List<Diamond> diamonds() {
        return myDiamonds;
    }

    public float[] getSunlight() {
        return mySunlight;
    }
    
    public double getMaxAltitude() {
    	return maxAltitude;
    }
    
    public double[] getCentre() {
    	double centreX = mySize.getWidth()/2.0;
    	double centreZ = mySize.getHeight()/2.0;
    	double centreY = altitude(centreX, centreZ);
    	double[] centre = {centreX, centreY, centreZ};
    	return centre;
    }
    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
        // Catch the maxAltitude
        maxAltitude = maxAltitude < h ? h : maxAltitude;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
        double altitude = 0;
        
        if (x < 0 || x > mySize.getWidth()-1 || z < 0 || z > mySize.getHeight()-1) {
        	return 0;
        }
        
        // Depth Interpolation
        int xCeil  = (int) Math.ceil(x);
        int xFloor = (int) Math.floor(x);
        int zCeil  = (int) Math.ceil(z);
        int zFloor = (int) Math.floor(z);
        double absX = (double) (xCeil - xFloor)*1.0;
        double absZ = (double) (zCeil - zFloor)*1.0;
        /*
        (XFloor,0,ZFloor)  (XCeil,0.5,ZFloor)   
				      +-----+  
				      L o  R|  
				      |  /  |
				      |L  x R
				      +-----+
	    (XFloor,0,ZCeil)   (XCeil,0.3,ZCeil)
	    
	    * If X > 0.5, We will take the bottom-right triangle
	    * Vice Versa
     	*/
        if (x == xFloor && z == zFloor) {
        	altitude = myAltitude[xFloor][zFloor];
        } else if (x == xFloor && z != zFloor) {
        	// On the edge which paralleled to x
        	altitude = ((z-zFloor)*myAltitude[xFloor][zCeil]+
        				(zCeil-z)*myAltitude[xFloor][zFloor])/absZ*1.0;        	
        } else if (z == zFloor && x != xFloor) {
        	// On the edge which paralleled to z
        	altitude = ((x-xFloor)*myAltitude[xCeil][zFloor]+
    					(xCeil-x)*myAltitude[xFloor][zFloor])/absX*1.0;
    	} else if (x - xFloor > 0.5) {
        	// Find the altitude at R
        	double altiR = ((z-zFloor)*myAltitude[xCeil][zCeil]+
        				   (zCeil-z)*myAltitude[xCeil][zFloor])/absZ*1.0;
        	// Find the altitude at L
        	double altiL = ((z-zFloor)*myAltitude[xFloor][zCeil]+
 				   		   (zCeil-z)*myAltitude[xCeil][zFloor])/absZ*1.0;
        	double xL = (xCeil - absX / absZ * (z - zFloor))* 1.0;
        	altitude = ((x - xL)*altiR + (xCeil - x)*altiL)/(xCeil - xL)*1.0;
        } else {
        	// Find the altitude at R
        	double altiR = ((z-zFloor)*myAltitude[xFloor][zCeil]+
        				   (zCeil-z)*myAltitude[xCeil][zFloor])/absZ*1.0;
        	// Find the altitude at L
        	double altiL = ((z-zFloor)*myAltitude[xFloor][zCeil]+
 				   		   (zCeil-z)*myAltitude[xFloor][zFloor])/absZ*1.0;
        	double xR = (xFloor + absX / absZ * (zCeil - z))* 1.0;
        	altitude = ((x - xFloor)*altiR + (xR - x)*altiL)/(xR - xFloor)*1.0;
        }
        return altitude;
    }
    
    public void display(GL2 gl) {
    	
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }
    
    /**
     * Add a diamond which is implemented in VBO 
     * 
     * @param x
     * @param z
     */
    public void addDiamond(double x, double y, double z) {
        Diamond diamond = new Diamond(x, y, z);
        myDiamonds.add(diamond);
    }


}
