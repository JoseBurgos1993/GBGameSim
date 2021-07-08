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
	
	// TEMP TESTING GRAPHICS \\
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
	private Tile   tileSet[]         = new Tile[256];   // The tileset that the game uses. I'll figure it out later. The elements in the int layers refer to a tile in this array.
	//private int    BackgroundLayer[] = new int[1024];  // 32x32 tiles. 20x18 is the screen size, but an extra tile is needed on each side. So 22x20. But then I need the extra stuff. So 32x32.
	private int    BackgroundLayer[] = new int[440];  // 32x32 tiles. 20x18 is the screen size, but an extra tile is needed on each side. So 22x20. But then I need the extra stuff. So 32x32.
	private Sprite SpriteLayer[]     = new Sprite[40]; // Rather than tiles, this contains the sprites themselves. I'll figure out the sprite limit later.
	private int    WindowLayer[]     = new int[1024];
	
	// Render \\
	public static Graphics2D g2d;
	private BufferedImage image;
	
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
		int tile1[] = new int[64];
		Arrays.fill(tile1, 1);
		tile1[10] = 2;
		tile1[13] = 2;
		tile1[18] = 2;
		tile1[21] = 2;

		tile1[33] = 0;
		tile1[38] = 0;
		tile1[42] = 0;
		tile1[45] = 0;
		tile1[51] = 0;
		tile1[52] = 0;
		tileSet[0] = new Tile(tile1);
		Arrays.fill(BackgroundLayer, 0);
	}
	
	private void setColorPallete() { // I have one color pallete for testing. More coming later...
		switch(chosenPallete) {
			case  0: colorPallete[0] = Color.black; colorPallete[1] = Color.white; colorPallete[2] = Color.red; colorPallete[3] = Color.green;
			default: colorPallete[0] = Color.black; colorPallete[1] = Color.white; colorPallete[2] = Color.red; colorPallete[3] = Color.green;
		}
	}
	
	private void update() {
		/*
		 * Read user input.
		 * Do game operations.
		 * Update tile layers with any new tiles.
		 */
		
		
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
		int x = cameraOffsetX;
		int y = cameraOffsetY;
		for(int i = 0; i < BackgroundLayer.length; i++) {
			drawTile(BackgroundLayer[i],x,y,g2d);
			
			x+=8;
			if(x>159) {
				x = cameraOffsetX;
				y+=8;
			}
		}
		///////////\\\\\\\\\\\
	}
	
	private void drawTile(int tileNum, int x, int y, Graphics2D g2d) {
		int tile[] = tileSet[tileNum].getTile();
		int tileX, tileY = 0;
		int xOffset = 0;
		int yOffset = 0;
		for(int i = 0; i < tile.length; i++) {
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
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		buttonPress = true;
		if(key == KeyEvent.VK_UP) {
			cameraOffsetY++;
			if(cameraOffsetY>-1) {
				cameraOffsetY = -1;
			}
		}
		if(key == KeyEvent.VK_DOWN) {
			cameraOffsetY--;
			if(cameraOffsetY<-15) {
				cameraOffsetY = -15;
			}
		}
		if(key == KeyEvent.VK_LEFT) {
			cameraOffsetX++;
			if(cameraOffsetX>-1) {
				cameraOffsetX = -1;
			}
		}
		if(key == KeyEvent.VK_RIGHT) {
			cameraOffsetX--;
			if(cameraOffsetX<-15) {
				cameraOffsetX = -15;
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
