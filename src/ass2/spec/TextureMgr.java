package ass2.spec;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class TextureMgr {
	
	public static final TextureMgr instance = new TextureMgr();
	private ArrayList<Texture> textures;
	private ArrayList<String> names;
	private int currentIndex;
	
	public TextureMgr() {
		textures = new ArrayList<Texture>();
		names = new ArrayList<String>();
		currentIndex = -1;
	}
	
	/**
	 * Add a texture to the TextureManager (Storage)
	 * All texture is stored in "res/" folder
	 * @param t Texture instance
	 * @param n Specific Name to be bound
	 */
	public void add(Texture t, String n) {
		textures.add(t);
		names.add(n);
	}
	
	/**
	 * Use the name you bound to activate texture before you draw
	 * @param gl
	 * @param name bound name
	 */
	public void activate(GL2 gl, String name) {
		currentIndex = names.indexOf(name);
		textures.get(currentIndex).activate(gl);
	}
	
	/**
	 * Deactivate current texture
	 * @param gl
	 */
	public void deactivate(GL2 gl) {
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Return the name of the current texture
	 * @return
	 */
	public String getCurrentTexture() {
		return names.get(currentIndex);
	}
	
	/**
	 * Get specific texture image width with provided texture name
	 * @param name
	 * @return
	 */
	public int getWidth(String name) {
		currentIndex = names.indexOf(name);
		return textures.get(currentIndex).getWidth();
	}
	
	/**
	 * Get current texture image width
	 * @return
	 */
	public int getWidth() {
		return textures.get(currentIndex).getWidth();
	}
	
	/**
	 * Get specific texture image height with provided texture name
	 * @param name
	 * @return
	 */
	public int getHeight(String name) {
		currentIndex = name.indexOf(name);
		return textures.get(currentIndex).getHeight();
	}
	
	/**
	 * Get current texture image height
	 * @return
	 */
	public int getHeight() {
		return textures.get(currentIndex).getHeight();
	}
	
	/**
	 * Get texture OpenGL ID with provided texture name
	 * @param name
	 * @return
	 */
	public int getGLID(String name) {
		int index = name.indexOf(name);
		return textures.get(index).getGLID();
	}
}
