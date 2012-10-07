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
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        BasicConfigurator.configure();
        System.out.println(String.format("running in %s mode", SQLiteJDBCLoader.isNativeMode() ? "native" : "pure-java"));
//        Client client = new Client();
//        List<Comment> comments = client.getComments(client.getBlogEntry(1));
//        client.initWriter("client_test_api" , SQLiteWriter.class);
//        client.storeJournal();

        BlogEntryReader reader = new SQLiteReader("client_test_api");
        BlogEntry entry = reader.readEntry(1);
//
        LJRenderer renderer = new HTMLRenderer();
//        System.out.println(renderer.renderBlogEntry(entry));
        List<Comment> comments2 = reader.readComments(entry.getItemid());
        String output = renderer.renderFullEntry(entry, comments2);
        FileWriter writer = new FileWriter("post.html");
        writer.write(output);
        writer.close();
        System.out.println(output);


    }

}
