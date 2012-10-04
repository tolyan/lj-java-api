package ru.anglerhood.lj.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.anglerhood.lj.api.XMLRPCClient;
import ru.anglerhood.lj.api.XMLRPCClientImpl;
import ru.anglerhood.lj.api.xmlrpc.arguments.GetCommentsArgument;
import ru.anglerhood.lj.api.xmlrpc.arguments.GetEventsArgument;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: anglerhood
 * Date: 10/4/12
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client {
    private final XMLRPCClient client = new XMLRPCClientImpl();
    private Log logger = LogFactory.getLog(Downloader.class);
    private static final int TIMEOUT = 0;

    private String user;
    private String passwd;

    public Client(){
        //N.B.! IDEA starts without env variables set in bashrc/bash_profile.
        //Use bash --login to overcome this issue.
        user = System.getenv("LJ_USER");
        passwd = System.getenv("LJ_PASSWD");
        if (user == null || user.isEmpty()) logger.debug("LJ_USER is not set");
        if (passwd == null || passwd.isEmpty()) logger.debug("LJ_PASSWD is not set");
    }

    public BlogEntry getBlogEntry(int entryId) {
        GetEventsArgument arg = new GetEventsArgument();
        arg.setCreds(user, passwd);
        arg.setSelecttype(GetEventsArgument.Type.ONE);
        arg.setItemid(entryId);
        BlogEntry [] entries = client.getevents(arg, TIMEOUT);
        if(entries.length == 0)
            return null;
        else
            return entries[0];

    }

    public List<Comment> getComments(int entryId, int anum) {
        GetCommentsArgument arg = new GetCommentsArgument();
        arg.setCreds(user, passwd);
        arg.setDItemId(BlogEntry.getDItemId(entryId, anum));
        arg.setExpandStrategy("expand_all");
        arg.setLineEndings("unix");
        arg.setJournal(user);
        return client.getcomments(arg, TIMEOUT);
    }
}
