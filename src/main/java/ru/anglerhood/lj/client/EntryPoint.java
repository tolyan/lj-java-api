package ru.anglerhood.lj.client;

import org.apache.log4j.BasicConfigurator;
import ru.anglerhood.lj.api.XMLRPCClient;
import ru.anglerhood.lj.api.XMLRPCClientImpl;
import ru.anglerhood.lj.api.xmlrpc.arguments.LoginArgument;
import ru.anglerhood.lj.api.xmlrpc.results.UserData;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class EntryPoint {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        LoginArgument arg = new LoginArgument();
        Downloader down = new Downloader();
        down.login();
        down.getLastEntry();
    }

}
