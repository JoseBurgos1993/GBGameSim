/* Tile Editor
 * TilePanel.java
 * By Jose David Burgos
 * 
 * This application allows for the user to paint and save a series of 8x8 tiles, using 5 colors, the 5th being transparent. Tiles can be viewd and edited. Each tile is named.
 * 
 * TODO: Implement File I/O - Currently does not save any tile data, aka clean memory on each run. I want to have some sort of JSON? file that holds all data to keep continuity between sessions
 *		 and allow for access by other applications.
 * 
 * TODO: Beautify - It looks ugly. Make it prettier.
 * 
 * TODO: Revise and Edit Code - There are methods and variables that could use with better naming and organization. It took too long to remember how this code works.
 * 
 * TODO: Saved Tile Screen Scroll Bar - Add a scroll bar, or arrow buttons, or whatever to the Saved Tile Screen. Needs ability to scroll through saved tiles once there are too many on the screen.
 * 
 */

package GBGame.TileEditor;

import GBGame.Engine.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TilePanel extends JPanel implements Runnable, KeyListener, MouseListener{
	
	//----- Panel Attributes -----\\
	public static Graphics2D g2d;
	private BufferedImage image;
	private boolean running;
	private Thread thread;
	private long targetTime;
	private static final int FRAME_RATE = 25;
	
	//----- Variables -----\\
	private int      numberOfSavedTiles = 0; // Tracks how many tiles are saved in savedTiles, since it's array length is predetermined.
	private int      editTileIndex = 0; // Follows which tile is being edited on Edit Tile Page. Value related to index in in savedTiles.
	private int      selectedTile = -1; // Follows which tile was clicked on the Saved Tile Page. Value related to index in savedTiles? (Need to check that one). -1 means nothing is selected.
	private int      scrollBarRow = 0;
	
	private byte     selectedColor = 3; // What color is currently selected while painting. Defaults to 3 (black) because why not. Value related to colorPalette.
	
	private boolean  nameFieldActive = false; // Whether or not the namefield is active on the screen or not.
	
	private int      state; // What page we are on
	private String   nameFieldText = ""; // Name Field Text. Tile names are limited to 20 characters.
	private String[] savedTileNames = new String[256]; // String arry of saved tile names.
	
	private Button   buttonList[]; // Array of buttons.
	private Button   activeButton = new Button(); // Which button is currently active. Used for mouse clicking.
	private Button[] tileColors; // Button array for the 8x8 tile on the Create New Tile or Edit Tile pages.
	private Button[] colorSelection = new Button[5]; // Button array for the 5 colors (5th is transparent/erase) on the Create New Tile or Edit Tile pages.
	private Button[] tileButtons;
	
	private Tile[]   savedTiles = new Tile[256]; // Remember, Tiles use Bytes, not Ints.
	
	private Color[]  colorPalette = new Color[] {Color.white, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.black}; // 4 colors used for painting. Doesn't affect colors on Saved Tile screen. That is in Button.java.
	
	private JsonReadWrite jsonReadWrite = new JsonReadWrite();
	
	//----- System -----\\
	
	public TilePanel() {
		setPreferredSize(new Dimension(800, 600));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		addMouseListener(this);
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
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
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
		image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		
		readFromFile();
		makeMainMenu();
	}
	
	//----- Pages -----\\
	
	// Main Menu
	private void makeMainMenu() {
		//state = "MainMenu";
		state = 1;
		
		buttonList = new Button[3];
		tileColors = new Button[64];
		buttonList[0] = new Button(100,350, 100, 50, "Create", "gotoCreate", false);
		buttonList[1] = new Button(300,350, 100, 50, "View Saved", "gotoSaved", false);
		buttonList[2] = new Button(500,350, 100, 50, "Leave", "Leave", false);
		nameFieldActive = false;
	}
	
	// View Saved Tiles
	private void makeSavedTilePage() {
		state = 2;
		nameFieldText = "";
		selectedTile = -1;
		buttonList = new Button[4];
		buttonList[0] = new Button(680,20, 100, 50, "Back", "gotoMainMenu", false);
		buttonList[1] = new Button(680,200, 100, 50, "Edit Tile", "gotoEdit", false);
		buttonList[2] = new Button(600,90, 50, 50, "Up", "scrollUp", false);
		buttonList[3] = new Button(600,160, 50, 50, "Down", "scrollDown", false);
		tileButtons = new Button[numberOfSavedTiles];
		for(int i = 0; i < numberOfSavedTiles; i++) {
			System.out.println("i = " + i);
			tileButtons[i] = new Button(50+53*(i%9),100+53*(i/9), 40, 40, "", "clickTile", false);
			tileButtons[i].tileSpot = i;
		}
		nameFieldActive = false;
	}
	
	// Edit Screen
	private void makeEditTileScreen() {
		//state = "Edit2";
		state = 3;
		buttonList = new Button[3];
		buttonList[0] = new Button(680,20, 100, 50, "Back", "gotoSaved", false);
		buttonList[1] = new Button(680,200,100,50, "Save", "Save", false);
		buttonList[2] = new Button(10,10,200,50, "", "clickedNameField", true);
		nameFieldActive = true;
		nameFieldText = savedTileNames[editTileIndex];
		buttonList[2].text = nameFieldText;
		tileColors = new Button[64];

		for(int i = 0; i < 8; i++) { // Vertical
			for(int j = 0; j < 8; j++) { // Horizonal
				tileColors[j+i*8] = new Button(40+42*j, 100+42*i, 40, 40, "", "paint", false);
				tileColors[j+i*8].color = savedTiles[editTileIndex].getPixel(j+i*8);
			}
		}
		
		colorSelection[0] = new Button(600, 300, 40, 40, "", "color 0", false);
		colorSelection[1] = new Button(600, 350, 40, 40, "", "color 1", false);
		colorSelection[2] = new Button(600, 400, 40, 40, "", "color 2", false);
		colorSelection[3] = new Button(600, 450, 40, 40, "", "color 3", false);
		colorSelection[4] = new Button(600, 500, 40, 40, "", "color 4", false);
	}
	
	// Create New Tile Screen (Very similar to edit screen)
	private void makeCreateScreen() {
		//state = "Create";
		state = 4;
		buttonList = new Button[3];
		buttonList[0] = new Button(680,20, 100, 50, "Back", "gotoMainMenu", false);
		buttonList[1] = new Button(680,200,100,50, "Save", "Save", false);
		buttonList[2] = new Button(10,10,200,50, "", "clickedNameField", true);
		nameFieldActive = true;
		nameFieldText = "";
		
		tileColors = new Button[64];

		for(int i = 0; i < 8; i++) { // Vertical
			for(int j = 0; j < 8; j++) { // Horizonal
				tileColors[j+i*8] = new Button(40+42*j, 100+42*i, 40, 40, "", "paint", false);
			}
		}
		
		colorSelection[0] = new Button(600, 300, 40, 40, "", "color 0", false); // White
		colorSelection[1] = new Button(600, 350, 40, 40, "", "color 1", false); // Light Gray
		colorSelection[2] = new Button(600, 400, 40, 40, "", "color 2", false); // Dark Gray
		colorSelection[3] = new Button(600, 450, 40, 40, "", "color 3", false); // Black
		colorSelection[4] = new Button(600, 500, 40, 40, "", "color 4", false); // Transparent
	}
	
	//----- Methods -----\\
	
	// Method that draws the tiles in the saved view page
	private void drawTileOnEdit(Graphics2D g2d, int k) {
		int tileX, tileY = 0;
		int xOffset = 0;
		int yOffset = 0;
		
		byte tile[] = savedTiles[k].getTile();
		
		for(int i = 0; i < tile.length; i++) {
			if(tile[i] == 4) {
				continue;
			}
			xOffset = i%8 * 6;
			yOffset = (int)i/8 * 6;
			
			tileX = 50+53*(k%9) + xOffset;
			tileY = 100+53*(k/9)-53*scrollBarRow + yOffset;
			

			
			g2d.setColor(colorPalette[tile[i]]);
			g2d.fillRect(tileX, tileY, 4, 4);
			//if(tile[i] == 0) {
			//	g2d.setColor(Color.black);
			//} else {
			g2d.setColor(colorPalette[tile[i]]);
			//}
			g2d.drawRect(tileX, tileY, 4, 4);
		}
	}
	
	// Saves Tile On Creation And Edit Pages
	private void saveTile() {
		if(nameFieldText != "") {
			Tile temp = new Tile();
			for(int i = 0; i < 64; i++) {
				temp.setPixel(i, tileColors[i].color);
			}
			savedTileNames[editTileIndex] = nameFieldText;
			nameFieldText = "";
			savedTiles[editTileIndex] = temp;
			if(state == 4) numberOfSavedTiles++; // Only increments if creating new tile. Doesn't increment if editing existing tile.
			
			writeToFile();
		}
	}
	
	// Handles Button Actions
	private void doButtonAction(String action) {
		if(action == "Leave") {
			System.exit(0);
		}
		else if(action == "gotoCreate") {
			editTileIndex = numberOfSavedTiles;
			makeCreateScreen();
		}
		else if(action == "gotoMainMenu") {
			makeMainMenu();
		}
		else if(action == "gotoSaved") {
			makeSavedTilePage();
		}
		else if(action == "gotoEdit") {
			System.out.println("Trying to edit selected tile.");
			if(selectedTile != -1) {
				editTileIndex = selectedTile;
				makeEditTileScreen();
			}
		}
		else if(action == "Save") {
			saveTile();
			makeMainMenu();
		}
		else if(action == "clickedNameField") {
			nameFieldActive = true;
		}
		else if(action == "color 0") {
			selectedColor = 0;
		}
		else if(action == "color 1") {
			selectedColor = 1;
		}
		else if(action == "color 2") {
			selectedColor = 2;
		}
		else if(action == "color 3") {
			selectedColor = 3;
		}
		else if(action == "color 4") {
			selectedColor = 4;
		}
		else if(action == "paint") {
			activeButton.color = selectedColor;
		}
		else if(action == "scrollDown") {
			scrollBarRow++;
		}
		else if(action == "scrollUp") {
			if(scrollBarRow > 0) scrollBarRow--;
		}
	}
	
	// Read From File
	private void readFromFile() {

		JSONObject json = jsonReadWrite.readFromFile("./tiles.json");
		Iterator<String> keys = json.keys();
		Tile tile = new Tile();
		JSONArray jsonArray = new JSONArray();
		int[] temp1;
		byte[] temp2;
		
		while(keys.hasNext()) {
			String key = keys.next();
			if(json.get(key) instanceof JSONArray) {
				savedTileNames[numberOfSavedTiles] = (String) key;
				jsonArray = (JSONArray) json.get(key);
				
				temp1 = new int[64];
				temp2 = new byte[64];
				
				for(int i = 0; i < 64; i++) {
					temp1[i] = (int) jsonArray.get(i);
					temp2[i] = (byte) temp1[i];
				}
				
				tile = new Tile(temp2);
				savedTiles[numberOfSavedTiles] = tile;
				numberOfSavedTiles++;
			}
		}
	}
	
	// Write To File
	private void writeToFile() {
		JSONObject obj = new JSONObject();
		JSONArray list;
		for(int i = numberOfSavedTiles - 1; i >= 0; i--) {
			list = new JSONArray();
			for(int j = 0; j < 64; j++) {
				list.put(savedTiles[i].getPixel(j));
			}
			obj.put(savedTileNames[i], list);
		}
		
		jsonReadWrite.writeToFile(obj, "./tiles.json");
	}

	//----- Rendering -----\\
	
	// Update method is empty
	private void update() {
		
	}
	
	// Request Render
	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}
	
	// Render
	private void render(Graphics2D g2d) {
		// Clear Screen
		g2d.clearRect(0, 0, 800, 600);
		
		// Draws Buttons
		for(Button b : buttonList) {
			if(b.isNameField) {
				b.drawNameField(g2d);
			} else if(b.tileSpot == -1){
				b.drawButton(g2d);
			}
		}
		
		switch(state) {
			case 2: { // Saved Tiles
				g2d.setColor(new Color(185, 122, 87)); // Brown
				g2d.fillRect(30,90,540,500);
				
				
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.drawString(nameFieldText, 20, 20);
				
				for(int i = 0; i < numberOfSavedTiles; i++) {
					//if(i >= 9*scrollBarRow) {
						drawTileOnEdit(g2d, i);
						if(selectedTile == i) {
							g2d.setColor(Color.green);
						} else {
							g2d.setColor(Color.black);
						}
						g2d.drawRect(tileButtons[i].x - 2, tileButtons[i].y - 2, 50, 50);
					//}
				}
				break;
			}
			case 3: { // Edit Tile
				g2d.setColor(Color.red);
				g2d.fillRect(30,90,334,334);
				for(Button b : tileColors) {
					b.drawTileColors(g2d);
				}
				
				g2d.setColor(Color.red);
				g2d.fillRect(590,290,60,260);
				colorSelection[0].drawTileColors(g2d);
				colorSelection[1].drawTileColors(g2d);
				colorSelection[2].drawTileColors(g2d);
				colorSelection[3].drawTileColors(g2d);
				colorSelection[4].drawTileColors(g2d);
				break;
			}
			case 4: { // Create New Tile
				g2d.setColor(Color.red);
				
				g2d.fillRect(30,90,334,334);
				
				for(Button b : tileColors) {
					b.drawTileColors(g2d);
				}
				
				g2d.setColor(Color.red);
				g2d.fillRect(590,290,60,260);
				colorSelection[0].drawTileColors(g2d);
				colorSelection[1].drawTileColors(g2d);
				colorSelection[2].drawTileColors(g2d);
				colorSelection[3].drawTileColors(g2d);
				colorSelection[4].drawTileColors(g2d);
				break;
			}
			default: break; // Bad State
		}
	}
	
	//----- Mouse And Keyboard Controls -----\\
	
	// Release Key
	@Override
	public void keyReleased(KeyEvent e) {
		char key = e.getKeyChar();
		//String keyLetter = "" + key; // Horrible way to convert a char to a string
		if(nameFieldActive && nameFieldText.length() < 20) {
			// if((letter >= 'A' && letter <= 'Z') || (letter >= 'a' && letter <= 'z') || (letter >= 0 && letter <= 9))
			//int letter = Integer.parseInt(keyLetter);
			
			
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				System.out.println("Pressed back space");
				nameFieldText = nameFieldText.substring(0, nameFieldText.length() - 1);
			} else if(e.getKeyCode() != KeyEvent.VK_SHIFT && e.getKeyCode() != KeyEvent.VK_CAPS_LOCK){
				System.out.println(key);
				nameFieldText += key;
			}
			
			for(Button b : buttonList) {
				if(b.isNameField) {
					b.text = nameFieldText;
				}
			}
		} else if(nameFieldActive && nameFieldText.length() == 20 && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			nameFieldText = nameFieldText.substring(0, nameFieldText.length() - 1);
			for(Button b : buttonList) {
				if(b.isNameField) {
					b.text = nameFieldText;
				}
			}
		}
	}

	// Press Mouse
	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		for(Button b : buttonList) {
			if(x > b.x && x < b.x + b.w && y > b.y && y < b.y + b.h) {
				b.pressed = true;
				activeButton = b;
			}
		}
		
		if(state == 3 || state == 4) { // Create or Edit Tile Page
			for(Button b : tileColors) {
				if(x > b.x && x < b.x + b.w && y > b.y && y < b.y + b.h) {
					b.pressed = true;
					activeButton = b;
				}
			}
			for(Button b : colorSelection) {
				if(x > b.x && x < b.x + b.w && y > b.y && y < b.y + b.h) {
					b.pressed = true;
					activeButton = b;
				}
			}
		} else if(state == 2) { // Saved Tile Page
			Button b;
			System.out.println("HERE2");
			for(int i = 0; i < tileButtons.length; i++) {
				b = tileButtons[i];
				if(x > b.x && x < b.x + b.w && y > b.y && y < b.y + b.h) {
					System.out.println("Tile " + b.tileSpot + " was clicked");
					b.pressed = true;
					activeButton = b;
					nameFieldText = savedTileNames[i];
				}
			} 
		}
	}

	// Release Mouse
	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		activeButton.pressed = false;
		
		if(x > activeButton.x && x < activeButton.x + activeButton.w && y > activeButton.y && y < activeButton.y + activeButton.h) {
			doButtonAction(activeButton.action);
			if(activeButton.action == "clickTile") {
				selectedTile = activeButton.tileSpot;
			}
		} else {
			nameFieldActive = false;
		}
	}
	
	// Unused

	@Override
	public void keyPressed(KeyEvent arg0) {
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent arg0) {
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent arg0) {
	}
}