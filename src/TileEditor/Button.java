package TileEditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Button {

	public int x,y,w,h;
	public String text;
	public boolean pressed = false;
	public String action;
	
	public Button(int x, int y, int w, int h, String text, String action) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
		this.action = action;
	}
	
	public Button() { // Default Constructor
		x = 0;
		y = 0;
		w = 1;
		h = 1;
		text = "Default";
		action = "None";
	}
	
	public void drawButton(Graphics2D g2d) {
		g2d.setColor(Color.blue);
		//g2d.fillRect(x, y, w, h);
		
		int thickness = 4;
		g2d.fill3DRect(x, y, w, h, !pressed);
		for (int i = 1; i <= thickness; i++) {
		      g2d.draw3DRect(x - i, y - i, w + 2 * i - 1, h + 2 * i - 1, true);
		}
		Font font = new Font("Verdana", Font.BOLD, 12);
		g2d.setFont(font);
		
		if(pressed) g2d.setColor(Color.yellow);
		else g2d.setColor(Color.white);
		
		g2d.drawString(text, x+w/2-10, y+h/2+3);
	}
}
