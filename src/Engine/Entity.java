package Engine;

public class Entity {

	private String spriteType = "Default";
	//private int allTiles[];
	private Sprite spriteSheet[];
	private int x = 40;
	private int y = 40; // Coordinates
	private int width, height; // Measured in tiles. Each tile is 8x8 pixels.
	private int direction; // I'll make this an enum later
	private int spriteState = 0; // INT that represents the index for which sprite is active.
	
	public Entity(String spriteType, Sprite[] spriteSheet) {
		this.spriteType = spriteType;
		//this.allTiles = allTiles;
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
	
	public int getSpriteState() {
		return spriteState;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
