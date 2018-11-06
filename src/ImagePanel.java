import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

	private Image image;

	public ImagePanel() { image = null; }

	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0,0, this.getWidth(), this.getHeight());
		if (image != null) {
			g.drawImage(image, (this.getWidth() - image.getWidth(this)) / 2,
					(this.getHeight() - image.getHeight(this)) / 2,this);
		}
	}

	public void setImage(String location, int newWidth, int newHeight) {
		image = new ImageIcon(location).getImage();
		double newScale = (double) newWidth / newHeight;
		double scale = (double)image.getWidth(this) / image.getHeight(this);
		if (newScale > scale) {
			image = image.getScaledInstance((int) (newHeight * scale), newHeight, Image.SCALE_DEFAULT);
		} else {
			image = image.getScaledInstance(newWidth, (int) (newWidth / scale), Image.SCALE_DEFAULT);
		}
	}

	public void setNullImage() { image = null; }
}
