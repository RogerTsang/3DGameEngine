package ass2.spec;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

public class DiamondSection {
	private static final String VERTEX_SHADER = "res/VertexTex.glsl";
    private static final String FRAGMENT_SHADER = "res/FragmentTex.glsl";
    private static int ShaderID;
    private static int textureLocation;
    
	private static final int SHORT = 2;
	private static final int FLOAT = 4;
	
	private static final int VERTICES_LENGTH = 14 * 3;
	private static final int NORMALS_LENGTH = 24 * 3;
	private static final int TEXTURE_LENGTH = 24 * 8;
	
	private static final int VERTICES_OFFSET = 0;
	private static final int NORMALS_OFFSET = VERTICES_LENGTH;
	private static final int TEXTURE_OFFSET = VERTICES_LENGTH + NORMALS_LENGTH;
	
	private static final float TOPFACE_RADIUS = 0.1f;
	private static final float MIDFACE_RADIUS = 0.2f;
	
	private static final float[] ambientCoeff = { 0f, 0f, 0f, 1.0f };
	private static final float[] diffuseCoeff = { 0f, 0f, 0f, 1.0f };
	private static final float[] specularCoeff = { 0f, 0f, 0f, 1.0f };
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer tecoorBuffer;
	private ShortBuffer indexData;
	private short indexes[] = {			
			6,0,1,	6,1,7,
			7,1,2,	7,2,8,
			8,2,3,	8,3,9,
			9,3,4,	9,4,10,
			10,4,5,	10,5,11,
			11,5,0,	11,0,6,// GL2.GL_TRIANGLES
			
			12,6,7,
			12,7,8,
			12,8,9,
			12,9,10,
			12,10,11,
			12,11,6, // GL2.GL_TRIANGLES
			
			13,5,4,
			13,4,3,
			13,3,2,
			13,2,1,
			13,1,0,
			13,0,5, // GL2.GL_TRIANGLES
	};
	private static float textcoor[];
	private static float vertices[];
	private static float normals[];
	private static int bufferIds[];
	
	private double posX, posY, posZ;
	public DiamondSection(double[] pos) {
		posX = pos[0];
		posY = pos[1];
		posZ = pos[2];
		vertices = new float[VERTICES_LENGTH];
		normals = new float[NORMALS_LENGTH];
		bufferIds = new int[2];
	}
	
	public DiamondSection() {
		// For VBO initiation purpose
	}
	
