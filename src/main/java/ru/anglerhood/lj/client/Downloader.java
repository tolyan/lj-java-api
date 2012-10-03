package ru.anglerhood.lj.client;

import ru.anglerhood.lj.api.XMLRPCClient;
import ru.anglerhood.lj.api.XMLRPCClientImpl;
import ru.anglerhood.lj.api.xmlrpc.arguments.LoginArgument;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: engage
 * Date: 10/3/12
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Downloader {
    private final XMLRPCClient client = new XMLRPCClientImpl();
    private String user;
    private String passwd;

    public void getCredentials() {
        System.out.println("User?");
        Scanner scan = new Scanner(System.in);
        user = scan.nextLine();
        System.out.println("Passwd");
        passwd = scan.nextLine();
        scan.close();
    }

    public void login() {
        getCredentials();
        LoginArgument arg = new LoginArgument();
        arg.setUsername(user);
        arg.setHpassword(passwd);
        client.login(arg, 0);
    }

}
