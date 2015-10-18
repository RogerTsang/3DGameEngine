package ass2.spec;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class Texture {
	
	private int GL2id = GL.GL_TEXTURE0;
	private int[] textureID = new int[1];
	private int width;
	private int height;
	
	//Create a texture from a file. Make sure the file has a width and height
	//that is a power of 2
	/**
	 * Create a Texture instance with the ImagePath you specific
	 * @param gl
	 * @param fileName
	 */
	public Texture(GL2 gl, String fileName) {
		TextureData data = null;
		try {
			File file = new File(fileName);
			BufferedImage img = ImageIO.read(file); // read file into BufferedImage
			ImageUtil.flipImageVertically(img);

			//This library call flips all images the same way
			data = AWTTextureIO.newTextureData(GLProfile.getDefault(), img, false);
		} catch (IOException exc) {
			System.err.println(fileName);
            exc.printStackTrace();
            System.exit(1);
        }
		
		gl.glGenTextures(1, textureID, 0);
		//The first time bind is called with the given id,
		//an openGL texture object is created and bound to the id
		//It also makes it the current texture.
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID[0]);

		// Build texture initialised with image data.
		width = data.getWidth();
		height = data.getHeight();
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0,
        				data.getInternalFormat(),
        				width,
        				height,
        				0,
        				data.getPixelFormat(),
        				data.getPixelType(),
        				data.getBuffer());
		
        // Always enable mipmap generation
		// Set texture parameters to enable automatic mipmap generation and bilinear/trilinear filtering
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);

		gl.glGenerateMipmap(GL2.GL_TEXTURE_2D); 
		
		// Specify how texture values combine with current surface color values.
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 
    	//gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
	}
	
	public void release(GL2 gl) {
		if (textureID[0] > 0) {
			gl.glDeleteTextures(1, textureID, 0);
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void activate(GL2 gl) {
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID[0]);
	}	
}

