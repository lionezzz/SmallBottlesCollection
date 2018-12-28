import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.stream.Stream;

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
	private boolean edited;

	public MainFrame(BottleList list) {
		bottles = list;
		sortedBy = Field.NAME;
		font = new Font("TimesRoman", Font.PLAIN, 20);
		Rectangle dim = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		scrWidth = dim.width;
		scrHeight = dim.height;
		card = new CardLayout();
		startUp();
	}

	///////////////////////////////////////////////////////добавить в EditFrame
	public MainFrame(BottleList list, boolean edit) {
		this(list);
		edited = edit;
	}

	public static void main(String[] args) {
		BottleList f = new BottleList();
		f.addBottle(new BottleEntry("name1", "type1", 1.0, 40, "RU", 2001, 2002, 1,
				"comm1", ""));
		f.addBottle(new BottleEntry("name2", "type3", 5.0, 1, "SW", 2004, 2001, 2,
				"comm1", ""));
		f.addBottle(new BottleEntry("name3", "type2", 4.0, 40, "GE", 1111, 2005, 1,
				"comm1", ""));
		f.addBottle(new BottleEntry("name4", "type1", 1.0, 45, "RU", 2004, 2002, 2,
				"comm1", ""));
		f.addBottle(new BottleEntry("name5", "type2", 2.0, 40, "EN", 2001, 2005, 1,
				"comm1", ""));
		f.addBottle(new BottleEntry("NAME", "type3", 4.0, 45, "GE", 200, 2002, 2,
				"comm1", ""));
		f.addBottle(new BottleEntry("", "", null, null, "", null, null, null,"", ""));
		new MainFrame(f);
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
		countButton = new JButton();
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

		exitButton.setFont(font);
		buttonPanel.add(exitButton);
		exitButton.addActionListener(e -> saveAndExit());
	}

	private void searchPanelSetUp(JPanel searchPanel) {
		searchField = new JTextField("Введите текст запроса");
		searchField.setFont(font);
		searchPanel.add(searchField);
		searchField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) { autoEmptySearch(); }
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

		rightCardPanel = new JPanel(card);
		background.add(rightCardPanel);
		rightCardPanel.setBounds(scrWidth / 2 + 5, 5, scrWidth / 2 - 10 , scrHeight - 10 -
				bottomPanel.getPreferredSize().height);

		JPanel bigInfoPanel = new JPanel(new GridLayout(2,1));
		bigInfoPanelSetUp(bigInfoPanel);

		rightCardPanel.add(bigInfoPanel, INFO);
		card.show(rightCardPanel, INFO);

		showByScrollPane = new JScrollPane();
		showByScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		showByScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		showByScrollPane.setVisible(false);

		rightCardPanel.add(showByScrollPane, LIST);
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

	private void setBottleList(BottleEntry[] entries) {
		listModel.clear();
		for (BottleEntry b : entries) listModel.addElement(b);
		showCount();
	}

	private void showCount() {
		countButton.setText("Количество: " + listModel.size());
	}

	private void addNewEntry() {
		new EditFrame(bottles);
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

	private void sortBy(Field field) {
		if (field == sortedBy) return;
		sortedBy = field;
		BottleEntry[] result = bottles.getBottleArray();
		switch (field) {
			case NAME:
				result = Stream.concat(
						Stream.of(result).filter(e -> !e.getName().equals("")).sorted(Comparator.comparing(BottleEntry::getName)),
						Stream.of(result).filter(e -> e.getName().equals(""))).toArray(BottleEntry[]::new);
				break;
			case TYPE:
				result = Stream.concat(
						Stream.of(result).filter(e -> !e.getType().equals("")).sorted(Comparator.comparing(BottleEntry::getType)),
						Stream.of(result).filter(e -> e.getType().equals(""))).toArray(BottleEntry[]::new);
				break;
			case VOLUME:
				result = Stream.concat(
						Stream.of(result).filter(e -> e.getVolume() >= 0).sorted(Comparator.comparing(BottleEntry::getVolume)),
						Stream.of(result).filter(e -> e.getVolume() < 0)).toArray(BottleEntry[]::new);
				break;
			case ALCO:
				result = Stream.concat(
						Stream.of(result).filter(e -> e.getAlco() >= 0).sorted(Comparator.comparing(BottleEntry::getAlco)),
						Stream.of(result).filter(e -> e.getAlco() < 0)).toArray(BottleEntry[]::new);
				break;
			case COUNTRY:
				result = Stream.concat(
						Stream.of(result).filter(e -> !e.getCountry().equals("")).sorted(Comparator.comparing(BottleEntry::getCountry)),
						Stream.of(result).filter(e -> e.getCountry().equals(""))).toArray(BottleEntry[]::new);
				break;
			case YEAR_BOTTLING:
				result = Stream.concat(
						Stream.of(result).filter(e -> e.getYearBottling() >= 0).sorted(Comparator.comparing(BottleEntry::getYearBottling)),
						Stream.of(result).filter(e -> e.getYearBottling() < 0)).toArray(BottleEntry[]::new);
				break;
			case YEAR_RECEIVE:
				result = Stream.concat(
						Stream.of(result).filter(e -> e.getYearReceive() >= 0).sorted(Comparator.comparing(BottleEntry::getYearBottling)),
						Stream.of(result).filter(e -> e.getYearReceive() < 0)).toArray(BottleEntry[]::new);
		}
		setBottleList(result);
	}

	private void showParameters() {
		if (showByButton.isSelected()) {
			card.show(bottomCardPanel, SHOW);
			if (sortByButton.isSelected()) {
				sortByButton.setSelected(false);
			}
		} else {
			card.show(bottomCardPanel, SEARCH);
			card.show(rightCardPanel, INFO);
			showedBy = null;
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
		showByList.addListSelectionListener(this::showBy);
		showByList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showByScrollPane.setViewportView(showByList);
		card.show(rightCardPanel, LIST);
	}

	private void showBy(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			BottleEntry[] result = new BottleEntry[0];
			Object selected = showByList.getSelectedValue();
			switch (showedBy) {
				case TYPE:
					result = bottles.getByType((String) selected);
					break;
				case VOLUME:
					result = bottles.getByVolume((Double) selected);
					break;
				case ALCO:
					result = bottles.getByAlco((Integer) selected);
					break;
				case COUNTRY:
					result = bottles.getByCountry((String) selected);
					break;
				case YEAR_BOTTLING:
					result = bottles.getByYearBottling((Integer) selected);
					break;
				case YEAR_RECEIVE:
					result = bottles.getByYearReceive((Integer) selected);
					break;
			}
			setBottleList(result);
			fullListButton.setEnabled(true);
		}
	}

	private void showDuplicates() {
		card.show(rightCardPanel, INFO);
		setBottleList(bottles.getDuplicates());
		fullListButton.setEnabled(true);
		clearAll();
	}

	private void clearAll() {
		nameField.setText("");
		typeField.setText("");
		volumeField.setText("");
		alcoField.setText("");
		countryField.setText("");
		yearBottlingField.setText("");
		yearReceiveField.setText("");
		quantityField.setText("");
		commentArea.setText("");

		bottlesList.clearSelection();

		imagePanel.setNullImage();
		imagePanel.repaint();
	}

	private void showFullList() {
		fullListButton.setEnabled(false);
		showByButton.setSelected(false);
		setBottleList(bottles.getBottleArray());
	}

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

				Double vol = chosen.getVolume();
				Integer alco = chosen.getAlco(),
						yearB = chosen.getYearBottling(),
						yearR = chosen.getYearReceive(),
						quantity = chosen.getQuantity();

				volumeField.setText(vol < 0 ? "" : Double.toString(vol));
				alcoField.setText(alco < 0 ? "" : (Integer.toString(alco) + "°"));
				yearBottlingField.setText(yearB < 0 ? "" : Integer.toString(yearB));
				yearReceiveField.setText(yearR < 0 ? "" : Integer.toString(yearR));
				quantityField.setText(quantity < 0 ? "" : Integer.toString(quantity));

				card.show(bottomCardPanel, SEARCH);
				showByButton.setSelected(false);
				sortByButton.setSelected(false);
				card.show(rightCardPanel, INFO);
				if (showedBy != null) showedBy = null;
			}
		}
	}

	//////////////////////////////////////////////////////////почему выдаёт не yes\no\cancel
	//////////////////////////////////////////////////////////почему выходит, если нажать NO
	private void saveAndExit() {
		if (edited) {
			int dialogResult = JOptionPane.showConfirmDialog(frame, "Сохранить изменения?", "Подтвердите действие",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (dialogResult == JOptionPane.CANCEL_OPTION) { return; }
			else if (dialogResult == JOptionPane.YES_OPTION) {
				try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("database.ser"))) {
					os.writeObject(bottles);
				} catch (Exception ex) { ex.printStackTrace(); }
			}
		} else {
			int dialogResult = JOptionPane.showConfirmDialog(frame, "Выйти из программы?", "Подтвердите действие",
					JOptionPane.YES_NO_OPTION);
			if (dialogResult == JOptionPane.NO_OPTION) { return; }
		}
		System.exit(0);
	}

	private void editEntry() {
		BottleEntry selected = bottlesList.getSelectedValue();
		if (selected != null) {
			new EditFrame(bottles, selected);
			frame.dispose();
		}
	}

	private void deleteChosen() {
		if (!bottlesList.getValueIsAdjusting()) {
			BottleEntry del = bottlesList.getSelectedValue();
			listModel.removeElement(del);
			bottles.deleteBottle(del);
			clearAll();
			showCount();
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
				setBottleList(searchResult);
				searchField.setText("Найдено: " + searchResult.length);
				fullListButton.setEnabled(true);
			}
		}
	}

	private void autoEmptySearch() {
		String searchText = searchField.getText();
		if (searchText.equals(ENTER_TEXT) || searchText.equals(NOT_FOUND_TEXT) || searchText.contains("Найдено")) {
			searchField.setText("");
		}
	}
}