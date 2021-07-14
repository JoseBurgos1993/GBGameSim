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
	
	// Buttons \\
	private Button buttonList[] = new Button[3];
	private Button activeButton = new Button();
	
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

			// For testing \\
			//frame_count++;
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			
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
		image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		
		makeMainMenu();
	}
	
	private void makeMainMenu() {
		buttonList[0] = new Button(100,350, 100, 50, "Create", "Create");
		buttonList[1] = new Button(300,350, 100, 50, "Edit", "Edit");
		buttonList[2] = new Button(500,350, 100, 50, "Leave", "Leave");
	}
	
	private void doButtonAction(String action) {
		
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
		
		for(Button b : buttonList) {
			b.drawButton(g2d);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		int x = e.getX();
		int y = e.getY();
		for(Button b : buttonList) {
			if(x > b.x && x < b.x + b.w && y > b.y && y < b.y + b.h) {
				System.out.println("You pressed the " + b.text + " button!");
				b.pressed = true;
				activeButton = b;
			}
		}
		
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		activeButton.pressed = false;
		if(x > activeButton.x && x < activeButton.x + activeButton.w && y > activeButton.y && y < activeButton.y + activeButton.h) {
			System.out.println("You activated the " + activeButton.text + " button!");
			doButtonAction(activeButton.action);
		} else {
			System.out.println("You failed to activate the " + activeButton.text + " button.");
		}
	}


}
