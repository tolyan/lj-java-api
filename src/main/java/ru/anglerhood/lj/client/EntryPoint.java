package ru.anglerhood.lj.client;

import org.apache.log4j.BasicConfigurator;
import org.sqlite.SQLiteJDBCLoader;
import ru.anglerhood.lj.client.pictures.PictureStorage;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

public class EntryPoint {

    private static final String JOURNAL_NAME = "metapractice";

    public static void main(String[] args) throws ClassNotFoundException, IOException, NoSuchAlgorithmException {
        BasicConfigurator.configure();
        System.out.println(String.format("running in %s mode", SQLiteJDBCLoader.isNativeMode() ? "native" : "pure-java"));
        Client client = new Client();
//        client.initWriter(JOURNAL_NAME , SQLiteWriter.class);
//        client.scrapJournal(JOURNAL_NAME);
//
//        BlogEntryReader reader = new SQLiteReader(JOURNAL_NAME);
//        BlogEntry entry = reader.readEntry(1323);
//////
        client.renderJournal(JOURNAL_NAME);


//        PictureStorage storage = new PictureStorage();
//        storage.storePicture(new URL("http://pix.academ.org/img/2012/10/06/a316c54173ad2bc5a01cd0d98004cda1.jpg"));



    }

}
