package Engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JPanel;



@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	// Panel Attributes \\
	public  static int WIDTH = 160;
	public  static int HEIGHT = 144;
	public  static int MAGNIFICATION = 4;
	private Thread thread;
	private boolean running;
	private long targetTime;
	private static final int FRAME_RATE = 60;
	
	// TEMP TESTING GRAPHICS \\ <-- Will be removed when unneeded
	private int     frame_count    = 0;
	private int     offset         = 0;
	private boolean buttonPress    = false;
	private Color   colorArray[]   = new Color[23040]; // Contains the colors for every pixel on screen.
	private int     colorArray2[]  = new int[23040];
	private Color   colorPallete[] = new Color[4];
	private int     chosenPallete  = 0;
	
	// Layers \\
	private int cameraOffsetX = -8; // Default is 8. Should go from -1 to -15.
	private int cameraOffsetY = -8; // Default is 8. Should go from -1 to -15.
	private int cameraPosX = 0;
	private int cameraPosY = 0;
	private Tile tileSet[] = new Tile[256];   // The tileset that the game uses. I'll figure it out later. The elements in the int layers refer to a tile in this array.
	private byte BackgroundLayer[] = new byte[440];  // 20x18 is the screen size, but an extra tile is needed on each side. So 22x20.
	private Entity EntityLayer[] = new Entity[2]; // Rather than tiles, this contains the sprites themselves. I'll figure out the sprite limit later.
	private byte WindowLayer[] = new byte[1024];
	
	// Render \\
	public static Graphics2D g2d;
	private BufferedImage image;
	
	// User Controls \\
	private boolean controls[] = new boolean[] {false,false,false,false};
	
	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH*MAGNIFICATION, HEIGHT*MAGNIFICATION));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	public void addNotify() {
		super.addNotify();
		thread = new Thread(this);
		thread.start();
	}
	
	private void setFPS(int fps) {
		targetTime = 1000/fps;
	}
	
	@Override
	public void run() {
		if(running) return;
		init();
		
		long startTime, elapsed, wait;
		
		while(running) {
			startTime = System.nanoTime();
			update();
			requestRender();

			// For testing \\
			frame_count++;
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			if(frame_count == FRAME_RATE) {
				System.out.println("Time Used = " + elapsed / 1000000 + "/" + targetTime);
				frame_count = 0;
			}
			/////////\\\\\\\\
			
			if(wait > 0) {
				try {
					Thread.sleep(wait);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void init() {
		setFPS(FRAME_RATE);
		image = new BufferedImage(WIDTH*MAGNIFICATION, HEIGHT*MAGNIFICATION, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		
		// This initalizaes the array to start with all black rather than start with empty spots
		for(int i = 0; i < colorArray.length; i++) {
			colorArray[i] = Color.black;
		}
		
		// Sets the color pallete
		setColorPallete();
		
		createTestGameTiles();
	}
	
	private void createTestGameTiles() {
		// Creates Background Tile (the smiley face)
		byte tile1[] = new byte[64];
		//Arrays.fill(tile1, 1); // Default filling the array with dark grey pixels. Followed by filling in specific elements with the colors to make the face.
		for(int i = 0; i < tile1.length; i++) {
			tile1[i] = 1;
		}
		tile1[10] = 3;
		tile1[13] = 3;
		tile1[18] = 3;
		tile1[21] = 3;

		tile1[33] = 0;
		tile1[38] = 0;
		tile1[42] = 0;
		tile1[45] = 0;
		tile1[51] = 0;
		tile1[52] = 0;
		tileSet[0] = new Tile(tile1); // Adds this tile to the tileSet for the system
		//Arrays.fill(BackgroundLayer, 0); // Fills the whole background layer with this tile.
		for(int i = 0; i < BackgroundLayer.length; i++) {
			BackgroundLayer[i] = 0;
		}
		// Creating pokemon boy. First 4 lines create 4 tiles with the appropriate colors.
		// Stand down *DONE
		byte pokTile0[] = new byte[] {4,4,4,4,4,0,0,0,  4,4,4,4,0,1,1,1,  4,4,4,0,1,1,1,1,  4,4,4,0,1,1,1,1,  4,4,0,0,0,1,2,2,  4,4,0,0,1,0,0,0,  4,0,2,0,2,2,2,2,  4,0,2,2,2,2,0,2};
		byte pokTile1[] = new byte[] {0,0,0,4,4,4,4,4,  1,1,1,0,4,4,4,4,  1,1,1,1,0,4,4,4,  1,1,1,1,0,4,4,4,  2,2,1,0,0,0,4,4,  0,0,0,1,0,0,4,4,  2,2,2,2,0,2,0,4,  2,0,2,2,2,2,0,4}; // Mirror of previous
		byte pokTile2[] = new byte[] {4,4,0,0,2,2,0,2,  4,4,0,0,0,2,2,1,  4,0,2,2,0,0,0,0,  4,0,2,2,0,0,0,0,  4,4,0,0,0,1,1,0,  4,4,4,0,1,0,0,1,  4,4,4,0,1,1,1,0,  4,4,4,4,0,0,0,4};
		byte pokTile3[] = new byte[] {2,0,2,2,0,0,4,4,  1,2,2,0,0,0,4,4,  0,0,0,0,2,2,0,4,  0,0,0,0,2,2,0,4,  0,1,1,0,0,0,4,4,  1,0,0,1,0,4,4,4,  0,1,1,1,0,4,4,4,  4,0,0,0,4,4,4,4}; // Mirror of previous
		
		// Adds these tiles to the tileSet
		tileSet[1] = new Tile(pokTile0);
		tileSet[2] = new Tile(pokTile1);
		tileSet[3] = new Tile(pokTile2);
		tileSet[4] = new Tile(pokTile3);
		
		// Creates the array that is used to pick the tiles used for the upcoming sprite/entity.
		int testArr[] = new int[] {1,2,3,4};
		
		// Creates the sprite and then adds it to a sprite array. This is because an entity can consist of multiple sprites (i.e. one for looking up, another for left, walking frame 1, walk frame 2, etc)
		Sprite pokerman1 = new Sprite(testArr,2,2);
		Sprite pokermanArr[] = new Sprite[6];
		pokermanArr[0] = pokerman1;
		///////////////////////////////////////////////////////////////
		// More SPRITES! \\
		// Walk down *DONE
		pokTile0 = new byte[] {4,4,4,4,4,4,4,4, 4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,0,1,2,2, 4,4,0,0,0,0,0,0, 4,0,2,0,2,2,2,2};
		pokTile1 = new byte[] {4,4,4,4,4,4,4,4, 0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 2,2,1,0,0,0,4,4, 0,0,0,0,0,0,4,4, 2,2,2,2,0,2,0,4}; // Mirror of previous
		pokTile2 = new byte[] {4,0,2,2,2,2,0,2, 4,0,0,0,2,2,0,2, 4,0,2,0,0,2,2,1, 4,4,0,0,0,0,0,0, 4,4,4,0,0,1,0,0, 4,4,4,4,0,0,0,1, 4,4,4,4,0,1,1,0, 4,4,4,4,4,0,0,0};
		pokTile3 = new byte[] {2,0,2,2,2,2,0,4, 2,0,2,2,0,0,4,4, 1,2,2,0,1,0,4,4, 0,0,0,0,2,0,4,4, 0,0,2,2,0,0,4,4, 1,0,2,2,0,4,4,4, 0,4,0,0,4,4,4,4, 4,4,4,4,4,4,4,4};

		tileSet[5] = new Tile(pokTile0);
		tileSet[6] = new Tile(pokTile1);
		tileSet[7] = new Tile(pokTile2);
		tileSet[8] = new Tile(pokTile3);

		testArr = new int[] {5,6,7,8};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[1] = pokerman1;
		
		// Stand left *DONE
		
		pokTile0 = new byte[] {4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,2,1,1,1, 4,0,2,2,2,2,1,1, 4,4,0,0,1,1,1,0, 4,4,4,0,2,0,2,2, 4,4,4,0,2,0,2,2};
		pokTile1 = new byte[] {0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,0,0,0,4,4, 0,0,0,0,0,0,4,4, 0,0,0,0,0,0,4,4, 0,2,2,0,0,4,4,4};
		pokTile2 = new byte[] {4,4,4,0,2,2,2,2, 4,4,4,4,0,1,2,2, 4,4,4,4,4,0,0,0, 4,4,4,4,4,4,0,0, 4,4,4,4,4,4,0,0, 4,4,4,4,4,0,1,1, 4,4,4,4,4,0,1,1, 4,4,4,4,4,4,0,0};
		pokTile3 = new byte[] {2,2,2,0,4,4,4,4, 4,0,0,1,0,4,4,4, 0,0,1,1,0,4,4,4, 2,2,0,1,0,4,4,4, 2,2,0,1,0,4,4,4, 0,0,0,0,4,4,4,4, 1,1,0,4,4,4,4,4, 0,0,4,4,4,4,4,4};

		tileSet[9] = new Tile(pokTile0);
		tileSet[10] = new Tile(pokTile1);
		tileSet[11] = new Tile(pokTile2);
		tileSet[12] = new Tile(pokTile3);

		testArr = new int[] {9,10,11,12};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[2] = pokerman1;
		
		// Walk left *DONE
		pokTile0 = new byte[] {4,4,4,4,4,4,4,4, 4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,2,1,1,1, 4,0,2,2,2,2,1,1, 4,4,0,0,1,1,1,0, 4,4,4,0,2,0,2,2};
		pokTile1 = new byte[] {4,4,4,4,4,4,4,4, 0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,0,0,0,4,4, 0,0,0,0,0,0,4,4, 0,0,0,0,0,0,4,4};
		pokTile2 = new byte[] {4,4,4,0,2,0,2,2, 4,4,4,0,2,2,2,2, 4,4,4,4,0,1,2,2, 4,4,4,4,4,0,0,0, 4,4,4,0,0,0,0,0, 4,4,0,1,1,0,1,1, 4,4,4,0,1,1,0,0, 4,4,4,4,0,0,0,4};
		pokTile3 = new byte[] {0,2,2,0,0,4,4,4, 2,2,2,0,4,4,4,4, 2,0,0,1,0,4,4,4, 0,0,0,1,0,4,4,4, 0,2,2,0,0,4,4,4, 0,2,2,0,1,0,4,4, 0,0,0,1,1,0,4,4, 4,4,4,0,0,4,4,4};

		tileSet[13] = new Tile(pokTile0);
		tileSet[14] = new Tile(pokTile1);
		tileSet[15] = new Tile(pokTile2);
		tileSet[16] = new Tile(pokTile3);

		testArr = new int[] {13,14,15,16};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[3] = pokerman1;
		
		// Stand north
		pokTile0 = new byte[] {4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,1,1,1,1, 4,4,0,0,0,4,4,4, 4,0,2,0,0,0,0,0, 4,0,2,2,0,0,0,0};
		pokTile1 = new byte[] {0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,0,4,4, 1,1,1,0,0,0,4,4, 0,0,0,0,0,2,0,4, 0,0,0,0,2,2,0,4}; // Mirror of previous
		pokTile2 = new byte[] {4,4,0,0,2,2,0,0, 4,4,0,0,0,0,1,1, 4,0,2,0,0,1,0,0, 4,0,2,0,0,1,1,2, 4,4,0,0,0,0,1,1, 4,4,4,0,1,0,0,0, 4,4,4,0,1,1,1,0, 4,4,4,4,0,0,0,4};
		pokTile3 = new byte[] {0,0,2,2,0,0,4,4, 1,1,0,0,0,0,4,4, 0,0,1,0,0,2,0,4, 2,1,1,0,0,2,0,4, 1,1,0,0,0,0,4,4, 0,0,0,1,0,4,4,4, 0,1,1,1,0,4,4,4, 4,0,0,0,4,4,4,4}; // Mirror of previous

		tileSet[17] = new Tile(pokTile0);
		tileSet[18] = new Tile(pokTile1);
		tileSet[19] = new Tile(pokTile2);
		tileSet[20] = new Tile(pokTile3);
		
		testArr = new int[] {17,18,19,20};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[4] = pokerman1;
		
		// Walk north
		pokTile0 = new byte[] {4,4,4,4,4,4,4,4, 4,4,4,4,4,0,0,0, 4,4,4,4,0,1,1,1, 4,4,4,0,1,1,1,1, 4,4,4,0,1,1,1,1, 4,4,0,0,1,1,1,1, 4,4,0,0,0,1,1,1, 4,0,2,0,0,0,0,0};
		pokTile1 = new byte[] {4,4,4,4,4,4,4,4, 0,0,0,4,4,4,4,4, 1,1,1,0,4,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,4,4,4, 1,1,1,1,0,0,4,4, 1,1,1,0,0,0,4,4, 0,0,0,0,0,2,0,4}; // Mirror of previous
		pokTile2 = new byte[] {4,0,2,2,0,0,0,0, 4,0,0,0,2,2,0,0, 4,0,2,0,0,0,1,1, 4,4,0,0,0,1,0,0, 4,4,4,0,0,1,1,2, 4,4,4,4,0,0,1,1, 4,4,4,4,0,1,0,0, 4,4,4,4,4,0,0,0};
		pokTile3 = new byte[] {0,0,0,0,2,2,0,4, 0,0,2,2,0,0,4,4, 1,1,0,0,0,0,4,4, 0,0,1,0,2,2,0,4, 2,1,1,0,2,2,0,4, 1,1,0,0,0,0,4,4, 0,0,4,4,4,4,4,4, 4,4,4,4,4,4,4,4};

		tileSet[21] = new Tile(pokTile0);
		tileSet[22] = new Tile(pokTile1);
		tileSet[23] = new Tile(pokTile2);
		tileSet[24] = new Tile(pokTile3);

		testArr = new int[] {21,22,23,24};
		
		pokerman1 = new Sprite(testArr,2,2);
		pokermanArr[5] = pokerman1;
		
		
		
		
		
		///////////////////////////////////////////////////////////////
		// Creates the entity. The second line is just a duplicate to make sure it worked.
		Entity pokerman = new Entity("Player", pokermanArr);
		Entity pokermanDos = new Entity("Player", pokermanArr);
		
		// Changing spawn location of the second one.
		pokermanDos.setLocation(90, 25);
		
		// Adds the entities to the entity layer. Later, there should just be a list of all entities in the game and the entity layer should just grab from that when needed.
		EntityLayer[0] = pokerman;
		EntityLayer[1] = pokermanDos;
	}
	
	private void setColorPallete() { // I have one color pallete for testing. More coming later...
		switch(chosenPallete) {
			case  0: colorPallete[0] = Color.black; colorPallete[1] = Color.DARK_GRAY; colorPallete[2] = Color.LIGHT_GRAY; colorPallete[3] = Color.red; break;
			default: colorPallete[0] = Color.black; colorPallete[1] = Color.white; colorPallete[2] = Color.red; colorPallete[3] = Color.green;
		}
	}
	
	private void update() {
		/*
		 * Read user input.
		 * Do game operations.
		 * Update tile layers with any new tiles. <-- Need to change how all that works first
		 */
		
		// Read user input
		if(controls[0]) {
			EntityLayer[0].changeDirection(0);
			EntityLayer[0].accelerate(0,-2);
		}
		else if(controls[1]) {
			EntityLayer[0].changeDirection(1);
			EntityLayer[0].accelerate(2,0);
		}
		else if(controls[2]) {
			EntityLayer[0].changeDirection(2);
			EntityLayer[0].accelerate(0,2);
		}
		else if(controls[3]) {
			EntityLayer[0].changeDirection(3);
			EntityLayer[0].accelerate(-2,0);
		}
		
		// Do game operations
		EntityLayer[0].move();
		
		// Update entity states
		for(Entity e : EntityLayer) {
			if(e.changed()) {
				e.updateSprite();
			}
		}
	}
	
	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}
	
	private void render(Graphics2D g2d) {
		/*
		 * Below is for testing.
		 * What I actually want is for this to render the Background Layer, then render the Sprite Layer, then Render the Window layer, but only if there is an update.
		 * Technically I only want to redraw tiles if the camera moves or a sprite or window element moves, but the window will mostly be static.
		 * 
		 * renderBackground();
		 * renderSprites();
		 * renderWindow();
		 * 
		 */
		
		
		
		//g2d.clearRect(0, 0, WIDTH*MAGNIFICATION, HEIGHT*MAGNIFICATION);
		
		/*
		int randx = (int)Math.floor(Math.random()*159);
		int randy = (int)Math.floor(Math.random()*143);
		g2d.setPaint(Color.red);
		g2d.fillRect(randx * MAGNIFICATION, randy * MAGNIFICATION, MAGNIFICATION, MAGNIFICATION);
		
		*/
		/*
		// Get random Colors. Initializes to magenta so it doesn't crash.
		Color one = Color.magenta;
		Color two = Color.magenta;
		Color three = Color.magenta;
		
		int rand = (int)Math.floor(Math.random()*3);
		switch(rand) {
			case 0: one = Color.yellow; break;
			case 1: one = Color.magenta; break;
			case 2: one = Color.green; break;
			default: break;
		}
		rand = (int)Math.floor(Math.random()*3);
		switch(rand) {
			case 0: two = Color.red; break;
			case 1: two = Color.white; break;
			case 2: two = Color.blue; break;
			default: break;
		}
		rand = (int)Math.floor(Math.random()*3);
		switch(rand) {
			case 0: three = Color.black; break;
			case 1: three = Color.orange; break;
			case 2: three = Color.gray; break;
			default: break;
		}
		
		// Fills the colorArray with the colors it needs.
		int temp;
		for(int i = 0; i < colorArray.length; i++) {
			temp = i%3;
			switch(temp) {
				case 0: colorArray[i]=one; break;
				case 1: colorArray[i]=two; break;
				case 2: colorArray[i]=three; break;
				default: colorArray[i]=Color.yellow; break;
			}
			offset++;
			if(offset>2) offset = 0;
		}
		
		// Draw on the canvas.
		for(int i = 0; i < 23040; i++) {
			g2d.setColor(colorArray[i]);
			g2d.drawRect((i % WIDTH) * MAGNIFICATION, ((int)(i / WIDTH)) * MAGNIFICATION, MAGNIFICATION - 1, MAGNIFICATION - 1);
		}
		*/

		// render tile test \\
		int x1 = cameraOffsetX;
		int y1 = cameraOffsetY;
		for(int i = 0; i < BackgroundLayer.length; i++) {
			drawTile(BackgroundLayer[i],x1,y1,g2d);
			
			x1+=8;
			if(x1>159) {
				x1 = cameraOffsetX;
				y1+=8;
			}
		}
		
		int width,height;
		int tile;
		Sprite sprite;
		
		int x,y;
		
		for(Entity entity : EntityLayer) {
			sprite = entity.getSprite(entity.getSpriteState());
			width = sprite.getWidth();
			height = sprite.getHeight();
			x = (int)entity.getX();
			y = (int)entity.getY();
			
			for(int i = 0; i < width+height; i++) {
				tile = sprite.getTileSheetElement(i);
				drawTile(tile,x,y,g2d);
				x+=8;
				if(x - (int)entity.getX() >= 8 * width) {
					x = (int)entity.getX();
					y+=8;
				}
			}
		}
		///////////\\\\\\\\\\\
	}
	
	private void drawTile(int tileNum, int x, int y, Graphics2D g2d) {
		byte tile[] = tileSet[tileNum].getTile();
		int tileX, tileY = 0;
		int xOffset = 0;
		int yOffset = 0;
		for(int i = 0; i < tile.length; i++) {
			if(tile[i] == 4) {
				continue;
			}
			xOffset = i%8;
			yOffset = (int)i/8;
			tileX = x+xOffset;
			tileY = y+yOffset;
			
			//if(tileX < 0 || tileY < 0 || tileX > 160 - 1 || tileY > 144 - 1) {
			//	continue;
			//}
			
			g2d.setColor(colorPallete[tile[i]]);
			g2d.fillRect(tileX * MAGNIFICATION, tileY * MAGNIFICATION, MAGNIFICATION, MAGNIFICATION);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		//buttonPress = true;
		//Entity boi = EntityLayer[0];
		
		if(key == KeyEvent.VK_UP) {
			controls[0] = true;
			//boi.changeDirection(0);
			//boi.accelerate(0,-4);
		}
		if(key == KeyEvent.VK_RIGHT) {
			controls[1] = true;
		}
		if(key == KeyEvent.VK_DOWN) {
			controls[2] = true;
		}
		if(key == KeyEvent.VK_LEFT) {
			controls[3] = true;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_UP) {
			controls[0] = false;
		}
		if(key == KeyEvent.VK_RIGHT) {
			controls[1] = false;
		}
		if(key == KeyEvent.VK_DOWN) {
			controls[2] = false;
		}
		if(key == KeyEvent.VK_LEFT) {
			controls[3] = false;
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
