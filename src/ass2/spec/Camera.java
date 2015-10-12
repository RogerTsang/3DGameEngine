package ass2.spec;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * The camera is a GameObject that can be moved, rotated and scaled like any
 * other.
 * 
 * TODO: You need to implment the setView() method. The methods you need to
 * complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class Camera {

	private float[] myBackground = new float[4];
	private double scale;
	private double rotation;
	private double positionX;
	private double positionY;
	private double positionZ;

	/**
	 * Set up background colour
	 * @param r
	 */
	public Camera(float r, float g, float b, float alpha) {
		myBackground[0] = r;
		myBackground[1] = g;
		myBackground[2] = b;
		myBackground[3] = alpha;
		scale = 1.0;
		rotation = 0.0;
		positionX = 0.0;
		positionY = 0.0;
		positionZ = 0.0;
	}

	public Camera() {
		myBackground[0] = 0.0f;
		myBackground[1] = 0.0f;
		myBackground[2] = 0.0f;
		myBackground[3] = 1.0f;
		scale = 1.0;
		rotation = 0.0;
		positionX = 0.0;
		positionY = 0.0;
		positionZ = 0.0;
	}

	public float[] getBackground() {
		return myBackground;
	}

	public void setBackground(float[] background) {
		myBackground = background;
	}

	// ===========================================
	// COMPLETE THE METHODS BELOW
	// ===========================================

	public void setView(GL2 gl) {
		// TODO 1. clear the view to the background colour
		// clear the window
		gl.glClearColor(myBackground[0], myBackground[1], myBackground[2], myBackground[3]);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		// TODO 2. set the view matrix to account for the camera's position
		// Load and initialise the model view matrix
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		double camScale = 1.0 / scale;
		double camRotate = -rotation;
		double camX = -positionX;
		double camY = -positionY;
		double camZ = -positionZ;
		gl.glScaled(camScale, camScale, camScale);
		gl.glRotated(camRotate, 1, 0, 0);
		System.out.println(camRotate);
		gl.glTranslated(camX, camY, camZ);
	}
    
	public void reshape(GL2 gl, int x, int y, int width, int height) {
        // match the projection aspect ratio to the viewport
        // to avoid stretching
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        double top, bottom, left, right;
        
        if (width > height) {
            double aspect = (1.0 * width) / height;
            top = 1.0;
            bottom = -1.0;
            left = -aspect;
            right = aspect;            
        }
        else {
            double aspect = (1.0 * height) / width;
            top = aspect;
            bottom = -aspect;
            left = -1;
            right = 1;                        
        }
        
        GLU myGLU = new GLU();
        // coordinate system (left, right, bottom, top)
        myGLU.gluOrtho2D(left, right, bottom, top);
    }
}
