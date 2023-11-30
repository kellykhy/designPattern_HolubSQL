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
package mobile.shop.holub.datastorage.exporter;

import java.io.*;
import java.util.*;
import mobile.shop.holub.datastorage.table.Table;



public class XMLExporter implements Table.Exporter
{	private final Writer out;
	private int	 width;
	private String tableName;
	
	private String[] columnNames;

	public XMLExporter( Writer out )
	{	
		this.out = out;
	}

	public void startTable() throws IOException {out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");}
	public void storeMetadata( String tableName,
							   int width,
							   int height,
							   Iterator columnNames ) throws IOException

	{	
		this.width = width;
		this.tableName = tableName;
		out.write(tableName == null ? "<anonymous>" : "<" + tableName + ">");
		out.write("\n");
		this.columnNames = new String[width];
        for (int i = 0; columnNames.hasNext(); i++) {
            this.columnNames[i] = columnNames.next().toString();
        }
	}

	public void storeRow( Iterator data ) throws IOException
	{	
		int idx = 0;
        out.write("\t<row>\n");
        while (data.hasNext())
        {
            Object datum = data.next();
            if( datum != null ) {
                out.write("\t\t<" + columnNames[idx] + ">");
                out.write(datum.toString());
                out.write("</" + columnNames[idx] + ">");
            } else {
                out.write("\t\t<" + columnNames[idx] + ">");
                out.write("</" + columnNames[idx] + ">");
            }

            out.write("\n");
            if (idx < width) {
                idx += 1;
            }
        }
        out.write("\t</row>\n");
	}
	public void endTable()   throws IOException {out.write("</" + this.tableName + ">");}
}
