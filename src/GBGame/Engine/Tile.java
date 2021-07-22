package GBGame.Engine;

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
	
	public Tile() {
		for(int i = 0; i < 64; i++){
			pixelArray[i] = 0;
		}
	}
	
	public byte[] getTile() {
		return pixelArray;
	}
	
	public void setTile(byte[] pixelArray) {
		this.pixelArray = pixelArray;
	}
	
	public void setPixel(int x, byte y) {
		this.pixelArray[x] = y;
	}
}
