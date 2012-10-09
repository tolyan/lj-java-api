package ru.anglerhood.lj.client;

import org.apache.log4j.BasicConfigurator;
import org.sqlite.SQLiteJDBCLoader;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;
import ru.anglerhood.lj.client.render.HTMLRenderer;
import ru.anglerhood.lj.client.render.LJRenderer;
import ru.anglerhood.lj.client.sql.SQLiteReader;
import ru.anglerhood.lj.client.sql.SQLiteWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class EntryPoint {

    private static final String JOURNAL_NAME = "metapractice";

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        BasicConfigurator.configure();
        System.out.println(String.format("running in %s mode", SQLiteJDBCLoader.isNativeMode() ? "native" : "pure-java"));
//        Client client = new Client();
//        client.initWriter(JOURNAL_NAME , SQLiteWriter.class);
//        client.scrapJournal(JOURNAL_NAME);

        BlogEntryReader reader = new SQLiteReader(JOURNAL_NAME);
        BlogEntry entry = reader.readEntry(1323);
////
        LJRenderer renderer = new HTMLRenderer();
        List<Comment> comments2 = reader.readComments(entry.getItemid());
        String output = renderer.renderFullEntry(entry, comments2);
        FileWriter writer = new FileWriter("post.html");
        writer.write(output);
        writer.close();
        System.out.println(output);


    }

}
