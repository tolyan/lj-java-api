package ru.anglerhood.lj;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.anglerhood.lj.api.LJHelpers;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;
import ru.anglerhood.lj.client.Client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */

public class ClientTest
{


    @org.junit.Test
    public void getBlogEntry() throws ParseException {
        Client client = new Client();
        BlogEntry entry = client.getBlogEntry(1);

        assertEquals(entry.getSubject(), "Тестовый заголовок");
        assertEquals(entry.getBody(), "Тестовое Тело");
        assertEquals(entry.getItemid(), 1);
        assertEquals(entry.getAnum(), new Integer(197));
        assertEquals(entry.getPermalink(), "#/453.html");
        SimpleDateFormat format = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH:mm:ss");
        long time = LJHelpers.parseDate("2012-10-02 00:00:00", format).getTime();
        assertEquals(entry.getDate().getTime(), time);

    }

    @org.junit.Test
    public void getComments(){
        Client client = new Client();
        List<Comment> comments = client.getComments(1, 197);
        Comment first = comments.get(0);
        assertEquals(first.getBody(), "абвАБВ");
        assertEquals(first.getDatePostUnix(), new Integer(1349292467));
        Comment second = comments.get(0).getChildren().get(0);
        assertEquals(second.getBody(), "коммент 2");
        assertEquals(second.getDatePostUnix(), new Integer(1349374862));

    }

}
