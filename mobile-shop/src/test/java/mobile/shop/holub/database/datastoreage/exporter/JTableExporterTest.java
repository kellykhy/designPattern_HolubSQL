package mobile.shop.holub.database.datastoreage.exporter;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import mobile.shop.holub.datastorage.exporter.JTableExporter;
import mobile.shop.holub.datastorage.table.Table;
import mobile.shop.holub.datastorage.table.TableFactory;

class JTableExporterTest {
    public static void main(String[] args) throws IOException {
        Table people = TableFactory.create("resources/people",
                new String[]{"First", "Last"});
        people.insert(new String[]{"Allen", "Holub"});
        people.insert(new String[]{"Ichabod", "Crane"});
        people.insert(new String[]{"Rip", "VanWinkle"});
        people.insert(new String[]{"Goldie", "Locks"});

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTableExporter tableBuilder = new JTableExporter();
        people.export(tableBuilder);

        frame.getContentPane().add(
                new JScrollPane(tableBuilder.getJTable()));
        frame.pack();
        frame.setVisible(true);
    }

}