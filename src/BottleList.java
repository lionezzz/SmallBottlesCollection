import java.util.ArrayList;
import java.util.TreeSet;

public class BottleList {
	private ArrayList <BottleEntry> list;
	private TreeSet <String> types;

	public BottleList() {
		types = new TreeSet<>();
		list = new ArrayList<>();
	}

	public int getLength() { return list.size(); }

	public void addBottle(BottleEntry entry) {
		list.add(entry);
		if (!types.contains(entry.getType())) {
			types.add(entry.getType());
		}
	}
}