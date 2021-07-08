package Engine;

public class Entity {

	private String spriteType;
	private int allTiles[];
	private Sprite spriteSheet[];
	private int x, y = 0; // Coordinates
	private int width, height; // Measured in tiles. Each tile is 8x8 pixels.
	private int direction; // I'll make this an enum later
	
	public Entity(String spriteType, int[] allTiles, Sprite[] spriteSheet) {
		this.spriteType = spriteType;
		this.allTiles = allTiles;
		this.spriteSheet = spriteSheet;
	}
	
	public String getSpriteType() {
		return spriteType;
	}
	
	public Sprite getSprite(int x) {
		return spriteSheet[x];
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
