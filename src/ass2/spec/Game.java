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
    private TerrainPainter myTerrainPainter;
    private Camera myCamera;
    

    public Game(Terrain terrain) {
    	super("Assignment 2");
    	
        myTerrain = terrain;
        myTerrainPainter = new TerrainPainter(myTerrain);
        
        // Create a camera
        double eyeX = myTerrain.size().getWidth()/2;
        double eyeY = myTerrain.getMaxAltitude()*2;
        double eyeZ = myTerrain.size().getHeight()/2;
        double centreX = eyeX;
        double centreY = myTerrain.getMaxAltitude()/2;
        double centreZ = eyeZ;
        myCamera = new Camera(myTerrain);
        
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
          
          // Add Keyboard Event Listener
          panel.addKeyListener(myCamera);
 
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
        myCamera.setView(gl); 
        
		//Drawing Terrain
		myTerrainPainter.draw(gl);
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
        myCamera.reshape(gl, x, y, width, height);
	}
}
