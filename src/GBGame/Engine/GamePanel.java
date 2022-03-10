package GBGame.Engine;

import GBGame.TileEditor.JsonReadWrite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;



@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	// Panel Attributes \\
	public  static int WIDTH = 160;
	public  static int HEIGHT = 144;
	public  static int MAGNIFICATION = 1;
	private Thread thread;
	private boolean running;
	private long targetTime;
	private static final int FRAME_RATE = 60; // Maybe I should have a seperate cycle rate and frame rate
	
	// TEMP TESTING GRAPHICS \\ <-- TODO Will be removed when unneeded
	private int     frame_count    = 0;
	private Color   colorPalette[] = new Color[4]; // For more palletes, expand the array.  C = N + 4 * P, where N is the color number[1-4], P is the Pallete number[0+], and C is the index in the array. 0 is transparent.
	private int     chosenPallete  = 0;            // TODO For now, it doesn't do that, but this is a note to change that.
	
	// Layers \\
	private int cameraOffsetX = -8; // Default is -8. Should go from -1 to -15. This is for printing tiles.
	private int cameraOffsetY = -8; // Default is -8. Should go from -1 to -15.
	private int cameraPosX = 0;
	private int cameraPosY = 0;
	private Tile tileSet[] = new Tile[256];   // The tileset that the game uses. I'll figure it out later. The elements in the int layers refer to a tile in this array.
	private byte BackgroundLayer[];// = new byte[440];  // 20x18 is the screen size, but an extra tile is needed on each side. So 22x20.
	private Entity EntityLayer[];// = new Entity[2];
	private Sprite SpriteLayer[];// = new Sprite[256];
	private Sprite WindowLayer[];// = new Sprite[440]; // TODO Figure out how I want the window layer to work. It should probably be changed to sprites since some UI elements are animated.
	private byte FinalPixelColors[] = new byte [23040];
	
	// Game Data \\
	private GameManager gameManager; // TODO This contains the game data. Will probably rename it. Maybe even call something else game manager.
	//private JsonReadWrite jsonReadWrite = new JsonReadWrite();

	// Render \\
	public static Graphics2D g2d;
	private BufferedImage image;
	
	// User Controls \\
	private boolean controls[] = new boolean[] {false,false,false,false}; // Up, Right, Down, Left arrow keys. TODO Figure out a better way to read controls.
	
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
			//frame_count++;
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000; // change to 1E9
			
			//if(frame_count == FRAME_RATE) {
			//	System.out.println("Time Used = " + elapsed / 1000000 + "/" + targetTime);
			//	frame_count = 0;
			//}
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
		
		setColorPallete();
		gameManager = new GameManager();
		
		tileSet = gameManager.getTileSet();
		//readFileData();
		
		
		BackgroundLayer = gameManager.getBackgroundLayer();
		EntityLayer = gameManager.getEntityLayer();
		WindowLayer = gameManager.getWindowLayer();
	}
	/*
	private void readFileData() {
		JSONObject json = jsonReadWrite.readFromFile("./tiles.json");
		Iterator<String> keys = json.keys();
		Tile tile = new Tile();
		JSONArray jsonArray = new JSONArray();
		int[] temp1;
		byte[] temp2;
		int n = 0;
		
		while(keys.hasNext()) {
			String key = keys.next();
			if(json.get(key) instanceof JSONArray) {
				//savedTileNames[numberOfSavedTiles] = (String) key;
				jsonArray = (JSONArray) json.get(key);
				
				temp1 = new int[64];
				temp2 = new byte[64];
				
				for(int i = 0; i < 64; i++) {
					temp1[i] = (int) jsonArray.get(i);
					temp2[i] = (byte) temp1[i];
				}
				
				tile = new Tile(temp2);
				//savedTiles[numberOfSavedTiles] = tile;
				tileSet[n] = tile;
				n++;
			}
		}
	}
	*/
	private void setColorPallete() { // I have one color pallete for testing. More coming later...
		switch(chosenPallete) {
			//case  0: colorPalette[0] = Color.black; colorPalette[1] = Color.DARK_GRAY; colorPalette[2] = Color.LIGHT_GRAY; colorPalette[3] = Color.red; break;
			case  0: colorPalette[0] = Color.white; colorPalette[1] = Color.LIGHT_GRAY; colorPalette[2] = Color.DARK_GRAY; colorPalette[3] = Color.black; break;
			default: colorPalette[0] = Color.black; colorPalette[1] = Color.white; colorPalette[2] = Color.red; colorPalette[3] = Color.green;
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
			EntityLayer[0].accelerate(0,-0.2);
		}
		else if(controls[1]) {
			EntityLayer[0].changeDirection(1);
			EntityLayer[0].accelerate(0.2,0);
		}
		else if(controls[2]) {
			EntityLayer[0].changeDirection(2);
			EntityLayer[0].accelerate(0,0.2);
		}
		else if(controls[3]) {
			EntityLayer[0].changeDirection(3);
			EntityLayer[0].accelerate(-0.2,0);
		}
		
		// Do game operations
		EntityLayer[0].decrementFrameCountdown();
		EntityLayer[0].move();
		
		// Update entity states
		EntityLayer[0].updateSprite();
		/*
		for(Entity e : EntityLayer) {
			if(e.changed()) {
				e.updateSprite();
			}
		}*/
	}
	
	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}
	
	private void render(Graphics2D g2d) {
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
			//System.out.println(entity.getSpriteState());
			sprite = entity.getSprite(entity.getSpriteState());
			//sprite = entity.getSprite(0);
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
			
			g2d.setColor(colorPalette[tile[i]]);
			g2d.fillRect(tileX * MAGNIFICATION, tileY * MAGNIFICATION, MAGNIFICATION, MAGNIFICATION);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_UP) {
			controls[0] = true;
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
