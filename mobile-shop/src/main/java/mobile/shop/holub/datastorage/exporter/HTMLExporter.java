package mobile.shop.holub.datastorage.exporter;

import mobile.shop.holub.datastorage.table.Table;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class HTMLExporter implements Table.Exporter{
    private final Writer out;
    private int width;

    public HTMLExporter( Writer out ) {
        this.out = out;
    }

    @Override
    public void startTable() throws IOException {
        out.write("<!DOCTYPE html>\n");
        out.write("<html>\n");
        out.write("<head>\n");
        out.write("</head>\n");
        out.write("<body>\n");
        out.write("\t<table>\n");
    }

    @Override
    public void storeMetadata(String tableName, int width, int height, Iterator columnNames) throws IOException {
        this.width = width;
        out.write("\t\t<caption>");
        out.write(tableName == null ? "<anonymous>" : tableName);
        out.write("</caption>\n");
        out.write("\t\t<thead>\n");
        out.write("\t\t\t<tr>\n");
        while (columnNames.hasNext()) {
            out.write("\t\t\t\t<th>");
            Object datum = columnNames.next();
            if (datum != null) {
                out.write(datum.toString());
            }
            out.write("</th>\n");
        }
        out.write("\t\t\t</tr>\n");
        out.write("\t\t</thead>\n");
    }

    @Override
    public void storeRow(Iterator data) throws IOException {
        out.write("\t\t<tbody>\n");
        out.write("\t\t\t<tr>\n");
        while (data.hasNext()){
            out.write("\t\t\t\t<td>");
            Object datum = data.next();
            if (datum != null){
                out.write(datum.toString());
            }
            out.write("</td>\n");
        }
        out.write("\t\t\t</tr>\n");
        out.write("\t\t</tbody>\n");
    }

    @Override
    public void endTable() throws IOException {
        out.write("\t</table>\n");
        out.write("</body>\n");
        out.write("</html>");
    }
}
