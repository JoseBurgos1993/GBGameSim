package TileEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.sun.glass.events.MouseEvent;

@SuppressWarnings("serial")
public class TilePanel extends JPanel implements Runnable, KeyListener, MouseListener{
	// Panel Attributes \\
	public static Graphics2D g2d;
	private BufferedImage image;
	private boolean running;
	private Thread thread;
	private long targetTime;
	private static final int FRAME_RATE = 60;
	private String state = "MainMenu";
	
	// Buttons \\
	private Button buttonList[] = new Button[3];
	private Button activeButton = new Button();
	private boolean nameFieldActive = false;
	private String nameFieldText = "";
	
	// Tiles \\
	private Button[] tileColors = new Button[64];
	private Button[] colorSelection = new Button[5];
	private int pickedTile = -1;
	private int selectedColor = 0;
	
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
		
		makeMainMenu();
	}
	
	private void makeMainMenu() {
		state = "MainMenu";
		buttonList = new Button[3];
		tileColors = new Button[64];
		buttonList[0] = new Button(100,350, 100, 50, "Create", "gotoCreate", false);
		buttonList[1] = new Button(300,350, 100, 50, "Edit", "Edit", false);
		buttonList[2] = new Button(500,350, 100, 50, "Leave", "Leave", false);
		nameFieldActive = false;
	}
	
	private void makeCreateScreen() {
		state = "Create";
		buttonList = new Button[3];
		buttonList[0] = new Button(680,20, 100, 50, "Back", "gotoMainMenu", false);
		buttonList[1] = new Button(680,200,100,50, "Save", "Save", false);
		buttonList[2] = new Button(10,10,200,50, "", "clickedNameField", true);
		nameFieldActive = true;

		tileColors = new Button[64];

		for(int i = 0; i < 8; i++) { // Vertical
			for(int j = 0; j < 8; j++) { // Horizonal
				tileColors[j+i*8] = new Button(40+42*j, 100+42*i, 40, 40, "", "color", false);
			}
		}
		
		colorSelection[0] = new Button(600, 300, 40, 40, "", "color 0", false);
		colorSelection[1] = new Button(600, 350, 40, 40, "", "color 1", false);
		colorSelection[2] = new Button(600, 400, 40, 40, "", "color 2", false);
		colorSelection[3] = new Button(600, 450, 40, 40, "", "color 3", false);
	}
	
	private void doButtonAction(String action) {
		if(action == "Leave") {
			System.exit(0);
		}
		else if(action == "gotoCreate") {
			makeCreateScreen();
		}
		else if(action == "gotoMainMenu") {
			makeMainMenu();
		}
		else if(action == "Save") {
			System.out.println("Pressed the save button.");
		}
		else if(action == "Edit") {
			System.out.println("Pressed the edit button.");
		}
		else if(action == "clickedNameField") {
			System.out.println("Pressed the name field.");
			nameFieldActive = true;
		}
		else if(action == "color 0") {
			System.out.println("Pressed the color 0.");
			selectedColor = 0;
		}
		else if(action == "color 1") {
			System.out.println("Pressed the color 1.");
			selectedColor = 1;
		}
		else if(action == "color 2") {
			System.out.println("Pressed the color 2.");
			selectedColor = 2;
		}
		else if(action == "color 3") {
			System.out.println("Pressed the color 3.");
			selectedColor = 3;
		} else if(action == "color") {
			changeColor();
		}
	}
	
	private void changeColor() {
		activeButton.color = selectedColor;
	}
	
	private void update() {
		
	}
	
	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}
	
	private void render(Graphics2D g2d) {
		g2d.clearRect(0, 0, 800, 600);
		for(Button b : buttonList) {
			if(b.isNameField) {
				b.drawNameField(g2d);
			} else if(b.tileSpot == -1){
				b.drawButton(g2d);
			}
		}
		if(state == "Create") {
			g2d.setColor(Color.red);
			g2d.fillRect(30,90,334,334);
			for(Button b : tileColors) {
				b.drawTileColors(g2d);
			}
			
			g2d.setColor(Color.red);
			g2d.fillRect(590,290,60,220);
			colorSelection[0].drawTileColors(g2d);
			colorSelection[1].drawTileColors(g2d);
			colorSelection[2].drawTileColors(g2d);
			colorSelection[3].drawTileColors(g2d);
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

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
			} else{
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

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

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
		if(state == "Create") {
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
		}
		
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		activeButton.pressed = false;
		
		if(x > activeButton.x && x < activeButton.x + activeButton.w && y > activeButton.y && y < activeButton.y + activeButton.h) {
			System.out.println(activeButton.isColor);
			doButtonAction(activeButton.action);
			//if(activeButton.isColor) {
			//	changeColor();
			//} else {
			//	
			//}
		} else {
			nameFieldActive = false;
		}
	}
}
