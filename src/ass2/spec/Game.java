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

import java.util.ArrayList;

/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener {

    private Terrain myTerrain;
    private TerrainPainter myTerrainPainter;
    private Camera myCamera;
    private Texture grass;
    private Texture treeBark;
    private Texture treeLeaves;
    private Texture road;
    private Texture cardboard;
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
		textureInit(gl);
		myTerrainPainter.init(gl);
		
		// Light0: Sun
		myLights.init(gl);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		myCamera.reshape(gl, x, y, width, height);
		
		// Link textures to the world again
		textureInit(gl);
	}
	
	private void textureInit(GL2 gl) {
		// Create a new texture instance and link it to a file
		grass = new Texture(gl, "res/grass.png");
		// Link a texture to the texture manager (storage) 
		TextureMgr.instance.add(grass, "Grass"); 
		
		treeBark = new Texture(gl, "res/treeBark.png");
		TextureMgr.instance.add(treeBark, "TreeBark");
		
		treeLeaves = new Texture(gl, "res/treeLeaves.png");
		TextureMgr.instance.add(treeLeaves, "TreeLeaves");
		
		road = new Texture(gl, "res/road.png");
		TextureMgr.instance.add(road, "Road");
		
		cardboard = new Texture(gl, "res/cardboard.png");
		TextureMgr.instance.add(cardboard, "Cardboard");
	}
}
