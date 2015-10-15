package ass2.spec;

import java.util.ArrayList;

import javax.media.opengl.GL2;

public class TextureMgr {
	
	public static final TextureMgr instance = new TextureMgr();
	private ArrayList<Texture> text;
	private ArrayList<String> name;
	private int currentIndex;
	
	public TextureMgr() {
		text = new ArrayList<Texture>();
		name = new ArrayList<String>();
		currentIndex = -1;
	}
	
	/**
	 * Add a texture to the TextureManager (Storage)
	 * All texture is stored in "res/" folder
	 * @param t Texture instance
	 * @param n Specific Name to be bound
	 */
	public void add(Texture t, String n) {
		text.add(t);
		name.add(n);
	}
	
	/**
	 * Use the name you bound to activate texture before you draw
	 * @param gl
	 * @param name bound name
	 */
	public void activate(GL2 gl, String name) {
		currentIndex = name.indexOf(name);
		text.get(currentIndex).activate(gl);
	}
	
	/**
	 * Return the name of the current texture
	 * @return
	 */
	public String getCurrentTexture() {
		return name.get(currentIndex);
	}
	
	public int getWidth(String name) {
		currentIndex = name.indexOf(name);
		return text.get(currentIndex).getWidth();
	}
	
	public int getWidth() {
		return text.get(currentIndex).getWidth();
	}
	
	public int getHeight(String name) {
		currentIndex = name.indexOf(name);
		return text.get(currentIndex).getHeight();
	}
	
	public int getHeight() {
		return text.get(currentIndex).getHeight();
	}
}
