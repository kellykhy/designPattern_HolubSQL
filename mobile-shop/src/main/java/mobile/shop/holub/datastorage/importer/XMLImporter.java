package mobile.shop.holub.datastorage.importer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import mobile.shop.holub.datastorage.table.Table;
import mobile.shop.holub.tools.ArrayIterator;

public class XMLImporter implements Table.Importer {
    private XMLStreamReader reader;
    private String tableName;
    private List<String> columnNames;

    public XMLImporter(Reader in) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        reader = factory.createXMLStreamReader(in);
    }

    public void startTable() throws IOException {
        try {
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    tableName = reader.getLocalName().trim();
                    break;
                }
            }

            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    if ("columns".equals(reader.getLocalName())) {
                        columnNames = new ArrayList<>();
                        while (reader.hasNext()) {
                            int columnEvent = reader.next();
                            if (columnEvent == XMLStreamConstants.START_ELEMENT) {
                                columnNames.add(reader.getLocalName());
                            } else if (columnEvent == XMLStreamConstants.END_ELEMENT
                                    && "columns".equals(reader.getLocalName())) {
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    public String loadTableName() throws IOException {
        return tableName;
    }

    public int loadWidth() throws IOException {
        return columnNames.size();
    }

    public Iterator loadColumnNames() throws IOException {
        return new ArrayIterator(columnNames.toArray(new String[0]));
    }

    public Iterator loadRow() throws IOException {
        Iterator row = null;
        try {
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT && "row".equals(reader.getLocalName())) {
                    List<String> rowData = new ArrayList<>();
                    while (reader.hasNext()) {
                        int columnEvent = reader.next();
                        if (columnEvent == XMLStreamConstants.START_ELEMENT) {
                            rowData.add(reader.getElementText());
                        } else if (columnEvent == XMLStreamConstants.END_ELEMENT
                                && "row".equals(reader.getLocalName())) {
                            row = new ArrayIterator(rowData.toArray(new String[0]));
                            break;
                        }
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
        return row;
    }

    public void endTable() throws IOException {
        try {
            reader.close();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }
}