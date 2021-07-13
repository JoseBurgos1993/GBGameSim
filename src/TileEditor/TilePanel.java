package TileEditor;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.sun.glass.events.MouseEvent;

@SuppressWarnings("serial")
public class TilePanel extends JPanel implements KeyListener, MouseListener{

	public TilePanel() {
		setPreferredSize(new Dimension(800, 600));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		addMouseListener(this);
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
	public void mousePressed(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("This better work.");
		
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
