package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener{

    private Terrain myTerrain;
    //private Camera myCamera;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        //myCamera = new Camera();
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
        // Test altitude Function
        // System.out.println(terrain.altitude(1, 0.9));
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

        // set the view matrix based on the camera position
        //myCamera.setView(gl); 
        
    	gl.glBegin(GL2.GL_TRIANGLES);
    	// Triangle Test
    	{    
    		//red triangle at front
    	    gl.glColor3f(1f,0f,0f);
    	    //CCW ordering of vertices
    	    double p0[] = {0,2,1}; 
    	    double p1[] = {0,3,0};
    	    double p2[] = {1,2,0};
    	    //Can pass in an array and an offset into the array
    	    
    	    gl.glVertex3dv(p0,0);
    	    gl.glVertex3dv(p1,0);
    	    gl.glVertex3dv(p2,0);
    	}
    	gl.glEnd();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// tell the camera and the mouse that the screen has reshaped
        GL2 gl = drawable.getGL().getGL2();
        //myCamera.reshape(gl, x, y, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();  
        
        //You can use an orthographic camera
        gl.glOrtho(-5, 5, -5, 5, 1, 20);
        GLU glu = new GLU();
        double centerX = myTerrain.size().getWidth()/2.0;
        double centerZ = myTerrain.size().getHeight()/2.0;
        glu.gluLookAt(0.0, 5.0, 0.0, centerX, 0.0, centerZ, 0, 0, -1);
	}
}
