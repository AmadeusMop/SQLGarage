import javax.swing.*;


public class Screen extends JFrame {
	
	public Screen() {
		JPanel main = new JPanel();
		add(main);
		repaint();
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Screen s = new Screen();
	}
}
