package ru.anglerhood.lj.client;

import org.apache.log4j.BasicConfigurator;
import org.sqlite.SQLiteJDBCLoader;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;
import ru.anglerhood.lj.client.sql.SQLiteReader;

import java.util.List;

public class EntryPoint {
    public static void main(String[] args) throws ClassNotFoundException {
        BasicConfigurator.configure();
        System.out.println(String.format("running in %s mode", SQLiteJDBCLoader.isNativeMode() ? "native" : "pure-java"));
        Client client = new Client();
//        List<Comment> comments = client.getComments(client.getBlogEntry(1));
//        client.initWriter("client_test_api" , SQLiteWriter.class);
//        client.storeJournal();
//
        BlogEntryReader reader = new SQLiteReader("client_test_api");
        BlogEntry entry = reader.readEntry(1);
        List<Comment> comments = reader.readComments(entry.getItemid());
        System.out.println(comments.get(0));
//        System.out.println(entry);


    }

}
