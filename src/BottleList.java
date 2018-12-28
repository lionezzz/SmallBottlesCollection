import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

public class BottleList implements Serializable {
	private ArrayList <BottleEntry> list;
	private TreeSet <String> types, countries;
	private TreeSet <Double> volumes;
	private TreeSet <Integer> alcos, yearsBottling, yearsReceive;

	public BottleList() {
		types = new TreeSet<>();
		countries = new TreeSet<>();
		volumes = new TreeSet<>();
		alcos = new TreeSet<>();
		yearsBottling = new TreeSet<>();
		yearsReceive = new TreeSet<>();
		list = new ArrayList<>();
	}

	public int getCount() { return list.size(); }

	///////////////////////////////////////////////сделать, чтобы пустые и отрицательные значения не добавлялись в списки
	public void addBottle(BottleEntry entry) {
		list.add(entry);

		String newType = entry.getType(), newCountry = entry.getCountry();
		Double newVolume = entry.getVolume();
		Integer newAlco = entry.getAlco(),
				newYB = entry.getYearBottling(),
				newYR = entry.getYearReceive();

		if (!newType.isEmpty() && !types.contains(newType)) types.add(newType.toLowerCase());
		if (!newCountry.isEmpty() && !countries.contains(newCountry)) countries.add(newCountry.substring(0, 1).toUpperCase() +
				newCountry.substring(1).toLowerCase());
		if (!(newVolume == null)) volumes.add(newVolume);
		if (!(newAlco == null)) alcos.add(newAlco);
		if (!(newYB == null)) yearsBottling.add(newYB);
		if (!(newYR == null)) yearsReceive.add(newYR);
	}

	public BottleEntry[] getBottleArray() { return list.toArray(new BottleEntry[0]); }

	public void sort() { list.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName())); }

	public String[] getTypes() { return types.toArray(new String[0]); }

	public String[] getCountries() { return countries.toArray(new String[0]); }

	public Double[] getVolumes() { return volumes.toArray(new Double[0]); }

	public Integer[] getAlcos() { return alcos.toArray(new Integer[0]);	}

	public Integer[] getYearsBotting() { return yearsBottling.toArray(new Integer[0]); }

	public Integer[] getYearsReceive() { return yearsReceive.toArray(new Integer[0]); }

	public BottleEntry[] getDuplicates() { return list.stream().filter(e -> e.getQuantity() > 1).toArray(BottleEntry[]::new); }

	public BottleEntry[] getByType(String type) {
		return list.stream().filter(o -> o.getType().equalsIgnoreCase(type)).toArray(BottleEntry[]::new);
	}

	public void deleteBottle(BottleEntry bottle) { list.remove(bottle); }

	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! изменить
	public BottleEntry[] searchEntries(String string) {
		return list.stream().filter(o -> o.getName().toLowerCase().contains(string.toLowerCase())).toArray(BottleEntry[]::new);
	}
}