	public void init(GL2 gl) {
		calculateV();
		calculateN();
		calculateT();
		bufferInit();
		
		// VBO section
		gl.glGenBuffers(2, bufferIds, 0);
		// would use GL.GL_ELEMENT_ARRAY_BUFFER for indices
		// BufferIds[0] = vertices + normals
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferIds[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, (VERTICES_LENGTH + NORMALS_LENGTH + TEXTURE_LENGTH)*FLOAT, null, GL2.GL_STATIC_DRAW);
		gl.glBufferSubData(GL.GL_ARRAY_BUFFER, VERTICES_OFFSET*FLOAT, VERTICES_LENGTH*FLOAT, vertexBuffer);
		gl.glBufferSubData(GL.GL_ARRAY_BUFFER, NORMALS_OFFSET*FLOAT, NORMALS_LENGTH*FLOAT, normalBuffer);
		gl.glBufferSubData(GL.GL_ARRAY_BUFFER, TEXTURE_OFFSET*FLOAT, TEXTURE_LENGTH*FLOAT, tecoorBuffer);
		// BufferIds[1] = indexes
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);
		gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indexes.length*SHORT, indexData, GL2.GL_STATIC_DRAW);
		
		// Shader Program Section
		// Compile & Generate Shader Program
		/*
		try {
			ShaderID = Shader.initShaders(gl, VERTEX_SHADER, FRAGMENT_SHADER);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		// Get Shader Texture Location
		textureLocation = gl.glGetUniformLocation(ShaderID, "texUnit1");
		*/
	}
	
	public void draw(GL2 gl) {
		gl.glPushMatrix();
		// Position
		gl.glTranslated(posX, posY, posZ);

		// Use Shader Program
		/*
		gl.glUseProgram(ShaderID);
		int textureID = TextureMgr.instance.getGLID("Diamond");
    	gl.glUniform1i(textureLocation , textureID);
		*/
		
    	// Texture
    	TextureMgr.instance.activate(gl, "Diamond");
    	
    	// Setup Material
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambientCoeff, 0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specularCoeff, 0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuseCoeff, 0);
    	
		// Enable vertex arrays: co-ordinates, normal and index.
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL2.GL_INDEX_ARRAY);
		
		// Bind vertex and index array
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, VERTICES_OFFSET*FLOAT);
		gl.glNormalPointer(GL.GL_FLOAT, 0, NORMALS_OFFSET*FLOAT);
		gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, TEXTURE_OFFSET*FLOAT);
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,bufferIds[1]);
		gl.glIndexPointer(GL2.GL_SHORT, 0, null);
		
    	// Index Drawing
    	gl.glDrawElements(GL2.GL_TRIANGLES, indexes.length, GL2.GL_UNSIGNED_SHORT, 0);
    	
    	// Disable vertex arrays: co-ordinates, normal and index.
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    	gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    	gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
    	gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    	gl.glDisableClientState(GL2.GL_INDEX_ARRAY);
    	
    	// Disable Shader
    	/*
    	gl.glUseProgram(0);
    	*/
    	gl.glPopMatrix();
	}
	
	public void calculateV() {
		vertices = new float[VERTICES_LENGTH];
		
		for (int i = 0; i < 6; i++) {
			double rad = (double) (i / 6.0 * 2 * Math.PI) ;
			vertices[i*3] = (float) Math.cos(rad) * TOPFACE_RADIUS;
			vertices[i*3+1] = (float) 0.4;
			vertices[i*3+2] = (float) Math.sin(rad) * TOPFACE_RADIUS;
		}
		for (int i = 6; i < 12; i++) {
			double rad = (double) (i / 6.0 * 2 * Math.PI);
			vertices[i*3] = (float) Math.cos(rad) * MIDFACE_RADIUS;
			vertices[i*3+1] = (float) 0.3;
			vertices[i*3+2] = (float) Math.sin(rad) * MIDFACE_RADIUS;
		}
		// Sharp Bottom
		vertices[36] = 0f;
		vertices[37] = 0f;
		vertices[38] = 0f;
		// Sharp Top
		vertices[39] = 0f;
		vertices[40] = 0.4f;
		vertices[41] = 0f;
	}
	
	public void calculateN() {
		normals = new float[NORMALS_LENGTH];
		float[] p0 = new float[3];
		float[] p1 = new float[3];
		float[] p2 = new float[3];
		float[] normal = new float[3];
		
		// Top layer normals
		for (int i = 0; i < 6; i++) {
			int id0 = i % 6 + 6;
			int id1 = i % 6;
			int id2 = (i + 1) % 6;
			p0[0] = vertices[id0*3+0]; p1[0] = vertices[id1*3+0]; p2[0] = vertices[id2*3+0];
			p0[1] = vertices[id0*3+1]; p1[1] = vertices[id1*3+1]; p2[1] = vertices[id2*3+1];
			p0[2] = vertices[id0*3+2]; p1[2] = vertices[id1*3+2]; p2[2] = vertices[id2*3+2];
			normal = MathUtil.getNormalisedNormal(p0, p1, p2);
			normals[i*6+0] = normal[0];
			normals[i*6+1] = normal[1];
			normals[i*6+2] = normal[2];
			normals[i*6+3] = normal[0];
			normals[i*6+4] = normal[1];
			normals[i*6+5] = normal[2];
		}
		
		// Bottom layer normals
		for (int i = 6; i < 12; i++) {
			int id0 = 12;
			int id1 = i % 6 + 6;
			int id2 = (i + 1) % 6 + 6;
			p0[0] = vertices[id0+0]; p1[0] = vertices[id1*3+0]; p2[0] = vertices[id2*3+0];
			p0[1] = vertices[id0+1]; p1[1] = vertices[id1*3+1]; p2[1] = vertices[id2*3+1];
			p0[2] = vertices[id0+2]; p1[2] = vertices[id1*3+2]; p2[2] = vertices[id2*3+2];
			normal = MathUtil.getNormalisedNormal(p0, p1, p2);
			normals[i*3+0+18] = normal[0];
			normals[i*3+1+18] = normal[1];
			normals[i*3+2+18] = normal[2];
		}
		
		// top fragment normals
		for (int i = 12; i < 18; i++) {
			normal[0] = 0.0f;
			normal[1] = 1.0f;
			normal[2] = 0.0f;
			normals[i*3+0+18] = normal[0];
			normals[i*3+1+18] = normal[1];
			normals[i*3+2+18] = normal[2];
		}
	}
	
	public void calculateT() {
		textcoor = new float[TEXTURE_LENGTH];
		for (int i = 0; i < TEXTURE_LENGTH / 8; i++) {
			textcoor[i*8+0] = 1f;
			textcoor[i*8+1] = 1f;
			textcoor[i*8+2] = 0f;
			textcoor[i*8+3] = 1f;
			textcoor[i*8+4] = 0f;
			textcoor[i*8+5] = 0f;
			textcoor[i*8+6] = 0f;
			textcoor[i*8+7] = 0f;
		}
	}
	
	public void bufferInit() {
		vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
		normalBuffer = Buffers.newDirectFloatBuffer(normals);
		tecoorBuffer = Buffers.newDirectFloatBuffer(textcoor);
		indexData = Buffers.newDirectShortBuffer(indexes);
	}
}
