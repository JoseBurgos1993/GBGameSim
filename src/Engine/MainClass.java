package Engine;

import java.awt.Dimension;

import javax.swing.JFrame;

import Engine.GamePanel;
/**
 * @author burgo
 *
 */

public class MainClass {
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Game Window");
		GamePanel game = new GamePanel();
		frame.add(game);
		//frame.setContentPane(new GamePanel());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		frame.setResizable(true);
		frame.pack();
		
		//frame.setPreferredSize(new Dimension(GamePanel.WIDTH,GamePanel.HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}

}
