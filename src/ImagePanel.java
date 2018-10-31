import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

	private Image image;

	public ImagePanel() {
		image = null;
	}

	public ImagePanel(String location) {
		image = new ImageIcon(location).getImage();
	}

	public ImagePanel(GridLayout grid) { //УДОЛИ!!!!!!!!!!!!!!!
		super(grid);
	}

	public void paintComponent(Graphics g) {
		if (image == null) {
			g.setColor(Color.GRAY);
			g.fillRect(0,0, this.getWidth(), this.getHeight());
		} else {
			g.drawImage(image, 0,0,this);
		}



	}
}
