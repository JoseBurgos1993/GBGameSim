package TileEditor;

import javax.swing.JFrame;


public class TileEditor{
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello, world!");
		JFrame frame = new JFrame("Tile Editor");
		TilePanel editor = new TilePanel();
		frame.add(editor);
		frame.getContentPane().add(editor);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		frame.setResizable(true);
		frame.pack();
		
		//frame.setPreferredSize(new Dimension(GamePanel.WIDTH,GamePanel.HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
