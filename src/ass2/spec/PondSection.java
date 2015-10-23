package ass2.spec;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

public class PondSection {
	private static final int FLOAT = 4;
	
	private static final int SECTIONS = 4;
	private static final float DEPTH = 0.2f;
	
	private static final float[] ambientCoeff = { 0.4f, 0.3f, 0.8f, 1.0f };
	private static final float[] diffuseCoeff = { 0.5f, 0.4f, 0.5f, 1.0f };
	private static final float[] specularCoeff = { 0.8f, 0.7f, 0.8f, 1.0f };
	
	private static final String VERTEX_SHADER = "res/WaterVTex.glsl";
	private static final String FRAGMENT_SHADER = "res/WaterFTex.glsl";
	private int ShaderID;
	private static long currentTime;
	private static long startTime;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer tecoorBuffer;
	private float textcoor[];
	private float vertices[];
	private int bufferIds[];
	
	private int NUM_QUADS;
	private int VERTICES_LENGTH;
	private int TEXTURE_LENGTH;
	private int VERTICES_OFFSET;
	private int TEXTURE_OFFSET;
	
	private double posX, posY, posZ; // Top left corner of the pond
	private double width, depth, wStep, dStep;
	private int wNum, dNum;
	public PondSection(double[] pos, GL2 gl) {
		posX = pos[0];
		posY = pos[1] - DEPTH;
		posZ = pos[2];
		width = pos[3] - pos[0];
		depth = pos[5] - pos[2];
		wNum = (int) (width * SECTIONS);
		dNum = (int) (depth * SECTIONS);
		wStep = width / wNum * 1.0;
		dStep = depth / dNum * 1.0;
		bufferIds = new int[1];
		calculateV();
		bufferInit();
		init(gl);
	}
	
	public void init(GL2 gl) {
		// VBO section
		gl.glGenBuffers(1, bufferIds, 0);
		// would use GL.GL_ELEMENT_ARRAY_BUFFER for indices
		// BufferIds[0] = vertices + normals
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferIds[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, (VERTICES_LENGTH + TEXTURE_LENGTH)*FLOAT, null, GL2.GL_STATIC_DRAW);
		gl.glBufferSubData(GL.GL_ARRAY_BUFFER, VERTICES_OFFSET*FLOAT, VERTICES_LENGTH*FLOAT, vertexBuffer);
		gl.glBufferSubData(GL.GL_ARRAY_BUFFER, TEXTURE_OFFSET*FLOAT, TEXTURE_LENGTH*FLOAT, tecoorBuffer);
		
		// Shader section
	   	 try {
            ShaderID = Shader.initShaders(gl, VERTEX_SHADER, FRAGMENT_SHADER);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
	   	 startTime = System.currentTimeMillis();
	}
	
	public void draw(GL2 gl) {
		gl.glPushMatrix();
		// Position
		gl.glTranslated(posX, posY, posZ);
		
    	// Texture
    	TextureMgr.instance.activate(gl, "Water");
    	
    	// Setup Material
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambientCoeff, 0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeff, 0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeff, 0);
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE);
    	
    	// Setup Shader
    	gl.glUseProgram(ShaderID);
    	currentTime = System.currentTimeMillis();
        float elapsedTime = currentTime - startTime;
        // Write current system time into shader program
        gl.glUniform1f(gl.glGetUniformLocation(ShaderID, "time"), elapsedTime);
        // Write current textureID to shader program
        gl.glUniform1i(gl.glGetUniformLocation(ShaderID, "textUnit"), TextureMgr.instance.getGLID("Water"));
        
		// Enable vertex arrays: co-ordinates, normal and index.
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		
		// Bind vertex and coordinate
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, VERTICES_OFFSET);
		gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, TEXTURE_OFFSET*FLOAT);
		
    	// Drawing
		gl.glNormal3f(0f, 1f, 0f);
		gl.glDrawArrays(GL2.GL_QUADS, 0, NUM_QUADS*4);
    	
    	// Disable vertex arrays: co-ordinates, normal and index.
		gl.glUseProgram(0);
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    	gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    	
    	gl.glPopMatrix();
	}
	
	public void calculateV() {
		int numWid = 0;
		int numHei = 0;
		// Calculate number of vertices
		VERTICES_LENGTH = 0;
		TEXTURE_LENGTH = 0;
		for (double x = posX; x < posX + width; x += wStep) {
			numWid++;
		}
		for (double z = posZ; z < posZ + depth; z += dStep) {
			numHei++;
		}
		
		NUM_QUADS = numWid * numHei;
		VERTICES_LENGTH = NUM_QUADS * 4 * 3;
		VERTICES_OFFSET = 0;
		TEXTURE_LENGTH = NUM_QUADS * 4 * 2;
		TEXTURE_OFFSET = VERTICES_LENGTH;
		vertices = new float[VERTICES_LENGTH];
		textcoor = new float[TEXTURE_LENGTH];
		
		for (int x = 0; x < numWid; x++) {
			for (int z = 0; z < numHei; z++) {
				// Top Left Corner
				vertices[(x+z*numWid) * 12+0] = (float) (x * wStep);
				vertices[(x+z*numWid) * 12+1] = (float) (posY);
				vertices[(x+z*numWid) * 12+2] = (float) (z * dStep);
				// Bottom Left Corner
				vertices[(x+z*numWid) * 12+3] = (float) (x * wStep);
				vertices[(x+z*numWid) * 12+4] = (float) (posY);
				vertices[(x+z*numWid) * 12+5] = (float) ((z+1) * dStep);
				// Bottom Right Corner
				vertices[(x+z*numWid) * 12+6] = (float) ((x+1) * wStep);
				vertices[(x+z*numWid) * 12+7] = (float) (posY);
				vertices[(x+z*numWid) * 12+8] = (float) ((z+1) * dStep);
				// Top Right Corner
				vertices[(x+z*numWid) * 12+9] = (float) ((x+1) * wStep);
				vertices[(x+z*numWid) * 12+10] = (float) (posY);
				vertices[(x+z*numWid) * 12+11] = (float) (z * dStep);
				
				// Top Left Corner
				textcoor[(x+z*numWid) * 8+0] = (float) (x * wStep / width);
				textcoor[(x+z*numWid) * 8+1] = (float) (z * dStep / depth);
				// Bottom Left Corner
				textcoor[(x+z*numWid) * 8+2] = (float) (x * wStep / width);
				textcoor[(x+z*numWid) * 8+3] = (float) ((z+1) * dStep / depth);
				// Bottom Right Corner
				textcoor[(x+z*numWid) * 8+4] = (float) ((x+1) * wStep / width);
				textcoor[(x+z*numWid) * 8+5] = (float) ((z+1) * dStep / depth);
				// Top Right Corner
				textcoor[(x+z*numWid) * 8+6] = (float) ((x+1) * wStep / width);
				textcoor[(x+z*numWid) * 8+7] = (float) (z * dStep / depth);
			}
		}
	}
	
	public void bufferInit() {
		vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
		tecoorBuffer = Buffers.newDirectFloatBuffer(textcoor);
	}
}

