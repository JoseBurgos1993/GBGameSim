package GBGame.Engine;

public class Entity {

	private String spriteType = "Default";
	private Sprite spriteSheet[];
	private byte[][] stateTable;
	
	private double x = 40.0; // Coordinates
	private double y = 40.0;
	private double dx = 0.0; // Current acceleration
	private double dy = 0.0;
	private double topSpeed = 1.0;
	private boolean moving = false;
	private boolean stateChanged = false; // If the state changed this frame, check and update things.
	private boolean frameDone = false;
	private int spriteState = 2; // INT that represents the index for which sprite is active.
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
			stateTable[0][3] = -1;
			stateTable[0][4] = 0;
			stateTable[0][5] = 4;

			stateTable[1][0] = 0;
			stateTable[1][1] = 1;
			stateTable[1][2] = -1;
			stateTable[1][3] = -1;
			stateTable[1][4] = 1;
			stateTable[1][5] = 2;

			stateTable[2][0] = 0;
			stateTable[2][1] = 2;
			stateTable[2][2] = -1;
			stateTable[2][3] = -1;
			stateTable[2][4] = 0;
			stateTable[2][5] = 0;

			stateTable[3][0] = 0;
			stateTable[3][1] = 3;
			stateTable[3][2] = -1;
			stateTable[3][3] = -1;
			stateTable[3][4] = 0;
			stateTable[3][5] = 2;
			
			stateTable[4][0] = 1;
			stateTable[4][1] = 0;
			stateTable[4][2] = 20;
			stateTable[4][3] = 5;
			stateTable[4][4] = 0;
			stateTable[4][5] = 5;

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
			stateTable[6][5] = 3;

			stateTable[7][0] = 1;
			stateTable[7][1] = 1;
			stateTable[7][2] = 20;
			stateTable[7][3] = 6;
			stateTable[7][4] = 1;
			stateTable[7][5] = 2;
			
			stateTable[8][0] = 1;
			stateTable[8][1] = 2;
			stateTable[8][2] = 20;
			stateTable[8][3] = 9;
			stateTable[8][4] = 0;
			stateTable[8][5] = 1;

			stateTable[9][0] = 1;
			stateTable[9][1] = 2;
			stateTable[9][2] = 20;
			stateTable[9][3] = 8;
			stateTable[9][4] = 0;
			stateTable[9][5] = 0;

			stateTable[10][0] = 1;
			stateTable[10][1] = 3;
			stateTable[10][2] = 20;
			stateTable[10][3] = 11;
			stateTable[10][4] = 0;
			stateTable[10][5] = 3;

			stateTable[11][0] = 1;
			stateTable[11][1] = 3;
			stateTable[11][2] = 20;
			stateTable[11][3] = 10;
			stateTable[11][4] = 0;
			stateTable[11][5] = 2;
			
			// For the start
			frameCountdown = stateTable[spriteState][2];
		}
	}
	
	public String getSpriteType() { return spriteType; }
	public Sprite getSprite(int x) {
		return spriteSheet[x];
	}
	public double getX() { return x; }
	public double getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getSpriteState() {
		//return spriteState;
		return (int)stateTable[spriteState][5];
	}
	public boolean changed() { return stateChanged; }
	public byte getFrameCountdown() { return frameCountdown; }
	public void decrementFrameCountdown() {
		if(frameCountdown != -1) {
			//System.out.println(frameCountdown);
			frameCountdown--;
			if(frameCountdown < 1) {
				frameDone = true;
			}
		}
	}
	
	public void updateSprite() {
		// Need to check if moving or still, and which direction.
		//System.out.println("Sprite State = " + spriteState);
		//if(frameDone && !stateChanged) {
		
		if(stateChanged) {
			switch(direction) {
				case 0: // NORTH
					if(moving) {
						changeState(4);
					} else {
						changeState(0);
					} break;
				case 1:  // EAST
					if(moving) {
						changeState(6);
					} else {
						changeState(1);
					} break;
				case 2: // SOUTH
					if(moving) {
						changeState(8);
					} else {
						changeState(2);
					} break;
				case 3: // WEST
					if(moving) {
						changeState(10);
					} else {
						changeState(3);
					} break;
			}
		}
		else if(frameDone) {
			changeState((int)stateTable[spriteState][3]);
		}
	}
	
	public void changeState(int newState) {
		//if(stateTable[newState][0] == 1) {
		//	moving = true;
		//} else {
		//	moving = false;
		//}
		
		frameCountdown = stateTable[newState][2];
		spriteState = newState;
		stateChanged = false;
		frameDone = false;
	}
	
	public void accelerate(double dx, double dy) {
		if(this.dx == 0.0 && this.dy == 0.0) {
			moving = true;
			stateChanged = true;
		}
		
		this.dx += dx;
		this.dy += dy;
		
		if(this.dx > topSpeed) this.dx = topSpeed;
		else if(this.dx < topSpeed*-1) this.dx = topSpeed *-1;
		
		if(this.dy > topSpeed) this.dy = topSpeed;
		else if(this.dy < topSpeed*-1) this.dy = topSpeed *-1;
	}
	
	public void changeDirection(int d) {
		if(direction != d) {
			direction = d;
			stateChanged = true;
		}
	}
	
	public void move() {
		
		//if(dx == 0 && dy == 0) {
		//	moving = false;
		//}
		//else {
		//	moving = true;
		//}
		//System.out.println("Moving = " + moving);
		
		//if(!moving) { used to be at the end of the 'accelerate' method
		//	moving = true;
		//	stateChanged = true;
		
		//}
		// We check for this twice in this method for before and after.
		//if(dx != 0 || dy != 0) { // Is still moving.
		if(moving) {
			// Move object 
			x += dx;
			y += dy;
			
			// Slow down. This is natural slow down. Acceleration can just counter this. Can also act as gravity if that's relevent. Temporary.
			//switch(direction) {
			//	case 0: dy += 0.1; if(dy > 0) dy = 0; break;
			//	case 1: dx -= 0.1; if(dx < 0) dx = 0; break;
			//	case 2: dy -= 0.1; if(dy < 0) dy = 0; break;
			//	case 3: dx += 0.1; if(dx > 0) dx = 0; break;
			//}
			if(dx > 0.0) {
				dx -= 0.1;
				if(dx < 0.0) dx = 0.0;
			} else if (dx < 0.0) {
				dx += 0.1;
				if(dx > 0.0) dx = 0.0;
			}
			
			if(dy > 0.0) {
				dy -= 0.1;
				if(dy < 0.0) dy = 0.0;
			} else if (dy < 0.0) {
				dy += 0.1;
				if(dy > 0.0) dy = 0.0;
			}
			
			if(dx == 0.0 && dy == 0.0) {
				moving = false;
				stateChanged = true;
			}
		}
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
