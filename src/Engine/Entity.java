package Engine;

public class Entity {

	private String spriteType = "Default";
	private Sprite spriteSheet[];
	private byte[][] stateTable;
	
	private double x = 40.0; // Coordinates
	private double y = 40.0;
	private double dx = 0.0; // Current acceleration
	private double dy = 0.0;
	private double topSpeed = 10.0;
	private boolean moving = false;
	private boolean stateChanged = false; // If the state changed this frame, check and update things.
	private int spriteState = 0; // INT that represents the index for which sprite is active.
	private byte frameCountdown = 0;
	
	private int width, height; // Measured in tiles. Each tile is 8x8 pixels.
	private int direction; // I'll make this an enum later. 0123 = NESW
	
	public Entity(String spriteType, Sprite[] spriteSheet) {
		this.spriteType = spriteType;
		this.spriteSheet = spriteSheet;
		if(spriteType == "Player") {
			stateTable = new byte[12][6];
			
			/* The left index is just the state number
			 * The right index is the different properties.
			 * From 0 - 5 they are:
			 * 0 = Action (wait, move, etc)
			 * 1 = Direction (0-N, 1-E, 2-S, 3-W)
			 * 2 = Frame Delay (Number of frames to countdown before changing state)
			 * 3 = Next State (Once frame delay is done, this is indicates the next state to switch to)
			 * 4 = Flip (0 for false, 1 for true. Is the sprite used a flipped version of an existing sprite?)
			 * 5 = Sprite (The index for which sprite is being used from the Sprite Sheet)
			 */
			stateTable[0][0] = 0;
			stateTable[0][1] = 0;
			stateTable[0][2] = -1;
			stateTable[0][3] = -0;
			stateTable[0][4] = 0;
			stateTable[0][5] = 0;

			stateTable[1][0] = 0;
			stateTable[1][1] = 1;
			stateTable[1][2] = -1;
			stateTable[1][3] = -0;
			stateTable[1][4] = 1;
			stateTable[1][5] = 1;

			stateTable[2][0] = 0;
			stateTable[2][1] = 2;
			stateTable[2][2] = -1;
			stateTable[2][3] = -0;
			stateTable[2][4] = 0;
			stateTable[2][5] = 2;

			stateTable[3][0] = 0;
			stateTable[3][1] = 3;
			stateTable[3][2] = -1;
			stateTable[3][3] = -0;
			stateTable[3][4] = 0;
			stateTable[3][5] = 3;
			
			stateTable[4][0] = 1;
			stateTable[4][1] = 0;
			stateTable[4][2] = 20;
			stateTable[4][3] = 5;
			stateTable[4][4] = 0;
			stateTable[4][5] = 4;

			stateTable[5][0] = 1;
			stateTable[5][1] = 0;
			stateTable[5][2] = 20;
			stateTable[5][3] = 4;
			stateTable[5][4] = 1;
			stateTable[5][5] = 4;

			stateTable[6][0] = 1;
			stateTable[6][1] = 1;
			stateTable[6][2] = 20;
			stateTable[6][3] = 7;
			stateTable[6][4] = 1;
			stateTable[6][5] = 5;

			stateTable[7][0] = 1;
			stateTable[7][1] = 1;
			stateTable[7][2] = 20;
			stateTable[7][3] = 6;
			stateTable[7][4] = 1;
			stateTable[7][5] = 6;
			
			stateTable[8][0] = 1;
			stateTable[8][1] = 2;
			stateTable[8][2] = 20;
			stateTable[8][3] = 9;
			stateTable[8][4] = 0;
			stateTable[8][5] = 7;

			stateTable[9][0] = 1;
			stateTable[9][1] = 2;
			stateTable[9][2] = 20;
			stateTable[9][3] = 8;
			stateTable[9][4] = 0;
			stateTable[9][5] = 7;

			stateTable[10][0] = 1;
			stateTable[10][1] = 3;
			stateTable[10][2] = 20;
			stateTable[10][3] = 11;
			stateTable[10][4] = 0;
			stateTable[10][5] = 5;

			stateTable[11][0] = 1;
			stateTable[11][1] = 3;
			stateTable[11][2] = 20;
			stateTable[11][3] = 10;
			stateTable[11][4] = 0;
			stateTable[11][5] = 6;
		}
	}
	
	public String getSpriteType() { return spriteType; }
	public Sprite getSprite(int x) { return spriteSheet[x]; }
	public double getX() { return x; }
	public double getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getSpriteState() { return spriteState; }
	public boolean changed() { return stateChanged; }
	
	public void updateSprite() {
		//byte number = this.stateTable[spriteState][];
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
		stateChanged = true;
	}
	
	public void move() {
		if(moving == false) {
			moving = true;
			stateChanged = true;
		}
		
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
		if(dx == 0 && dy == 0) {
			moving = false;
			stateChanged = true;
		}
		
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
