package Engine;

public class Entity {

	private String spriteType = "Default";
	private Sprite spriteSheet[];
	
	private double x = 40.0; // Coordinates
	private double y = 40.0;
	private double dx = 0.0; // Current acceleration
	private double dy = 0.0;
	private double speed = 0.0;
	private double topSpeed = 10.0;
	private boolean moving = false;
	
	private int width, height; // Measured in tiles. Each tile is 8x8 pixels.
	private int direction; // I'll make this an enum later. 0123 = NESW
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
	
	public double getX() {
		return x;
	}
	
	public double getY() {
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
	
	public void accelerate(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
		
		if(dx > topSpeed) dx = topSpeed;
		else if(dx < topSpeed*-1) dx = topSpeed *-1;
		
		if(dy > topSpeed) dy = topSpeed;
		else if(dy < topSpeed*-1) dy = topSpeed *-1;
	}
	
	public void changeDirection(int d) {
		direction = d;
	}
	
	public void move() {
		// Move object 
		x += dx;
		y += dy;
		
		// Slow down. This is natural slow down. Acceleration can just counter this. Can also act as gravity if that's relevent. Temporary.
		switch(direction) {
			case 0: dy += 0.1; if(dy > 0) dy = 0; break;
			case 1: dx -= 0.1; if(dx < 0) dx = 0; break;
			case 2: dy -= 0.1; if(dy < 0) dy = 0; break;
			case 3: dx += 0.1; if(dx > 0) dx = 0; break;
		}
		
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
