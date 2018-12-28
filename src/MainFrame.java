import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Comparator;

public class MainFrame {
	private BottleList bottles;
	private JFrame frame;
	private JList<BottleEntry> bottlesList;
	private JList showByList;
	private JScrollPane showByScrollPane;
	private ImagePanel imagePanel;
	private JPanel bottomPanel, rightCardPanel, bottomCardPanel;
	private JTextField nameField, typeField, volumeField, alcoField, countryField, yearBottlingField,
			yearReceiveField, quantityField;
	private JTextArea commentArea;
	private JButton countButton, fullListButton;
	private JToggleButton sortByButton, showByButton;
	private int scrWidth, scrHeight;
	private JTextField searchField;
	private Font font;
	private CardLayout card;
	private Field sortedBy, showedBy;
	private final String ENTER_TEXT = "Введите текст запроса", NOT_FOUND_TEXT = "Не найдено", SHOW = "Show", SORT = "Sort",
			SEARCH = "Searh", INFO = "Info", LIST = "List";

	private DefaultListModel<BottleEntry> listModel;

	public MainFrame(BottleList list) {
		bottles = list;
		sortedBy = Field.NAME;
		font = new Font("TimesRoman", Font.PLAIN, 20);
		Rectangle dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		scrWidth = dim.width;
		scrHeight = dim.height;
		card = new CardLayout();
		SwingUtilities.invokeLater(this::startUp);
	}

	public static void main(String[] args) {
		MainFrame f = new MainFrame(new BottleList());
		f.bottles.addBottle(new BottleEntry("name1", "type1", 1.0, 40, "RU", 2001, 2002, 1,
				"comm1", ""));
		f.bottles.addBottle(new BottleEntry("name2", "type3", 5.0, 1, "SW", 2004, 2001, 2,
				"comm1", ""));
		f.bottles.addBottle(new BottleEntry("name3", "type2", 4.0, 40, "GE", 1111, 2005, 1,
				"comm1", ""));
		f.bottles.addBottle(new BottleEntry("name4", "type1", 1.0, 45, "RU", 2004, 2002, 2,
				"comm1", ""));
		f.bottles.addBottle(new BottleEntry("name5", "type2", 2.0, 40, "EN", 2001, 2005, 1,
				"comm1", ""));
		f.bottles.addBottle(new BottleEntry("name6", "type3", 4.0, 45, "GE", 200, 2002, 2,
				"comm1", ""));
		f.bottles.addBottle(new BottleEntry("", "", null, null, "", null, null, null,
				"", ""));
	}



	private void startUp() {
		frameSetUp();

		bottomPanelSetUp();

		backgroundSetUp();
	}

