package Engine;

public class Sprite {
	private int tileSheet[]; // Will probably hold every tile, includes dupelicates.
	private int width, height; // Number of tiles wide and high.
	
	public Sprite(int[] tileSheet, int width, int height) {
		/*
		 * Sprite type: decor, player/npc, other?
		 * Size: number of tiles
		 * Number of different frame: 2 for walk animation going vertical or horizontal each, 1 for idle in each direction, etc
		 * Number of tiles being used
		 */
		this.tileSheet = tileSheet;
		this.width = width;
		this.height = height;
		System.out.println(this.width + " " + this.height);
	}
	
	public int[] getTileSheet(){
		return tileSheet;
	}
	public int getTileSheetElement(int x) {
		return tileSheet[x];
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
