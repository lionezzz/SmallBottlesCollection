import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Scanner;

public class EditFrame {

	private BottleList bottles;
	private BottleEntry bottleToEdit;
	private JFrame frame;
	private ImagePanel imagePanel;
	private JTextField nameField, typeField, volumeField, alcoField, countryField, yearBottlingField,
			yearReceiveField, quantityField;
	private JTextArea commentArea;
	private String path;
	private int scrWidth, scrHeight;
	private Font font;
	private JPanel buttonPanel;
	private boolean edited;

	public static void main(String[] args) {
		EditFrame f = new EditFrame(new BottleList());
	}

	public EditFrame(BottleList list) {
		bottles = list;
		bottleToEdit = null;
		path = "";
		font = new Font("TimesRoman", Font.PLAIN, 20);
		Rectangle dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		scrWidth = dim.width;
		scrHeight = dim.height;
		startUp();
	}

	public EditFrame(BottleList list, BottleEntry entry) {
		this(list);
		editEntry(entry);
	}

	private void startUp() {
		frameSetUp();

		buttonPanelSetUp();

		backgroundSetUp();
	}

	private void frameSetUp() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { returnToMain(); }
		});
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMaximizedBounds(new Rectangle(scrWidth, scrHeight));
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}

	private void buttonPanelSetUp() {
		buttonPanel = new JPanel(new GridLayout(1, 2));
		frame.add(BorderLayout.SOUTH, buttonPanel);

		JButton createButton = new JButton("Сохранить");
		createButton.setFont(font);
		buttonPanel.add(createButton);
		createButton.addActionListener(e -> saveBottle());

		JButton returnButton = new JButton("Вернуться в режим просмотра");
		returnButton.setFont(font);
		buttonPanel.add(returnButton);
		returnButton.addActionListener(e -> returnToMain());
	}

	private void backgroundSetUp() {
		JPanel background = new JPanel(null);
		frame.getContentPane().add(background);

		imagePanelSetUp(background);

		bigInfoPanelSetUp(background);
	}

	private void imagePanelSetUp(JPanel background) {
		imagePanel = new ImagePanel();
		imagePanel.setBounds(5,5,scrWidth / 2 - 10, scrHeight - 10 - buttonPanel.getPreferredSize().height);
		background.add(imagePanel);

		JButton browseButton = new JButton("Выбрать фото");
		browseButton.addActionListener(e -> browseImage());
		imagePanel.add(browseButton);
	}

	private void bigInfoPanelSetUp(JPanel background) {
		JPanel bigInfoPanel = new JPanel(new GridLayout(2,1));
		bigInfoPanel.setBounds(scrWidth / 2 + 5, 5, scrWidth / 2 - 10 , scrHeight -
				10 - buttonPanel.getPreferredSize().height);
		background.add(bigInfoPanel);

		infoPanelSetUp(bigInfoPanel);

		commentPanelSetUp(bigInfoPanel);
	}

	private void infoPanelSetUp(JPanel bigInfoPanel) {
		JPanel infoPanel = new JPanel(new BorderLayout());
		bigInfoPanel.add(infoPanel);

		labelPanelSetUp(infoPanel);

		textFieldPanelSetUp(infoPanel);
	}

	private void labelPanelSetUp(JPanel infoPanel) {
		JPanel labelPanel = new JPanel(new GridLayout(8,1));
		infoPanel.add(BorderLayout.WEST, labelPanel);

		JLabel nameLabel = new JLabel("Название:");
		JLabel typeLabel = new JLabel("Тип:");
		JLabel volumeLabel = new JLabel("Объём:");
		JLabel alcoLabel = new JLabel("Содержание алкоголя:");
		JLabel countryLabel = new JLabel("Страна происхождения:");
		JLabel dateBottlingLabel = new JLabel("Год розлива:");
		JLabel dateReceiveLabel = new JLabel("Год поступления:");
		JLabel quantityLabel = new JLabel("Количество:");

		labelPanel.add(nameLabel);
		labelPanel.add(typeLabel);
		labelPanel.add(volumeLabel);
		labelPanel.add(alcoLabel);
		labelPanel.add(countryLabel);
		labelPanel.add(dateBottlingLabel);
		labelPanel.add(dateReceiveLabel);
		labelPanel.add(quantityLabel);
	}

	private void textFieldPanelSetUp(JPanel infoPanel) {
		JPanel textFieldPanel = new JPanel(new GridLayout(8,1));
		infoPanel.add(BorderLayout.CENTER, textFieldPanel);

		nameField = new JTextField();
		textFieldPanel.add(nameField);

		typeField = new JTextField();
		textFieldPanel.add(typeField);

		volumeField = new JTextField();
		textFieldPanel.add(volumeField);

		alcoField = new JTextField();
		textFieldPanel.add(alcoField);

		countryField = new JTextField();
		textFieldPanel.add(countryField);

		yearBottlingField = new JTextField();
		textFieldPanel.add(yearBottlingField);

		yearReceiveField = new JTextField();
		textFieldPanel.add(yearReceiveField);

		quantityField = new JTextField();
		textFieldPanel.add(quantityField);
	}

	private void commentPanelSetUp(JPanel bigInfoPanel) {
		JPanel commentPanel = new JPanel(new BorderLayout());
		bigInfoPanel.add(commentPanel);

		JLabel commentLabel = new JLabel("Комментарий:");
		commentPanel.add(BorderLayout.NORTH, commentLabel);

		commentArea = new JTextArea();
		commentArea.setLineWrap(true);
		commentArea.setWrapStyleWord(true);
		commentPanel.add(BorderLayout.CENTER, commentArea);
	}

	private void editEntry(BottleEntry entry) {
		bottleToEdit = entry;

		Double volume = entry.getVolume();
		Integer alco = entry.getAlco();
		Integer yearBottling = entry.getYearBottling();
		Integer yearReceive = entry.getYearReceive();
		Integer quantity = entry.getQuantity();

		nameField.setText(entry.getName());
		typeField.setText(entry.getType());
		volumeField.setText(volume < 0 ? "" : Double.toString(volume));
		alcoField.setText(alco < 0 ? "" : Integer.toString(alco));
		countryField.setText(entry.getCountry());
		yearBottlingField.setText(yearBottling < 0 ? "" : Integer.toString(yearBottling));
		yearReceiveField.setText(yearReceive < 0 ? "" : Integer.toString(yearReceive));
		quantityField.setText(quantity < 0 ? "" : Integer.toString(quantity));
		commentArea.setText(entry.getComment());

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
		if (edited) {
			bottles.sort();
		}
		new MainFrame(bottles, edited);
		frame.dispose();
	}

	private void saveBottle() {
		if (!fieldsEmpty()) {
			if (nameField.getText().equals("")) {
				JOptionPane.showMessageDialog(frame, "Введите название", "!!!", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int dialogResult = JOptionPane.showConfirmDialog(frame, "Сохранить?", "Подтвердите действие",
					JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.NO_OPTION) {
				return;
			}

			BottleEntry newBottle = getNewBottle();
			if (bottleToEdit != null) {
				if (!bottleToEdit.equals(newBottle)) {
					bottles.addBottle(newBottle);
					bottles.deleteBottle(bottleToEdit);
				}
				bottleToEdit = null;
			} else {
				bottles.addBottle(newBottle);
			}
			edited = true;
			setEmptyFields();
		}
		if (!path.equals("")) {
			setNullPath();
		}
	}

	private boolean fieldsEmpty() {
		return nameField.getText().equals("") && typeField.getText().equals("") &&	volumeField.getText().equals("") &&
				alcoField.getText().equals("") && countryField.getText().equals("") && yearBottlingField.getText().equals("") &&
				yearReceiveField.getText().equals("") && quantityField.getText().equals("") && commentArea.getText().equals("");
	}

	private BottleEntry getNewBottle() {
		String vol = volumeField.getText().replace(',','.');
		Double volume = vol.matches("\\d+[.]\\d+") ? Double.parseDouble(vol) : null;

		Scanner scanner = new Scanner(alcoField.getText());
		Integer alco = scanner.hasNextInt() ? scanner.nextInt() : null;

		scanner = new Scanner(yearBottlingField.getText());
		Integer yearBottling = scanner.hasNextInt() ? scanner.nextInt() : null;

		scanner = new Scanner(yearReceiveField.getText());
		Integer yearReceive = scanner.hasNextInt() ? scanner.nextInt() : null;

		scanner = new Scanner(quantityField.getText());
		Integer quantity = scanner.hasNextInt() ? scanner.nextInt() : null;

		return new BottleEntry(nameField.getText(), typeField.getText(), volume,
				alco, countryField.getText(), yearBottling,
				yearReceive, quantity, commentArea.getText(), path);
	}

	private void setEmptyFields() {
		nameField.setText("");
		typeField.setText("");
		volumeField.setText("");
		alcoField.setText("");
		countryField.setText("");
		yearBottlingField.setText("");
		yearReceiveField.setText("");
		quantityField.setText("");
		commentArea.setText("");
	}

	private void setNullPath() {
		path = "";
		imagePanel.setNullImage();
		imagePanel.repaint();
	}
}
