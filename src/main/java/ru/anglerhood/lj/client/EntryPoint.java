package ru.anglerhood.lj.client;

import org.apache.log4j.BasicConfigurator;
import org.sqlite.SQLiteJDBCLoader;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.client.sql.SQLiteReader;
import ru.anglerhood.lj.client.sql.SQLiteWriter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class EntryPoint {

    private static final String JOURNAL_NAME = "metapractice";

    public static void main(String[] args) throws ClassNotFoundException, IOException, NoSuchAlgorithmException {
        BasicConfigurator.configure();
        System.out.println(String.format("running in %s mode", SQLiteJDBCLoader.isNativeMode() ? "native" : "pure-java"));
        Client client = new Client(SQLiteWriter.class, JOURNAL_NAME);
//        client.initWriter();
//        client.scrapJournal();
//
        BlogEntryReader reader = new SQLiteReader(JOURNAL_NAME);
//        client.renderJournal();
        client.writeIndex(JOURNAL_NAME);


//        PictureStorage storage = new PictureStorage();
//        storage.storePicture(new URL("http://pix.academ.org/img/2012/10/06/a316c54173ad2bc5a01cd0d98004cda1.jpg"));



    }

}
