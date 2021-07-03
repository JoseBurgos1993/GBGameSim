package Engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;



@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	// Panel Attributes \\
	public static int WIDTH = 160;
	public static int HEIGHT = 144;
	public static int MAGNIFICATION = 4;
	private Thread thread;
	private boolean running;
	private long targetTime;
	private static final int FRAME_RATE = 60;
	
	// TEMP TESTING GRAPHICS \\
	private int frame_count = 0;
	private int offset = 0;
	private Color graphicsArray[] = new Color[23040];
	private boolean buttonPress = false;
	
	// Layers \\
	private int BackgroundLayer[] = new int[20];
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

			frame_count++;
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			if(frame_count == FRAME_RATE) {
				System.out.println("Time Used = " + elapsed / 1000000 + "/" + targetTime);
				frame_count = 0;
			}
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
		for(int i = 0; i < graphicsArray.length; i++) {
			graphicsArray[i] = Color.black;
		}
	}
	
	private void update() {
		// Read User Input
		// poop
	}
	
	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}
	
	private void render(Graphics2D g2d) {
		//g2d.clearRect(0, 0, WIDTH*MAGNIFICATION, HEIGHT*MAGNIFICATION);
		
		/*
		int randx = (int)Math.floor(Math.random()*159);
		int randy = (int)Math.floor(Math.random()*143);
		g2d.setPaint(Color.red);
		g2d.fillRect(randx * MAGNIFICATION, randy * MAGNIFICATION, MAGNIFICATION, MAGNIFICATION);
		
		*/
		
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
		
		int temp;
		for(int i = 0; i < graphicsArray.length; i++) {
			temp = i%3;
			switch(temp) {
				case 0: graphicsArray[i]=one; break;
				case 1: graphicsArray[i]=two; break;
				case 2: graphicsArray[i]=three; break;
				default: graphicsArray[i]=Color.yellow; break;
			}
			offset++;
			if(offset>2) offset = 0;
		}
		for(int i = 0; i < 23040; i++) {
			
			g2d.setColor(graphicsArray[i]);
			g2d.drawRect((i % WIDTH) * MAGNIFICATION, ((int)(i / WIDTH)) * MAGNIFICATION, MAGNIFICATION - 1, MAGNIFICATION - 1);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		buttonPress = true;
		if(key == KeyEvent.VK_UP) {
			offset = 0;
		}
		if(key == KeyEvent.VK_DOWN) {
			offset = 1;
		}
		if(key == KeyEvent.VK_LEFT) {
			offset = 2;
		}
		if(key == KeyEvent.VK_RIGHT) {
			offset = 3;
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