	private void frameSetUp() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { saveAndExit(); }
		});
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMaximizedBounds(new Rectangle(scrWidth, scrHeight));
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}

	private void bottomPanelSetUp() {
		bottomPanel = new JPanel(new GridLayout(2,1, 0,5));
		frame.add(BorderLayout.SOUTH, bottomPanel);

		bottomCardPanel = new JPanel(card);
		bottomPanel.add(bottomCardPanel);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 6));
		buttonPanelSetUp(buttonPanel);

		JPanel searchPanel = new JPanel(new BorderLayout());
		searchPanelSetUp(searchPanel);

		JPanel sortPanel = new JPanel(new GridLayout(1, 7));
		sortPanelSetUp(sortPanel);

		JPanel showByPanel = new JPanel(new GridLayout(1, 6));
		showByPanelSetUp(showByPanel);

		bottomCardPanel.add(searchPanel, SEARCH);
		bottomCardPanel.add(sortPanel, SORT);
		bottomCardPanel.add(showByPanel, SHOW);
	}

	private void buttonPanelSetUp(JPanel buttonPanel) {
		bottomPanel.add(buttonPanel);

		sortByButton = new JToggleButton("Сортировать по...");
		showByButton = new JToggleButton("Отобразить...");
		countButton = new JButton("Количество: " + bottles.getCount());
		fullListButton = new JButton("Показать весь список");
		JButton addButton = new JButton("Добавить"), exitButton = new JButton("Закрыть");

		addButton.setFont(font);
		buttonPanel.add(addButton);
		addButton.addActionListener(e -> addNewEntry());

		sortByButton.setFont(font);
		buttonPanel.add(sortByButton);
		sortByButton.addActionListener(e -> showSortPanel());

		showByButton.setFont(font);
		buttonPanel.add(showByButton);
		showByButton.addActionListener(e -> showParameters());

		fullListButton.setFont(font);
		fullListButton.setEnabled(false);
		buttonPanel.add(fullListButton);
		fullListButton.addActionListener(e -> showFullList());

		countButton.setFont(font);
		buttonPanel.add(countButton);
		countButton.addActionListener(e -> showCount());

		exitButton.setFont(font);
		buttonPanel.add(exitButton);
		exitButton.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
	}

	private void searchPanelSetUp(JPanel searchPanel) {
		searchField = new JTextField("Введите текст запроса");
		searchField.setFont(font);
		searchPanel.add(searchField);
		searchField.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {autoEmptySearch(e);}
		});

		JButton searchButton = new JButton("Поиск");
		searchButton.setFont(font);
		searchPanel.add(BorderLayout.EAST,searchButton);
		searchButton.addActionListener(e -> search());
	}

	private void sortPanelSetUp(JPanel sortPanel) {
		JButton nameSortButton = new JButton("Названию"),
				typeSortButton = new JButton("Типу"),
				volumeSortButton = new JButton("Объёму"),
				alcoSortButton = new JButton("Содержанию алкоголя"),
				countrySortButton = new JButton("Стране происхождения"),
				yearBottlingSortButton = new JButton("Дате розлива"),
				yearReceiveSortButton = new JButton("Дате поступления");

		sortPanel.add(nameSortButton);
		sortPanel.add(typeSortButton);
		sortPanel.add(volumeSortButton);
		sortPanel.add(alcoSortButton);
		sortPanel.add(countrySortButton);
		sortPanel.add(yearBottlingSortButton);
		sortPanel.add(yearReceiveSortButton);

		nameSortButton.setFont(font);
		typeSortButton.setFont(font);
		volumeSortButton.setFont(font);
		alcoSortButton.setFont(font);
		countrySortButton.setFont(font);
		yearBottlingSortButton.setFont(font);
		yearReceiveSortButton.setFont(font);

		nameSortButton.addActionListener(e -> sortBy(Field.NAME));
		typeSortButton.addActionListener(e -> sortBy(Field.TYPE));
		volumeSortButton.addActionListener(e -> sortBy(Field.VOLUME));
		alcoSortButton.addActionListener(e -> sortBy(Field.ALCO));
		countrySortButton.addActionListener(e -> sortBy(Field.COUNTRY));
		yearBottlingSortButton.addActionListener(e -> sortBy(Field.YEAR_BOTTLING));
		yearReceiveSortButton.addActionListener(e -> sortBy(Field.YEAR_RECEIVE));
	}

	private void showByPanelSetUp(JPanel showByPanel) {
		JButton typeShowButton = new JButton("Типы"),
				volumeShowButton = new JButton("Объёмы"),
				alcoShowButton = new JButton("Содержание алкоголя"),
				countryShowButton = new JButton("Страны происхождения"),
				yearBottlingShowButton = new JButton("Годы розлива"),
				yearReceiveShowButton = new JButton("Годы поступления"),
				duplicatesShowButton = new JButton("Дубликаты");

		showByPanel.add(typeShowButton);
		showByPanel.add(volumeShowButton);
		showByPanel.add(alcoShowButton);
		showByPanel.add(countryShowButton);
		showByPanel.add(yearBottlingShowButton);
		showByPanel.add(yearReceiveShowButton);
		showByPanel.add(duplicatesShowButton);

		typeShowButton.setFont(font);
		volumeShowButton.setFont(font);
		alcoShowButton.setFont(font);
		countryShowButton.setFont(font);
		yearBottlingShowButton.setFont(font);
		yearReceiveShowButton.setFont(font);
		duplicatesShowButton.setFont(font);

		typeShowButton.addActionListener(e -> showRightList(Field.TYPE));
		volumeShowButton.addActionListener(e -> showRightList(Field.VOLUME));
		alcoShowButton.addActionListener(e -> showRightList(Field.ALCO));
		countryShowButton.addActionListener(e -> showRightList(Field.COUNTRY));
		yearBottlingShowButton.addActionListener(e -> showRightList(Field.YEAR_BOTTLING));
		yearReceiveShowButton.addActionListener(e -> showRightList(Field.YEAR_RECEIVE));
		duplicatesShowButton.addActionListener(e -> showDuplicates());
	}

	private void backgroundSetUp() {
		JPanel background = new JPanel();
		frame.add(BorderLayout.CENTER,background);
		background.setLayout(null);

		bottlesListSetUp(background);

		JPanel bigInfoPanel = new JPanel(new GridLayout(2,1));
		bigInfoPanelSetUp(bigInfoPanel);

		showByScrollPane = new JScrollPane();
		showByScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		showByScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		showByScrollPane.setVisible(false);

		rightCardPanel = new JPanel(card);
		rightCardPanel.setBounds(scrWidth / 2 + 5, 5, scrWidth / 2 - 10 , scrHeight - 10 -
				bottomPanel.getPreferredSize().height);
		rightCardPanel.add(bigInfoPanel, INFO);
		rightCardPanel.add(showByScrollPane, LIST);
		background.add(rightCardPanel);
	}


	private void setBottleList(BottleEntry[] entries) {
		for (BottleEntry b : entries) listModel.addElement(b);
	}

	private void bottlesListSetUp(JPanel background) {
		bottlesList = new JList<>();
		bottlesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bottlesList.addListSelectionListener(this::showChosenBottle);

		listModel = new DefaultListModel<>();
		bottlesList.setModel(listModel);
		setBottleList(bottles.getBottleArray());


		JScrollPane scrollPane = new JScrollPane(bottlesList);
		scrollPane.setBounds(5,5,scrWidth / 2 - 10, scrHeight - 10 -
				bottomPanel.getPreferredSize().height);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		background.add(scrollPane);
	}

	private void bigInfoPanelSetUp(JPanel bigInfoPanel) {
		imagePanel = new ImagePanel();
		bigInfoPanel.add(imagePanel);

		JPanel infoPanel = new JPanel(new GridLayout(2,1));
		infoPanelSetUp(infoPanel);

		bigInfoPanel.add(infoPanel);
	}

	private void infoPanelSetUp(JPanel infoPanel){
		JPanel paramsPanel = new JPanel(new BorderLayout());
		infoPanel.add(paramsPanel);

		JPanel paramsLabelPanel = new JPanel(new GridLayout(8, 1));
		paramsPanel.add(BorderLayout.WEST, paramsLabelPanel);

		paramsLabelSetUp(paramsLabelPanel);

		JPanel paramsValuePanel = new JPanel(new GridLayout(8, 1, 0, 1));
		paramsPanel.add(BorderLayout.CENTER, paramsValuePanel);

		paramsValueSetUp(paramsValuePanel);

		JPanel CBPanel = new JPanel(new BorderLayout());
		infoPanel.add(CBPanel);

		CBPanelSetUp(CBPanel);
	}

	private void paramsLabelSetUp(JPanel paramsLabelPanel) {
		JLabel nameLabel = new JLabel("Название:");
		JLabel typeLabel = new JLabel("Тип:");
		JLabel volumeLabel = new JLabel("Объём:");
		JLabel alcoLabel = new JLabel("Содержание алкоголя:");
		JLabel countryLabel = new JLabel("Страна происхождения:");
		JLabel yearBottlingLabel = new JLabel("Год розлива:");
		JLabel yearReceiveLabel = new JLabel("Год поступления:");
		JLabel quantityLabel = new JLabel("Количество:");

		paramsLabelPanel.add(nameLabel);
		paramsLabelPanel.add(typeLabel);
		paramsLabelPanel.add(volumeLabel);
		paramsLabelPanel.add(alcoLabel);
		paramsLabelPanel.add(countryLabel);
		paramsLabelPanel.add(yearBottlingLabel);
		paramsLabelPanel.add(yearReceiveLabel);
		paramsLabelPanel.add(quantityLabel);
	}

	private void paramsValueSetUp(JPanel paramsValuePanel) {
		nameField = new JTextField();
		nameField.setEditable(false);
		paramsValuePanel.add(nameField);

		typeField = new JTextField();
		typeField.setEditable(false);
		paramsValuePanel.add(typeField);

		volumeField = new JTextField();
		volumeField.setEditable(false);
		paramsValuePanel.add(volumeField);

		alcoField = new JTextField();
		alcoField.setEditable(false);
		paramsValuePanel.add(alcoField);

		countryField = new JTextField();
		countryField.setEditable(false);
		paramsValuePanel.add(countryField);

		yearBottlingField = new JTextField();
		yearBottlingField.setEditable(false);
		paramsValuePanel.add(yearBottlingField);

		yearReceiveField = new JTextField();
		yearReceiveField.setEditable(false);
		paramsValuePanel.add(yearReceiveField);

		quantityField = new JTextField();
		quantityField.setEditable(false);
		paramsValuePanel.add(quantityField);
	}

	private void CBPanelSetUp(JPanel CBPanel) {
		JLabel commentLabel = new JLabel("Комментарий:");
		CBPanel.add(BorderLayout.NORTH, commentLabel);

		commentArea = new JTextArea();
		commentArea.setEditable(false);
		commentArea.setLineWrap(true);
		commentArea.setWrapStyleWord(true);
		CBPanel.add(BorderLayout.CENTER, commentArea);

		JPanel editPanel = new JPanel(new GridLayout(1,2));
		CBPanel.add(BorderLayout.SOUTH, editPanel);

		JButton editButton = new JButton("Изменить");
		editPanel.add(editButton);
		editButton.addActionListener(e -> editEntry());

		JButton deleteButton = new JButton("Удалить");
		editPanel.add(deleteButton);
		deleteButton.addActionListener(e -> deleteChosen());
	}

	private void addNewEntry() {
		EditFrame editFrame = new EditFrame(bottles);
		editFrame.startUp();
		frame.dispose();
	}

	private void showSortPanel() {
		if (sortByButton.isSelected()) {
			card.show(bottomCardPanel, SORT);
			if (showByButton.isSelected()) {
				showByButton.setSelected(false);
			}
		} else {
			card.show(bottomCardPanel, SEARCH);
		}
	}

	///////////////////////////////////////////////убрать скрытие панельки
	private void sortBy(Field field) {
		sortByButton.setSelected(false);
		card.show(bottomCardPanel, SEARCH);
		if (field == sortedBy) return;
		sortedBy = field;
		BottleEntry[] result = bottles.getBottleArray();
		switch (field) {
			case TYPE:
				Arrays.sort(result, (o1, o2) -> o1.getType().compareToIgnoreCase(o2.getType()));
				break;
			case VOLUME:
				Arrays.sort(result, Comparator.comparingDouble(BottleEntry::getVolume));
				break;
			case ALCO:
				Arrays.sort(result, Comparator.comparingInt(BottleEntry::getAlco));
				break;
			case COUNTRY:
				Arrays.sort(result, (o1, o2) -> o1.getCountry().compareToIgnoreCase(o2.getCountry()));
				break;
			case YEAR_BOTTLING:
				Arrays.sort(result, Comparator.comparingInt(BottleEntry::getYearBottling));
				break;
			case YEAR_RECEIVE:
				Arrays.sort(result, Comparator.comparingInt(BottleEntry::getYearReceive));
		}
		bottlesList.setListData(result);
	}

	///////////////////////////////////////////////добавить скрытие правого списка
	private void showParameters() {
		if (showByButton.isSelected()) {
			card.show(bottomCardPanel, SHOW);
			if (sortByButton.isSelected()) {
				sortByButton.setSelected(false);
			}
		} else {
			card.show(bottomCardPanel, SEARCH);
			card.show(rightCardPanel, INFO);
		}
	}

	private void showRightList(Field field) {
		showedBy = field;
		switch (field) {
			case TYPE:
				showByList = new JList<String>();
				showByList.setListData(bottles.getTypes());
				break;
			case VOLUME:
				showByList = new JList<Double>();
				showByList.setListData(bottles.getVolumes());
				break;
			case ALCO:
				showByList = new JList<Integer>();
				showByList.setListData(bottles.getAlcos());
				break;
			case COUNTRY:
				showByList = new JList<String>();
				showByList.setListData(bottles.getCountries());
				break;
			case YEAR_BOTTLING:
				showByList = new JList<Integer>();
				showByList.setListData(bottles.getYearsBotting());
				break;
			case YEAR_RECEIVE:
				showByList = new JList<Integer>();
				showByList.setListData(bottles.getYearsReceive());
		}
		//////////////////////////////////////////////////////изменить работу showBy и проверить не нужно ли другого listenera добавлять
		showByList.addListSelectionListener(this::showBy);

		showByList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showByScrollPane.setViewportView(showByList);
		card.show(rightCardPanel, LIST);

	}

	////////////////////////////////////////////////добавить fullListButton.setEnabled(true);
	private void showBy(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			//bottlesList.setListData(bottles.getByType(showByList.getSelectedValue()));
		}
	}

	private void showDuplicates() {
		bottlesList.setListData(bottles.getDuplicates());
		fullListButton.setEnabled(true);
		clearAll();
	}

	////////////////////////////////////////// диалоговое окно и отслеживание изменений
	private void saveAndExit() {
		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("database.ser"))) {
			os.writeObject(bottles);
		} catch (Exception ex) { ex.printStackTrace(); }
		System.exit(0);
	}

	private void editEntry() {
		if (bottlesList.getSelectedValue() != null) {
			EditFrame editFrame = new EditFrame(bottles);
			//editFrame.editEntry(bottlesList.getSelectedValue());
			frame.dispose();
		}
	}

	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!убрать проверку на null и поменять на отрицательное значение???
	//////////////////////////////////////////////////////сделать при скрытии правой панельки обнуление поля showedBy
	private void showChosenBottle(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			BottleEntry chosen = bottlesList.getSelectedValue();
			if (chosen != null) {
				nameField.setText(chosen.getName());
				typeField.setText(chosen.getType());
				countryField.setText(chosen.getCountry());
				commentArea.setText(chosen.getComment());
				imagePanel.setImage(chosen.getImage(), imagePanel.getWidth(), imagePanel.getHeight());
				imagePanel.repaint();
				card.show(rightCardPanel, INFO);

				Double vol = chosen.getVolume();
				Integer alco = chosen.getAlco(),
						yearB = chosen.getYearBottling(),
						yearR = chosen.getYearReceive();
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!убрать проверку на null и поменять на отрицательное значение???
				if (vol == null) volumeField.setText("");
				else volumeField.setText(Double.toString(chosen.getVolume()));

				if (alco == null) alcoField.setText("");
				else alcoField.setText(Integer.toString(chosen.getAlco()) + "°");

				if (yearB == null) yearBottlingField.setText("");
				else yearBottlingField.setText(Integer.toString(chosen.getYearBottling()));

				if (yearR == null) yearReceiveField.setText("");
				else yearReceiveField.setText(Integer.toString(chosen.getYearReceive()));

				quantityField.setText(Integer.toString(chosen.getQuantity()));
			}
		}
	}

	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! изменить


	/////////////////////////////////////////////////////////////////////////переделать с использованием listModel
	private void deleteChosen() {
		bottles.deleteBottle(bottlesList.getSelectedValue());
		bottlesList.setListData(bottles.getBottleArray());
		clearAll();
		showCount();
	}

	private void showCount() {
		countButton.setText("Количество: " + bottles.getCount());
	}

	/////////////////////////////////////дополнить очищение всех новых полей
	private void clearAll() {
		imagePanel.setNullImage();
		nameField.setText("");
		typeField.setText("");
		commentArea.setText("");
		bottlesList.clearSelection();
		imagePanel.repaint();
	}

	private void showFullList() {
		/*if (fullListMark != 0) {
			fullListMark = 0;
			bottlesList.setListData(bottles.getBottleArray());
			if (typesButtonMark != 0) {
				//hideTypes();
			}
		}*/
	}

	private void search() {
		/*String searchText = searchField.getText();
		if (searchText.length() == 0 || searchText.equals(NOT_FOUND_TEXT)) {
			searchField.setText(ENTER_TEXT);
		} else if (!searchText.equals(ENTER_TEXT)) {
			BottleEntry[] searchResult = bottles.searchEntries(searchText);
			if (searchResult.length == 0) {
				searchField.setText(NOT_FOUND_TEXT);
			} else {
				bottlesList.setListData(searchResult);
				++fullListMark;
				searchField.setText(ENTER_TEXT);
			}
		}*/
	}

	private void autoEmptySearch(MouseEvent event) {
		if (searchField.getText().equals(ENTER_TEXT) || searchField.getText().equals(NOT_FOUND_TEXT)) {
			searchField.setText("");
		}
	}
}