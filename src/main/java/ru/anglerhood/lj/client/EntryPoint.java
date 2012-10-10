package ru.anglerhood.lj.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.BasicConfigurator;
import org.sqlite.SQLiteJDBCLoader;
import ru.anglerhood.lj.api.LJHelpers;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;
import ru.anglerhood.lj.client.pictures.PictureStorage;
import ru.anglerhood.lj.client.render.HTMLRenderer;
import ru.anglerhood.lj.client.render.LJRenderer;
import ru.anglerhood.lj.client.sql.SQLiteReader;
import ru.anglerhood.lj.client.sql.SQLiteWriter;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class EntryPoint {

    private static final String JOURNAL_NAME = "metapractice";

    public static void main(String[] args) throws ClassNotFoundException, IOException, NoSuchAlgorithmException {
        BasicConfigurator.configure();
        System.out.println(String.format("running in %s mode", SQLiteJDBCLoader.isNativeMode() ? "native" : "pure-java"));
//        Client client = new Client();
//        client.initWriter(JOURNAL_NAME , SQLiteWriter.class);
//        client.scrapJournal(JOURNAL_NAME);
//
//        BlogEntryReader reader = new SQLiteReader(JOURNAL_NAME);
//        BlogEntry entry = reader.readEntry(1323);
//////
//        LJRenderer renderer = new HTMLRenderer();
//        List<Comment> comments2 = reader.readComments(entry.getItemid());
//        String output = renderer.renderFullEntry(entry, comments2);
//        FileWriter writer = new FileWriter("post.html");
//        writer.write(output);
//        writer.close();
//        System.out.println(output);
        PictureStorage storage = new PictureStorage();
        storage.storePicture(new URL("http://pix.academ.org/img/2012/10/06/a316c54173ad2bc5a01cd0d98004cda1.jpg"));



    }

}
