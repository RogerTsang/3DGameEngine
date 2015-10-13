package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
public class Camera implements KeyListener {
	
	private static final double NEAR = 1;
	private static final double FAR = 8;

	private float[] myBackground = new float[4];
	private double positionX, positionY, positionZ;
	private double lookatX, lookatY, lookatZ;
	private double rotateX, rotateY, rotateZ;
	
	// Default Frustum
    private double left, right, bottom, top;
    

	/**
	 * Set up background colour
	 * @param r
	 */
	public Camera(double eyeX, double eyeY, double eyeZ, double cX, double cY, double cZ) {
		myBackground[0] = 0.0f;
		myBackground[1] = 0.0f;
		myBackground[2] = 0.0f;
		myBackground[3] = 1.0f;
		positionX = eyeX;
		positionY = eyeY;
		positionZ = eyeZ;
		lookatX = cX;
		lookatY = cY;
		lookatZ = cZ;
		
		left = -3;
		right = 3;
		bottom = -4;
		top = 4;
		
		rotateX = 0;
		rotateY = 0;
		rotateZ = 0;
	}

	public float[] getBackground() {
		return myBackground;
	}

	public void setBackground(float[] background) {
		myBackground = background;
	}
	
	public double[] getEyePos() {
		double[] pos = {positionX, positionY, positionZ};
		return pos;
	}
	
	public void setEyePos(double eyeX, double eyeY, double eyeZ) {
		positionX = eyeX;
		positionY = eyeY;
		positionZ = eyeZ;
	}
	
	public double[] getAimPos() {
		double[] pos = {lookatX, lookatY, lookatZ};
		return pos;
	}
	
	public void setAimPos(double cX, double cY, double cZ) {
		lookatX = cX;
		lookatY = cY;
		lookatZ = cZ;
	}
	
	public void rotate() {
		
	}

	// ===========================================
	// COMPLETE THE METHODS BELOW
	// ===========================================

	public void setView(GL2 gl) {
		// clear the window
		gl.glClearColor(myBackground[0], myBackground[1], myBackground[2], myBackground[3]);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		// match the projection aspect ratio to the viewport
        // to avoid stretching
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        gl.glRotated(rotateX, 1, 0, 0);
        gl.glRotated(rotateY, 0, 1, 0);
        gl.glRotated(rotateZ, 0, 0, 1);
        
        //You can use an orthographic camera
        gl.glFrustum(left, right, bottom, top, NEAR, FAR);
        
		GLU glu = new GLU();
        glu.gluLookAt(positionX, positionY, positionZ, lookatX, lookatY, lookatZ, 0, 0, -1);
	}
    
	public void reshape(GL2 gl, int x, int y, int width, int height) {
		// match the projection aspect ratio to the viewport
        // to avoid stretching
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();  
        
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
        
        //You can use an orthographic camera
        gl.glFrustum(left, right, bottom, top, NEAR, FAR);
        
		GLU glu = new GLU();
        glu.gluLookAt(positionX, positionY, positionZ, lookatX, lookatY, lookatZ, 0, 0, -1); 
    }

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
        case KeyEvent.VK_W:
            positionY -= 0.1;
            break;

        case KeyEvent.VK_S:
            positionY += 0.1;
            break;
            
        case KeyEvent.VK_UP:
            positionZ -= 0.1;
            break;

        case KeyEvent.VK_DOWN:
            positionZ += 0.1;
            break;
            
        case KeyEvent.VK_LEFT:
            positionX -= 0.1;
            break;

        case KeyEvent.VK_RIGHT:
            positionX += 0.1;
            break;
            
        case KeyEvent.VK_Q:
        	rotateX += 10;
            break;
        
        case KeyEvent.VK_E:
        	rotateX -= 10;
            break;
            
        case KeyEvent.VK_D:
        	rotateY += 10;
            break;
        
        case KeyEvent.VK_A:
        	rotateY -= 10;
            break;
            
        case KeyEvent.VK_Z:
        	rotateZ += 10;
            break;
        
        case KeyEvent.VK_C:
        	rotateZ -= 10;
            break;
		} 
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
