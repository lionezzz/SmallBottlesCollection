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

	public void addBottle(BottleEntry entry) {
		if (isNotEmptyEntry(entry)) {
			list.add(entry);

			String newType = entry.getType(), newCountry = entry.getCountry();
			Double newVolume = entry.getVolume();
			Integer newAlco = entry.getAlco(),
					newYB = entry.getYearBottling(),
					newYR = entry.getYearReceive();

			if (!newType.isEmpty() && !types.contains(newType)) types.add(newType.toLowerCase());
			if (!newCountry.isEmpty() && !countries.contains(newCountry)) countries.add(newCountry.substring(0, 1).toUpperCase() +
					newCountry.substring(1).toLowerCase());
			if (newVolume != null && newVolume >= 0) volumes.add(newVolume);
			if (newAlco != null && newAlco >= 0) alcos.add(newAlco);
			if (newYB != null && newYB >= 0) yearsBottling.add(newYB);
			if (newYR != null && newYR >= 0) yearsReceive.add(newYR);
		}
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
		return list.stream().filter(e -> e.getType().equalsIgnoreCase(type)).toArray(BottleEntry[]::new);
	}

	public BottleEntry[] getByVolume(Double vol) {
		return list.stream().filter(e -> e.getVolume().equals(vol)).toArray(BottleEntry[]::new);
	}

	public BottleEntry[] getByAlco(Integer alco) {
		return list.stream().filter(e -> e.getAlco().equals(alco)).toArray(BottleEntry[]::new);
	}

	public BottleEntry[] getByCountry(String country) {
		return list.stream().filter(e -> e.getCountry().equalsIgnoreCase(country)).toArray(BottleEntry[]::new);
	}

	public BottleEntry[] getByYearBottling(Integer yB) {
		return list.stream().filter(e -> e.getYearBottling().equals(yB)).toArray(BottleEntry[]::new);
	}

	public BottleEntry[] getByYearReceive(Integer yR) {
		return list.stream().filter(e -> e.getYearReceive().equals(yR)).toArray(BottleEntry[]::new);
	}

	public void deleteBottle(BottleEntry bottle) {
		String oldType = bottle.getType(),
				oldCountry = bottle.getCountry();
		Double oldVolume = bottle.getVolume();
		Integer oldAlco = bottle.getAlco(),
				oldYB = bottle.getYearBottling(),
				oldYR = bottle.getYearReceive();

		list.remove(bottle);

		if (list.stream().noneMatch(e -> e.getType().equalsIgnoreCase(oldType))) types.remove(oldType);
		if (list.stream().noneMatch(e -> e.getCountry().equalsIgnoreCase(oldCountry))) countries.remove(oldCountry);
		if (list.stream().noneMatch(e -> e.getVolume().equals(oldVolume))) volumes.remove(oldVolume);
		if (list.stream().noneMatch(e -> e.getAlco().equals(oldAlco))) alcos.remove(oldAlco);
		if (list.stream().noneMatch(e -> e.getYearBottling().equals(oldYB))) yearsBottling.remove(oldYB);
		if (list.stream().noneMatch(e -> e.getYearReceive().equals(oldYR))) yearsReceive.remove(oldYR);
	}

	public BottleEntry[] searchEntries(String string) {
		return list.stream().filter(o -> o.toString().toLowerCase().contains(string.toLowerCase())).toArray(BottleEntry[]::new);
	}

	private boolean isNotEmptyEntry(BottleEntry e) {
		return !(e.getName().equals("") && e.getType().equals("") && e.getCountry().equals("") && e.getVolume() < 0
				&& e.getYearReceive() < 0 && e.getYearBottling() < 0 && e.getAlco() < 0);
	}
}
