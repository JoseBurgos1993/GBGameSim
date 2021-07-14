package TileEditor;

import javax.swing.JFrame;


public class TileEditor{
		
	public static void main(String[] args) {
		JFrame frame = new JFrame("Tile Editor");
		TilePanel editor = new TilePanel();
		frame.add(editor);
		frame.getContentPane().add(editor);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		frame.setResizable(true);
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
