import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

public class BottleList implements Serializable {
	private ArrayList <BottleEntry> list;
	private TreeSet <String> types;
	private int sortingMark;

	public BottleList() {
		types = new TreeSet<>();
		list = new ArrayList<>();
		sortingMark = -1;
	}

	public int getCount() { return list.size(); }

	public void addBottle(BottleEntry entry) {
		list.add(entry);
		String newType = entry.getType();
		if (!types.contains(newType)) {
			types.add(newType.toLowerCase());
		}
	}

	public int getSortingMark() { return sortingMark; }

	public void setUnsorted() {	sortingMark = -1; }

	public BottleEntry[] getBottleArray() { return list.toArray(new BottleEntry[list.size()]); }

	public void sortByName() {
		list.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
		sortingMark = 0;
	}

	public void sortByType() {
		list.sort((o1, o2) -> o1.getType().compareToIgnoreCase(o2.getType()));
		sortingMark = 1;
	}

	public String[] getTypes() { return types.toArray(new String[types.size()]); }

	public BottleEntry[] getByType(String type) {
		return list.stream().filter(o -> o.getType().equalsIgnoreCase(type)).toArray(BottleEntry[]::new);
	}

	public void deleteBottle(BottleEntry bottle) { list.remove(bottle); }

	public BottleEntry[] searchEntries(String string) {
		return list.stream().filter(o -> o.getName().toLowerCase().contains(string.toLowerCase())).toArray(BottleEntry[]::new);
	}
}
