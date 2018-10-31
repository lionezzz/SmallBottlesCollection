import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class MainFrame {
	BottleList bottles;
	JFrame frame = new JFrame();
	JList bottlesList;
	ImagePanel imagePanel = new ImagePanel();
	JTextField name, type, comment;

	public void startUp(BottleList list) {
		bottles = list;

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int ScrWidth = dim.width, ScrHeight = dim.height;

		JPanel buttonBox = new JPanel(new GridLayout(1, 4));
		JButton addButton = new JButton("Добавить"), sortButton = new JButton("Сортировать по имени"),
				typesButton = new JButton("Показать типы"), exitButton = new JButton("Сохранить и выйти");
		Font font = new Font("TimesRoman", Font.PLAIN, 20);
		addButton.setFont(font);
		sortButton.setFont(font);
		typesButton.setFont(font);
		exitButton.setFont(font);
		buttonBox.add(addButton);
		buttonBox.add(sortButton);
		buttonBox.add(typesButton);
		buttonBox.add(exitButton);
		frame.getContentPane().add(BorderLayout.SOUTH, buttonBox);

		JPanel background = new JPanel();
		frame.getContentPane().add(BorderLayout.CENTER,background);
		background.setLayout(null);

		bottles.addBottle(new BottleEntry("name1", "type1", "com1", "img1"));
		bottles.addBottle(new BottleEntry("name2", "type2", "com2", "img2"));
		bottles.addBottle(new BottleEntry("name3", "type3", "com3", "img3"));
		bottles.addBottle(new BottleEntry("name4", "type4", "com4", "img4"));

		JList<BottleEntry> leftList = new JList<>();
		leftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (bottles.getLength() > 0) {
			leftList.setListData(bottles.getBottleArray());
		}

		JScrollPane scrollPane = new JScrollPane(leftList);
		scrollPane.setBounds(5,5,ScrWidth / 2 - 10, ScrHeight - 70);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		background.add(scrollPane);

		JPanel rightPanel = new JPanel(new GridLayout(2,1));
		rightPanel.setBounds(ScrWidth / 2 + 5, 5, ScrWidth / 2 - 10 , ScrHeight - 70);
		background.add(rightPanel);

		JPanel imagePanel = new ImagePanel();
		rightPanel.add(imagePanel);

		Box infoBox = new Box(BoxLayout.Y_AXIS);
		rightPanel.add(infoBox);

		JLabel nameLabel = new JLabel("Имя:");
		infoBox.add(nameLabel);
		JTextArea nameArea = new JTextArea();
		nameArea.setEditable(false);
		nameArea.setLineWrap(true);
		nameArea.setWrapStyleWord(true);
		infoBox.add(nameArea);

		JLabel typeLabel = new JLabel("Тип:");
		infoBox.add(typeLabel);
		JTextArea typeArea = new JTextArea();
		typeArea.setEditable(false);
		infoBox.add(typeArea);

		JLabel commentLabel = new JLabel("Комментарий:");
		infoBox.add(commentLabel);
		JTextArea commentArea = new JTextArea();
		commentArea.setEditable(false);
		commentArea.setLineWrap(true);
		commentArea.setWrapStyleWord(true);
		infoBox.add(commentArea);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}