package Engine;

public class Tile {
	/*
	 * 8x8 tiles
	 * The int is the color. 4 is transparent because 0-3 are the 4 colors from the palette.
	 */
	private byte pixelArray[] = new byte[64];
	
	
	public Tile(byte[] pixelArray) {
		// TODO Auto-generated constructor stub
		this.pixelArray = pixelArray;
	}
	
	public byte[] getTile() {
		return pixelArray;
	}
}
