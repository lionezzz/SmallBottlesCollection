import java.io.Serializable;

public class BottleEntry  implements Serializable {
	private String name, type, comment, image, country;
	private Integer alco, quantity, yearBottling, yearReceive;
	private Double volume;

	public BottleEntry(String nm, String ty, Double vol, Integer al, String cnt, Integer yB, Integer yR,
	                   Integer qnt, String cm, String pic) {
		name = (nm != null) ? nm : "";
		type = (ty != null) ? ty.toLowerCase() : "";
		comment = (cm!= null) ? cm : "";
		image = (pic != null) ? pic : "";
		volume = (vol != null && vol >= 0) ? vol : -1;
		country = (cnt != null) ? cnt : "";
		alco = (al != null && al >= 0) ? al : -1;
		quantity = (qnt != null && qnt >= 0) ? qnt : -1;
		yearBottling = (yB != null && yB >= 0) ? yB : -1;
		yearReceive = (yR != null && yR >= 0) ? yR : -1;
	}

	public String getImage() { return image; }

	public String getName() { return name; }

	public String getType() { return type; }

	public String getComment() { return comment; }

	public Double getVolume() { return volume; }

	public String getCountry() { return country; }

	public Integer getAlco() { return alco; }

	public Integer getQuantity() { return quantity; }

	public Integer getYearBottling() { return yearBottling; }

	public Integer getYearReceive() { return yearReceive; }

	public String toString() {
		StringBuilder result = new StringBuilder(name + " (");
		if (!type.isEmpty()) result.append(type);
		if (volume >= 0) result.append(", ").append(volume);
		if (alco >= 0) result.append(", ").append(alco).append("°");
		if (!country.isEmpty()) result.append(", ").append(country);
		if (yearBottling >= 0) result.append(", розлив: ").append(yearBottling);
		if (yearReceive >= 0) result.append(" , поступление: ").append(yearReceive);
		result.append(")");
		if (quantity >= 0) result.append("- ").append(quantity).append("шт.");
		return result.toString().replace("(, ", "(").replace("()", "");
	}

	public boolean equals(BottleEntry b) {
		return (b != null && name.equals(b.getName()) && type.equals(b.getType())
				&& comment.equals(b.getComment()) && image.equals(b.getImage())
				&& volume.equals(b.getVolume()) && country.equals(b.getCountry())
				&& alco.equals(b.getAlco()) && quantity.equals(b.getQuantity())
				&& yearBottling.equals(b.getYearBottling()) && yearReceive.equals(b.getYearReceive()));
	}
}
