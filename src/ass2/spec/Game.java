package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener {

    private Terrain myTerrain;
    private TerrainPainter myTerrainPainter;
    private Camera myCamera;
    private Lights myLights;

    public Game(Terrain terrain) {
    	super("Assignment 2");
    	
        myTerrain = terrain;
        myTerrainPainter = new TerrainPainter(myTerrain);
        
        // Create a camera
        myCamera = new Camera(myTerrain);
        
        myLights = new Lights(myTerrain);
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
          
          // Add Keyboard Event Listener to camera and terrain painter
          panel.addKeyListener(myCamera);
          panel.addKeyListener(myTerrainPainter);
 
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
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		// Default Matrix Mode should be GL2.GL_MODELVIEW
		gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
		// set the view matrix based on the camera position
		myCamera.setView(gl); 
		
        //Lights rendering before Geometry
        myLights.drawSun(gl);

        //Drawing Terrain
		myTerrainPainter.draw(gl);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		// Enable depth test
		gl.glEnable(GL2.GL_DEPTH_TEST);
		
		// Enable texture
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		// Enable Blending
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_BLEND);
		
		// Enable Cull Face
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glCullFace(GL2.GL_BACK);
		
		// Initialise textures
		textureInit(gl);
		
		// INitialise terrain
		myTerrainPainter.init(gl);
		
		// Light0: Sun
		myLights.init(gl);
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		myCamera.reshape(gl, x, y, width, height);
		
	}
	
	private void textureInit(GL2 gl) {
		// Create a new texture instance and link it to a file
		Texture grass = new Texture(gl, "res/grass.png");
		// Link a texture to the texture manager (storage) 
		TextureMgr.instance.add(grass, "Grass"); 
		
		Texture treeBark = new Texture(gl, "res/treeBark.png");
		TextureMgr.instance.add(treeBark, "TreeBark");
		
		Texture treeLeaves = new Texture(gl, "res/treeLeaves.png");
		TextureMgr.instance.add(treeLeaves, "TreeLeaves");
		
		Texture road = new Texture(gl, "res/road.png");
		TextureMgr.instance.add(road, "Road");
		
		Texture cardboard = new Texture(gl, "res/cardboard.png");
		TextureMgr.instance.add(cardboard, "Cardboard");
		
		Texture slime = new Texture(gl, "res/slime.png");
		TextureMgr.instance.add(slime, "Slime");
		
		Texture pond = new Texture(gl, "res/water.jpg");
		TextureMgr.instance.add(pond, "Water");
	}
}
