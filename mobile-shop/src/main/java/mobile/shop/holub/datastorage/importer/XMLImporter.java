/*  (c) 2004 Allen I. Holub. All rights reserved.
 *
 *  This code may be used freely by yourself with the following
 *  restrictions:
 *
 *  o Your splash screen, about box, or equivalent, must include
 *    Allen Holub's name, copyright, and URL. For example:
 *
 *      This program contains Allen Holub's SQL package.<br>
 *      (c) 2005 Allen I. Holub. All Rights Reserved.<br>
 *              http://www.holub.com<br>
 *
 *    If your program does not run interactively, then the foregoing
 *    notice must appear in your documentation.
 *
 *  o You may not redistribute (or mirror) the source code.
 *
 *  o You must report any bugs that you find to me. Use the form at
 *    http://www.holub.com/company/contact.html or send email to
 *    allen@Holub.com.
 *
 *  o The software is supplied <em>as is</em>. Neither Allen Holub nor
 *    Holub Associates are responsible for any bugs (or any problems
 *    caused by bugs, including lost productivity or data)
 *    in any of this code.
 */
package mobile.shop.holub.datastorage.importer;

import mobile.shop.holub.tools.ArrayIterator;
import mobile.shop.holub.datastorage.table.Table;

import java.io.*;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class XMLImporter implements Table.Importer
{	
	private Reader in;
	private Element root;
	private int currentRow;

	public XMLImporter( Reader in )
	{	
		this.in = in;
        this.currentRow = -1;
	}
	public void startTable()			throws IOException
	{	
		try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(in));
            this.root = doc.getDocumentElement();
        } catch(Exception e) {

        }
	}
	public String loadTableName()		throws IOException
	{	
		return root.getTagName();
	}
	public int loadWidth()			    throws IOException
	{	
		int i = 0;
        Iterator columnNames = loadColumnNames();
        while(columnNames.hasNext()) {
            i++;
            columnNames.next();
        }
        return i;
	}
	public Iterator loadColumnNames()	throws IOException
	{	
		ArrayList columns = new ArrayList<String>();
        for (int i = 0; i < root.getChildNodes().item(1).getChildNodes().getLength(); i++) {
            Node node = root.getChildNodes().item(1).getChildNodes().item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                String tagName = childElement.getTagName();
                if(!tagName.trim().equals(""))
                    columns.add(tagName);
            }
        }
        return new ArrayIterator(columns.toArray());
	}

	public Iterator loadRow()			throws IOException
	{	
		currentRow += 1;
        if (currentRow >= root.getChildNodes().getLength()-1) {
            return null;
        }

        Node rowNode = root.getChildNodes().item(currentRow);
        while (rowNode.getNodeType() == Node.TEXT_NODE) {
            currentRow += 1;
            rowNode = rowNode.getNextSibling();
            if (rowNode == null)
                return null;
        }

        ArrayList row = new ArrayList<String>();
        for(int i=0; i < rowNode.getChildNodes().getLength(); i++) {
            Node node = rowNode.getChildNodes().item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                String value = childElement.getTextContent();
                row.add(value);
            }
        }
        return new ArrayIterator(row.toArray());
	}

	public void endTable() throws IOException {}
}
