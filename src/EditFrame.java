import javax.swing.*;
import java.awt.*;
import java.io.File;

public class EditFrame {

	private BottleList bottles;
	private BottleEntry bottleToEdit;
	private JFrame frame = new JFrame();
	private ImagePanel imagePanel;
	private JTextArea nameArea, typeArea, commentArea;
	private String path;

	public EditFrame(BottleList list) {
		bottles = list;
		path = "";
	}

	public void showFrame() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int ScrWidth = dim.width, ScrHeight = dim.height;
		Font font = new Font("TimesRoman", Font.PLAIN, 20);

		JPanel buttonBox = new JPanel(new GridLayout(1, 2));
		frame.getContentPane().add(BorderLayout.SOUTH, buttonBox);

		JButton createButton = new JButton("Сохранить");
		createButton.setFont(font);
		buttonBox.add(createButton);
		createButton.addActionListener(e -> saveBottle());

		JButton returnButton = new JButton("Вернуться в режим просмотра");
		returnButton.setFont(font);
		buttonBox.add(returnButton);
		returnButton.addActionListener(e -> returnToMain());

		JPanel background = new JPanel(null);
		frame.getContentPane().add(background);

		imagePanel = new ImagePanel();
		imagePanel.setBounds(5,5,ScrWidth / 2 - 10, ScrHeight - 70);
		background.add(imagePanel);

		JButton browseButton = new JButton("Выбрать фото");
		browseButton.addActionListener(e -> browseImage());
		imagePanel.add(browseButton);

		Box infoBox = new Box(BoxLayout.Y_AXIS);
		infoBox.setBounds(ScrWidth / 2 + 5, 5, ScrWidth / 2 - 10 , ScrHeight - 70);
		background.add(infoBox);

		JLabel nameLabel = new JLabel("Имя:");
		infoBox.add(nameLabel);
		nameArea = new JTextArea();
		nameArea.setLineWrap(true);
		nameArea.setWrapStyleWord(true);
		infoBox.add(nameArea);

		JLabel typeLabel = new JLabel("Тип:");
		infoBox.add(typeLabel);
		typeArea = new JTextArea();
		infoBox.add(typeArea);

		JLabel commentLabel = new JLabel("Комментарий:");
		infoBox.add(commentLabel);
		commentArea = new JTextArea();
		commentArea.setLineWrap(true);
		commentArea.setWrapStyleWord(true);
		infoBox.add(commentArea);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}

	public void editEntry(BottleEntry entry) {
		showFrame();
		bottleToEdit = entry;
		nameArea.setText(bottleToEdit.getName());
		typeArea.setText(bottleToEdit.getType());
		commentArea.setText(bottleToEdit.getComment());
		path = bottleToEdit.getImage();
		if (path == null) path = "";
		imagePanel.setImage(bottleToEdit.getImage(), imagePanel.getWidth(), imagePanel.getHeight());
		imagePanel.repaint();
	}

	private void browseImage() {
		JFileChooser fileOpen = new JFileChooser();
		fileOpen.setCurrentDirectory(new File("photo/."));
		fileOpen.showOpenDialog(frame);
		if (fileOpen.getSelectedFile() != null) {
			path = fileOpen.getSelectedFile().toString();
			imagePanel.setImage(path, imagePanel.getWidth(), imagePanel.getHeight());
			imagePanel.repaint();
		}
	}

	private void returnToMain() {
		MainFrame f = new MainFrame(bottles);
		f.startUp();
		frame.dispose();
	}

	private void saveBottle() {
		BottleEntry newBottle = new BottleEntry(nameArea.getText(), typeArea.getText(),commentArea.getText(),path);
		if (bottleToEdit != null && !bottleToEdit.equals(newBottle)) {
			bottles.addBottle(newBottle);
			bottles.deleteBottle(bottleToEdit);
		} else if (bottleToEdit == null) {
			bottles.addBottle(newBottle);
		}
		bottles.setUnsorted();
		bottleToEdit = null;
		path = "";
		imagePanel.setNullImage();
		imagePanel.repaint();
		nameArea.setText("");
		typeArea.setText("");
		commentArea.setText("");
	}
}
