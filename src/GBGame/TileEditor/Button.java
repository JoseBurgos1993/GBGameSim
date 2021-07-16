package TileEditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Button {

	public int x,y,w,h;
	public String text;
	public boolean pressed = false;
	public String action;
	public boolean isNameField = false;
	public boolean isColor = false;
	public int tileSpot = -1;
	public int color = 0;
	
	public Button(int x, int y, int w, int h, String text, String action, boolean isNameField) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
		this.action = action;
		this.isNameField = isNameField;
		if(action == "color 0") {
			color = 0;
		}
		else if(action == "color 1") {
			color = 1;
		}
		else if(action == "color 2") {
			color = 2;
		}
		else if(action == "color 3") {
			color = 3;
		}
	}
	
	public Button(int x, int y, int w, int h, String text, int tileSpot, boolean isNameField) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
		this.tileSpot = tileSpot;
		this.isNameField = isNameField;
		this.isColor = true;
	}
	
	public Button() { // Default Constructor
		x = 0;
		y = 0;
		w = 1;
		h = 1;
		text = "Default";
		action = "None";
	}
	
	public void drawTileColors(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.drawRect(x, y, w, h);
		
		switch(color) {
			case 0: g2d.setColor(Color.white); break;
			case 1: g2d.setColor(Color.LIGHT_GRAY); break;
			case 2: g2d.setColor(Color.DARK_GRAY); break;
			case 3: g2d.setColor(Color.black); break;
			case 4: g2d.setColor(Color.green); break;
		}
		g2d.fillRect(x+1, y+1, w-1, h-1);
		
	}
	
	public void drawNameField(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.drawRect(x, y, w, h);

		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(x+1, y+1, w-2, h-2);
		
		g2d.setColor(Color.BLACK);
		
		g2d.drawString(text, x+5, y+h/2+3);
	}
	
	public void drawButton(Graphics2D g2d) {
		g2d.setColor(Color.blue);
		//g2d.fillRect(x, y, w, h);
		
		int thickness = 4;
		g2d.fill3DRect(x, y, w, h, !pressed);
		for (int i = 1; i <= thickness; i++) {
		      g2d.draw3DRect(x - i, y - i, w + 2 * i, h + 2 * i, true);
		}
		Font font = new Font("Verdana", Font.BOLD, 12);
		g2d.setFont(font);
		
		if(pressed) g2d.setColor(Color.yellow);
		else g2d.setColor(Color.white);
		
		g2d.drawString(text, x+w/2-10, y+h/2+3);
	}
}
