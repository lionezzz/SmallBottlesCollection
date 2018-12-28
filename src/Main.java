import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class Main {
	public static void main(String[] args) {
		File db = new File("database.ser");
		BottleList list = null;
		if (db.exists()) {
			try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(db))){
				list = (BottleList) inputStream.readObject();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			list = new BottleList();
		}
		new MainFrame(list);
	}
}
