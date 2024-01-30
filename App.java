import javax.swing.*;

import assignment2.database.Database;
import assignment2.views.*;

public class App {
    public static void main(String[] args) {
        Database db = new Database();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookStoreGUI(db);
            }
        });
    }
}
