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
	
	private static final double NEAR = 0.1;
	private static final double FAR = 10;
	private static final double SPEED = 0.1;
	private static final double CAMERA_HEIGHT = 1;
	private static final double FPSCAMERA_OFFSET_SCALE = 10;
	
	private Terrain map;
	private Avatar myAvatar;
	private boolean firstPerson;
	private static boolean flightMode;
	
	private double positionX, positionY, positionZ;
	private double stepX, stepY, stepZ, leftX, leftZ;
	private double lookatX, lookatY, lookatZ;
	private double rotateY, VAngle;
	
	private boolean keys[];
	
	/**
	 * Set up background colour
	 * @param r
	 */
	public Camera(Terrain t) {
		map = t;
		myAvatar = new Avatar();
		
		double eye[] = t.getCentre();
		positionX = eye[0];
		positionY = eye[1] + CAMERA_HEIGHT;
		positionZ = eye[2];
		
		// Looking at positive X direction
		updateStep();
		updateLookAt();
		
		firstPerson = true;
		flightMode = false;
		rotateY = 0;
		VAngle = 0; //Vertical Angle
		
		keys = new boolean[256];
		for (int i = 0; i < 256; i++) {
			keys[i] = false;
		}
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
		stepX = Math.cos(rotateY/180*Math.PI);
		stepY = Math.sin(VAngle/180*Math.PI);
		stepZ = -Math.sin(rotateY/180*Math.PI);
		
		double[] up = {0, 1, 0};
    	double[] aim = {stepX, stepY, stepZ};
    	double[] left = MathUtil.cross(up, aim);
    	
    	left = MathUtil.normalise(left);
    	leftX = left[0] * SPEED;
    	leftZ = left[2] * SPEED;
    	
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

	public void setView(GL2 gl) {
		readKey();
		
		// clear the window
		gl.glClearColor(0,0,0.2f,1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
        // Torch not working yet
		GLU glu = new GLU();
		if (firstPerson) {
			glu.gluLookAt(positionX, positionY, positionZ, lookatX, lookatY, lookatZ, 0, 1, 0);
		} else {
			glu.gluLookAt(positionX-stepX*FPSCAMERA_OFFSET_SCALE, 
						  positionY+1, positionZ-stepZ*FPSCAMERA_OFFSET_SCALE, lookatX, lookatY, lookatZ, 0, 1, 0);
		}
        
        // Draw Avatar
        if (!firstPerson) {
        	double[] position = {positionX+stepX*FPSCAMERA_OFFSET_SCALE, positionY-1, positionZ+stepZ*FPSCAMERA_OFFSET_SCALE};
            myAvatar.draw(gl, position, rotateY);
        }
	}
    
	public void reshape(GL2 gl, int x, int y, int width, int height) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        GLU glu = new GLU();
        glu.gluPerspective(60.0, (float)width/(float)height, NEAR, FAR);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void readKey() {
        if (keys[KeyEvent.VK_W]) {
            positionX += stepX;
            positionZ += stepZ;
            if (flightMode) {            	
            	positionY += stepY;
            } else {
            	positionY = map.altitude(positionX, positionZ) + CAMERA_HEIGHT;
            }
        }
        
        if (keys[KeyEvent.VK_S]) {
        	positionX -= stepX;
            positionZ -= stepZ;
            if (flightMode) {            	
            	positionY -= stepY;
            } else {
            	positionY = map.altitude(positionX, positionZ) + CAMERA_HEIGHT;
            }
        }
        
        if (keys[KeyEvent.VK_A]) {
        	positionX += leftX;
        	positionZ += leftZ;
        	if (!flightMode) {            	
            	positionY = map.altitude(positionX, positionZ) + CAMERA_HEIGHT;
            }
        }
        	
        if (keys[KeyEvent.VK_D]) {
        	positionX -= leftX;
        	positionZ -= leftZ;
        	if (!flightMode) {            	
            	positionY = map.altitude(positionX, positionZ) + CAMERA_HEIGHT;
            }
        }
        	
        if (keys[KeyEvent.VK_F]) {
        	if (firstPerson) {
        		firstPerson = false;
        	} else {
        		firstPerson = true;
        	}
        }

        if (keys[KeyEvent.VK_LEFT]) {
        	rotateHorizontal(5);
        }
        
        if (keys[KeyEvent.VK_RIGHT]) {
        	rotateHorizontal(-5);
        }
        
        if (keys[KeyEvent.VK_UP]) {
        	rotateVertical(5);
        }
        	
        if (keys[KeyEvent.VK_DOWN]) {
        	rotateVertical(-5);
        }
            
        if (keys[KeyEvent.VK_SPACE]) {
        	if (flightMode) {
        		positionY += 0.1;
        	}
        }
        	
        if (keys[KeyEvent.VK_CONTROL]) {
        	if (flightMode) {
        		positionY -= 0.1;
        	}
        }
        	
        if (keys[KeyEvent.VK_R]) {
			if (flightMode) {
				System.out.println(" FlightMode Disabled ");
				positionY = map.altitude(positionX, positionZ) + CAMERA_HEIGHT;
			} else {
				System.out.println(" FlightMode Enabled ");
				positionY = map.altitude(positionX, positionZ) + CAMERA_HEIGHT * 3;
			}
			flightMode = !flightMode;
		}
		
		updateStep();
		updateLookAt();
	}
}
