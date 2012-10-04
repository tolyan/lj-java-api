package ru.anglerhood.lj.api.xmlrpc.results;

import ru.anglerhood.lj.api.LJHelpers;
import ru.anglerhood.lj.client.Util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: anglerhood
 * Date: 10/3/12
 * Time: 12:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class Comment {
    public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH:mm:ss");
    private Integer pages;
    private Integer datePostUnix;
    private Integer isLoaded;
    private Integer isShow;
    private String postername;
    private Integer dtalkid;
    private Integer level;
    private String subject;
    private String body;
    private String state;
    private Integer posterid;
    private List <Comment> children;


    public Comment(Map map) throws ParseException, UnsupportedEncodingException {
        pages = (Integer) map.get("pages");
        datePostUnix = (Integer) map.get("datepostunix");
        isLoaded = (Integer) map.get("is_loaded");
        isShow = (Integer) map.get("is_show");
        postername = (String) map.get("postername");
        dtalkid = (Integer) map.get("dtalkid");
        level = (Integer) map.get("level");
        subject = LJHelpers.getUnicodeText(map.get("subject"));
        body = LJHelpers.getUnicodeText(map.get("body"));
        state = (String) map.get("state");
        posterid = (Integer) map.get("posterid" );
        children = addChildren(map);
        //TODO implement child comments unmarshalling
    }

    private List<Comment> addChildren(Map map) throws ParseException, UnsupportedEncodingException {
        List<Comment> result = new LinkedList<Comment>();
        Object [] rawChildren = (Object [])map.get("children");
        if(null != rawChildren) {
            for (Object childMap : rawChildren ) {
                Comment child = new Comment((Map) childMap);
                result.add(child);
            }
        }
        return result;
    }

    public Integer getPages() {
        return pages;
    }

    public Integer getDatePostUnix() {
        return datePostUnix;
    }

    public Integer getLoaded() {
        return isLoaded;
    }

    public Integer getShow() {
        return isShow;
    }

    public String getPostername() {
        return postername;
    }

    public Integer getDtalkid() {
        return dtalkid;
    }

    public Integer getLevel() {
        return level;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getState() {
        return state;
    }

    public Integer getPosterid() {
        return posterid;
    }

    public List<Comment> getChildren() {
        return children;
    }


    public String toString() {
        return "Comment: { " +
                    "pages =>"          + Util.nullString(pages) +
                    ", datepostunix =>" + Util.nullString(datePostUnix) +
                    ", is_loaded =>"    + Util.nullString(isLoaded) +
                    ", is_show =>"      + Util.nullString(isShow) +
                    ", postername =>"   + Util.nullString(postername) +
                    ", dtalkid =>"      + Util.nullString(dtalkid.toString()) +
                    ", level =>"        + Util.nullString(level.toString()) +
                    ", subject =>"      + Util.nullString(subject) +
                    ", body =>"         + Util.nullString(body) +
                    ", state =>"        + Util.nullString(state) +
                    ", posterid =>"     + Util.nullString(posterid) + "}";
    }







}
