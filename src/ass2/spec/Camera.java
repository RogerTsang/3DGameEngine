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
	private static final double FAR = 10;
	private static final double SPEED = 0.1;
	private static final double CAMERA_HEIGHT = 2;
	
	private Terrain map;
	
	private double positionX, positionY, positionZ;
	private double stepX, stepY, stepZ;
	private double lookatX, lookatY, lookatZ;
	private double rotateY, VAngle;
	
	// Default Frustum
    private double left, right, bottom, top;
    

	/**
	 * Set up background colour
	 * @param r
	 */
	public Camera(Terrain t) {
		map = t;
		
		double eye[] = t.getCentre();
		positionX = eye[0];
		positionY = eye[1] + CAMERA_HEIGHT;
		positionZ = eye[2];
		
		// Looking at positive X direction
		updateStep();
		updateLookAt();
		
		left = -3;
		right = 3;
		bottom = -4;
		top = 4;
		
		rotateY = 0;
		VAngle = 0; //Vertical Angle
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
	
	public void rotateHorizontal(double amount) {
		rotateY += amount;
		rotateY = MathUtil.normaliseAngle(rotateY);
	}
	
	public void rotateVertical(double amount) {
		VAngle += amount;
		if (VAngle > 90) {
			VAngle = 90;
		} else if (VAngle < -90) {
			VAngle = -90;
		}
	}
	
	public void updateStep() {
		stepX = Math.cos(rotateY*1.0/2.0/Math.PI);
		stepY = Math.sin(VAngle*1.0/2.0/Math.PI);
		stepZ = -Math.sin(rotateY*1.0/2.0/Math.PI);
		double[] step = {stepX, stepY, stepZ};
		step = MathUtil.normalise(step);
		stepX = SPEED * step[0];
		stepY = SPEED * step[1];
		stepZ = SPEED * step[2];
	}
	
	public void updateLookAt() {
		lookatX = positionX + stepX;
		lookatY = positionY + stepY;
		lookatZ = positionZ + stepZ;
	}

	// ===========================================
	// COMPLETE THE METHODS BELOW
	// ===========================================

	public void setView(GL2 gl) {
		// clear the window
		gl.glClearColor(0,0,0,1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		// match the projection aspect ratio to the viewport
        // to avoid stretching
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        gl.glRotated(rotateY, 0, 1, 0);
        
        //You can use an orthographic camera
        gl.glFrustum(-1, 1, -1, 1, NEAR, FAR);
        
		GLU glu = new GLU();
        glu.gluLookAt(positionX, positionY, positionZ, lookatX, lookatY, lookatZ, 0, 1, 0);
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
        gl.glFrustum(-1, 1, -1, 1, NEAR, FAR);
        
		GLU glu = new GLU();
        glu.gluLookAt(positionX, positionY, positionZ, lookatX, lookatY, lookatZ, 0, 1, 0); 
    }

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
        case KeyEvent.VK_W:
            positionX += stepX;
            positionY += stepY;
            positionZ += stepZ;
            break;

        case KeyEvent.VK_S:
        	positionX -= stepX;
        	positionY -= stepY;
            positionZ -= stepZ;
            break;

        case KeyEvent.VK_LEFT:
        	rotateHorizontal(0.5);
            break;
        
        case KeyEvent.VK_RIGHT:
        	rotateHorizontal(-0.5);
            break;
            
        case KeyEvent.VK_UP:
        	rotateVertical(0.5);
        	break;
        	
        case KeyEvent.VK_DOWN:
        	rotateVertical(-0.5);
        	break;
            
        case KeyEvent.VK_SPACE:
        	positionY += 0.1;
        	break;
        	
        case KeyEvent.VK_CONTROL:
        	positionY -= 0.1;
        	break;
		}
		updateStep();
		updateLookAt();
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
