import java.io.Serializable;

public class BottleEntry  implements Serializable {
	private String name;
	private String type;
	private String comment;
	private String image;

	public BottleEntry(String n, String t, String c, String p) {
		name = n;
		type = t.toLowerCase();
		comment = c;
		image = p;
	}

	public String getImage() { return image; }

	public String getName() { return name; }

	public String getType() { return type; }

	public String getComment() { return comment; }

	public String toString() { return name + " (" + type + ")";	}

	public boolean equals(BottleEntry bottle) {
		return (bottle != null && name.equals(bottle.getName()) && type.equals(bottle.getType())
				&& comment.equals(bottle.getComment()) && image.equals(bottle.getImage()));
	}
}
