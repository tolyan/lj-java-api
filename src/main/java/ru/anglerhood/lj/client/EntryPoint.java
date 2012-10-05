package ru.anglerhood.lj.client;

import org.apache.log4j.BasicConfigurator;
import org.sqlite.SQLiteJDBCLoader;
import ru.anglerhood.lj.api.XMLRPCClient;
import ru.anglerhood.lj.api.XMLRPCClientImpl;
import ru.anglerhood.lj.api.xmlrpc.arguments.LoginArgument;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.UserData;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.*;

public class EntryPoint {
    public static void main(String[] args) throws ClassNotFoundException {
        BasicConfigurator.configure();
        System.out.println(String.format("running in %s mode", SQLiteJDBCLoader.isNativeMode() ? "native" : "pure-java"));
        BlogEntryWriter writer = new SQLiteWriter("test");
        writer.init();
        Client client = new Client();
        BlogEntry entry = client.getBlogEntry(-1);
        writer.write(entry);

    }

}
