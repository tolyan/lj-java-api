package ru.anglerhood.lj.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.anglerhood.lj.api.XMLRPCClient;
import ru.anglerhood.lj.api.XMLRPCClientImpl;
import ru.anglerhood.lj.api.xmlrpc.arguments.GetCommentsArgument;
import ru.anglerhood.lj.api.xmlrpc.arguments.GetEventsArgument;
import ru.anglerhood.lj.api.xmlrpc.arguments.LoginArgument;
import ru.anglerhood.lj.api.xmlrpc.arguments.SessionGenerateArgument;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;
import ru.anglerhood.lj.api.xmlrpc.results.UserData;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
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
    private Log logger = LogFactory.getLog(Downloader.class);
    private static final int TIMEOUT = 0;

    private String user;
    private String passwd;
    private String session;

    public void setCredentials() {
        //N.B.! IDEA starts without env variables set in bashrc/bash_profile.
        //Use bash --login to overcome this issue.
        user = System.getenv("LJ_USER");
        passwd = System.getenv("LJ_PASSWD");
        if (user == null || user.isEmpty()) logger.debug("LJ_USER is not set");
        if (passwd == null || passwd.isEmpty()) logger.debug("LJ_PASSWD is not set");
    }

    public void login() {
        setCredentials();
        LoginArgument arg = new LoginArgument();
        arg.setUsername(user);
        arg.setHpassword(passwd);
        UserData data = client.login(arg, TIMEOUT);
        System.out.println(data);
    }


    public void getLastEntry() {
        GetEventsArgument arg = new GetEventsArgument();
        arg.setCreds(user, passwd);
        arg.setSelecttype(GetEventsArgument.Type.ONE);
        arg.setItemid(-1);
        BlogEntry [] entries = client.getevents(arg, TIMEOUT);
        BlogEntry entry = entries[0];
        GetCommentsArgument comArg = new GetCommentsArgument();
        comArg.setCreds(user, passwd);
        comArg.setDItemId(entry.getDItemId());
        comArg.setExpandStrategy("expand_all");
        comArg.setLineEndings("unix");
        comArg.setJournal(user);
        Comment[] comments = client.getcomments(comArg, TIMEOUT);
        for(Comment com : comments) {
            System.out.println(com.toString());
        }

    }



}
