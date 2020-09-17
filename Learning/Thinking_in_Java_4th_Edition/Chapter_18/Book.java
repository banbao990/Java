/**
 * @author banbao
 * @comment 修改自示例代码
 * 依赖库: nu.xom.*(http://www.xom.nu)
 */
import nu.xom.*;
import java.util.*;
import java.io.*;

import java.util.Arrays;

public class Book {
    private String name, author;
    // constructor1
    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }
    // constructor2
    public Book(Element book) {
        this.name = book.getFirstChildElement("name").getValue();
        this.author = book.getFirstChildElement("author").getValue();
    }
    @Override
    public String toString() {
        return "[ " + this.name + " " + this.author + " ]";
    }
    // produce XML
    public Element getXML() {
        Element book = new Element("book");
        Element ele1 = new Element("name");
        ele1.appendChild(name);
        Element ele2 = new Element("author");
        ele2.appendChild(author);
        book.appendChild(ele1);
        book.appendChild(ele2);
        return book;
    }
    // os
    public static void format(OutputStream os, Document doc) 
        throws Exception {
        Serializer serializer= new Serializer(os, "UTF-8");
        serializer.setIndent(4);
        serializer.setMaxLength(60);
        serializer.write(doc);
        serializer.flush();
    }
    public static void main(String...args) throws Exception {
        List<Book> shelf = Arrays.asList(
            new Book("Effective Java", "Joshua Bloch"),
            new Book("Thinking in Java", "Bruce Eckel"),
            new Book("Head First  Java", "Kathy Sierra")
        );
        Element root = new Element("shelf");
        for(Book book : shelf) {
            root.appendChild(book.getXML());
        }
        Document doc = new Document(root);
        format(System.out, doc);
        // format(new BufferedOutputStream(
                // new FileOutputStream("shelf.xml")), doc);
    }
}


/* Output
<?xml version="1.0" encoding="UTF-8"?>
<shelf>
    <book>
        <name>Effective Java</name>
        <author>Joshua Bloch</author>
    </book>
    <book>
        <name>Thinking in Java</name>
        <author>Bruce Eckel</author>
    </book>
    <book>
        <name>Head First Java</name>
        <author>Kathy Sierra</author>
    </book>
</shelf>
*/
