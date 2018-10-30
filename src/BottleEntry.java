public class BottleEntry  {
	private String name;
	private String type;
	private String comment;

	public BottleEntry(String n, String t, String c) {
		name = n;
		type = t;
		comment = c;
	}

	public String getName() { return name; }

	public String getType() { return type; }

	public String getComment() { return comment; }

}
