import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class MainFrame {
	private BottleList bottles;
	private JFrame frame = new JFrame();
	private JList<BottleEntry> bottlesList;
	private JList<String> typeList;
	private ImagePanel imagePanel;
	private JPanel rightPanel;
	private JScrollPane typeScrollPane;
	private JTextArea nameArea, typeArea, commentArea;
	private JButton sortButton, typesButton, countButton;
	private int sortButtonMark = 0, fullListMark = 0, typesButtonMark = 0;
	private JTextField searchField;
	private final String ENTER_TEXT = "Введите текст запроса", NOT_FOUND_TEXT = "Не найдено";

	public MainFrame(BottleList list) {	bottles = list;	}

	public void startUp() {
		Font font = new Font("TimesRoman", Font.PLAIN, 20);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int ScrWidth = dim.width, ScrHeight = dim.height;

		JPanel bottomPanel = new JPanel(new GridLayout(2,1, 0,5));
		frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);

		JPanel searchBox = new JPanel(new BorderLayout());
		bottomPanel.add(searchBox);

		searchField = new JTextField("Введите текст запроса");
		searchField.setFont(font);
		searchBox.add(searchField);
		searchField.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {autoEmptySearch(e);}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});

		JButton searchButton = new JButton("              Поиск              ");
		searchButton.setFont(font);
		searchBox.add(BorderLayout.EAST,searchButton);
		searchButton.addActionListener(e -> search());

		JPanel buttonBox = new JPanel(new GridLayout(1, 6));
		bottomPanel.add(buttonBox);

		sortButton = new JButton("Сортировать по имени");
		typesButton = new JButton("Показать типы");
		countButton = new JButton("Количество: " + bottles.getCount());
		JButton addButton = new JButton("Добавить"), exitButton = new JButton("Сохранить и выйти"),
				fullListButton = new JButton("Показать весь список");

		addButton.setFont(font);
		buttonBox.add(addButton);
		addButton.addActionListener(e -> addNewEntry());

		sortButton.setFont(font);
		buttonBox.add(sortButton);
		sortButton.addActionListener(e -> sortBy());

		typesButton.setFont(font);
		buttonBox.add(typesButton);
		typesButton.addActionListener(e -> showTypes());

		fullListButton.setFont(font);
		buttonBox.add(fullListButton);
		fullListButton.addActionListener(e -> showFullList());

		countButton.setFont(font);
		buttonBox.add(countButton);
		countButton.addActionListener(e -> showCount());

		exitButton.setFont(font);
		buttonBox.add(exitButton);
		exitButton.addActionListener(e -> saveAndExit());

		JPanel background = new JPanel();
		frame.getContentPane().add(BorderLayout.CENTER,background);
		background.setLayout(null);

		bottlesList = new JList<>();
		bottlesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (bottles.getCount() > 0) {
			bottlesList.setListData(bottles.getBottleArray());
		}
		bottlesList.addListSelectionListener(this::showChosenBottle);

		JScrollPane scrollPane = new JScrollPane(bottlesList);
		scrollPane.setBounds(5,5,ScrWidth / 2 - 10, ScrHeight - 88);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		background.add(scrollPane);

		rightPanel = new JPanel(new GridLayout(2,1));
		rightPanel.setBounds(ScrWidth / 2 + 5, 5, ScrWidth / 2 - 10 , ScrHeight - 85);
		background.add(rightPanel);

		typeList = new JList<>();
		typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		typeList.addListSelectionListener(this::showByType);
		typeScrollPane = new JScrollPane(typeList);
		typeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		typeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		typeScrollPane.setBounds(rightPanel.getX(), 5, rightPanel.getWidth(), rightPanel.getHeight());
		typeScrollPane.setVisible(false);
		background.add(typeScrollPane);

		imagePanel = new ImagePanel();
		rightPanel.add(imagePanel);

		Box infoBox = new Box(BoxLayout.Y_AXIS);
		rightPanel.add(infoBox);

		JLabel nameLabel = new JLabel("Имя:");
		infoBox.add(nameLabel);
		nameArea = new JTextArea();
		nameArea.setEditable(false);
		nameArea.setLineWrap(true);
		nameArea.setWrapStyleWord(true);
		infoBox.add(nameArea);

		JLabel typeLabel = new JLabel("Тип:");
		infoBox.add(typeLabel);
		typeArea = new JTextArea();
		typeArea.setEditable(false);
		infoBox.add(typeArea);

		JLabel commentLabel = new JLabel("Комментарий:");
		infoBox.add(commentLabel);
		commentArea = new JTextArea();
		commentArea.setEditable(false);
		commentArea.setLineWrap(true);
		commentArea.setWrapStyleWord(true);
		infoBox.add(commentArea);

		JPanel editPanel = new JPanel(new GridLayout(1,2));
		infoBox.add(editPanel);

		JButton editButton = new JButton("Изменить");
		editPanel.add(editButton);
		editButton.addActionListener(e -> editEntry());

		JButton deleteButton = new JButton("Удалить");
		editPanel.add(deleteButton);
		deleteButton.addActionListener(e -> deleteChosen());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}

	private void addNewEntry() {
		EditFrame editFrame = new EditFrame(bottles);
		editFrame.showFrame();
		frame.dispose();
	}

	private void sortBy() {
		if (sortButtonMark == 0) {
			if (bottles.getSortingMark() != 0) {
				bottles.sortByName();
				bottlesList.setListData(bottles.getBottleArray());
			}
			sortButton.setText("Сортировать по типу");
			sortButtonMark++;
		} else {
			bottles.sortByType();
			bottlesList.setListData(bottles.getBottleArray());
			sortButton.setText("Сортировать по имени");
			sortButtonMark = 0;
		}
	}

	private void showTypes() {
		if (typesButtonMark == 0) {
			typesButtonMark++;
			rightPanel.setVisible(false);
			typeScrollPane.setVisible(true);
			typeList.setListData(bottles.getTypes());
			typesButton.setText("Скрыть типы");
			clearAll();
		} else { hideTypes(); }
	}

	private void hideTypes() {
		typeScrollPane.setVisible(false);
		rightPanel.setVisible(true);
		typesButton.setText("Показать типы");
		typesButtonMark = 0;
	}

	private void saveAndExit() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("database.ser"));
			os.writeObject(bottles);
			os.close();
		} catch (Exception ex) { ex.printStackTrace(); }
		System.exit(0);
	}

	private void editEntry() {
		if (bottlesList.getSelectedValue() != null) {
			EditFrame editFrame = new EditFrame(bottles);
			editFrame.editEntry(bottlesList.getSelectedValue());
			frame.dispose();
		}
	}

	private void showChosenBottle(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			BottleEntry chosen = bottlesList.getSelectedValue();
			if (chosen != null) {
				nameArea.setText(chosen.getName());
				typeArea.setText(chosen.getType());
				commentArea.setText(chosen.getComment());
				imagePanel.setImage(chosen.getImage(), imagePanel.getWidth(), imagePanel.getHeight());
				imagePanel.repaint();
				hideTypes();
			}

		}
	}

	private void showByType(ListSelectionEvent event) {
		fullListMark++;
		if (!event.getValueIsAdjusting()) {
			bottlesList.setListData(bottles.getByType(typeList.getSelectedValue()));
		}
	}

	private void deleteChosen() {
		bottles.deleteBottle(bottlesList.getSelectedValue());
		bottlesList.setListData(bottles.getBottleArray());
		clearAll();
		showCount();
	}

	private void showCount() {
		countButton.setText("Количество: " + bottles.getCount());
	}

	private void clearAll() {
		imagePanel.setNullImage();
		nameArea.setText("");
		typeArea.setText("");
		commentArea.setText("");
		bottlesList.clearSelection();
		imagePanel.repaint();
	}

	private void showFullList() {
		if (fullListMark != 0) {
			fullListMark = 0;
			bottlesList.setListData(bottles.getBottleArray());
			if (typesButtonMark != 0) {
				hideTypes();
			}
		}
	}

	private void search() {
		String searchText = searchField.getText();
		if (searchText.length() == 0 || searchText.equals(NOT_FOUND_TEXT)) {
			searchField.setText(ENTER_TEXT);
		} else if (!searchText.equals(ENTER_TEXT)) {
			BottleEntry[] searchResult = bottles.searchEntries(searchText);
			if (searchResult.length == 0) {
				searchField.setText(NOT_FOUND_TEXT);
			} else {
				bottlesList.setListData(searchResult);
				fullListMark++;
				searchField.setText(ENTER_TEXT);
			}
		}
	}

	private void autoEmptySearch(MouseEvent event) {
		if (searchField.getText().equals(ENTER_TEXT) || searchField.getText().equals(NOT_FOUND_TEXT)) {
			searchField.setText("");
		}
	}
}