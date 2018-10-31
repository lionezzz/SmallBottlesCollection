import java.io.Serializable;

public class BottleEntry  implements Serializable {
	private String name;
	private String type;
	private String comment;
	private String image;

	public BottleEntry(String n, String t, String c, String p) {
		name = n;
		type = t;
		comment = c;
		image = p;
	}

	public String getImage() { return image; }

	public String getName() { return name; }

	public String getType() { return type; }

	public String getComment() { return comment; }

	public String toString() {
		return name;
	}

}
