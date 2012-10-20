/*
 * Copyright (c) 2012, Anatoly Rybalchenko
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions
 *       and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *       and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * The name of the author may not be used may not be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */


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

public class Comment {
    public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH:mm:ss");

    public static final String PARENTDTALKID = "parentdtalkid";
    public static final String PAGES = "pages";
    public static final String DATEPOSTUNIX = "datepostunix";
    public static final String IS_LOADED = "is_loaded";
    public static final String IS_SHOW = "is_show";
    public static final String POSTERNAME = "postername";
    public static final String DTALKID = "dtalkid";
    public static final String LEVEL = "level";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    public static final String STATE = "state";
    public static final String POSTERID = "posterid";
    public static final String ENTRYID = "entryid";

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
    private Integer parentdtalkid;
    private Integer entryId;

    /**
     * Represents commentary collection for specific BlogEntry
     * @param map
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    public Comment(Map map, Integer entryId) throws UnsupportedEncodingException {
        this.entryId = entryId;
        parentdtalkid = (Integer) map.get(PARENTDTALKID);
        pages = (Integer) map.get(PAGES);
        datePostUnix = (Integer) map.get(DATEPOSTUNIX);
        isLoaded = (Integer) map.get(IS_LOADED);
        isShow = (Integer) map.get(IS_SHOW);
        postername = (String) map.get(POSTERNAME);
        dtalkid = (Integer) map.get(DTALKID);
        level = (Integer) map.get(LEVEL);
        subject = LJHelpers.getUnicodeText(map.get(SUBJECT));
        body = LJHelpers.getUnicodeText(map.get(BODY));
        state = (String) map.get(STATE);
        posterid = (Integer) map.get(POSTERID);
        children = addChildren(map);
    }


    private List<Comment> addChildren(Map map) throws UnsupportedEncodingException {
        List<Comment> result = new LinkedList<Comment>();
        Object [] rawChildren = (Object [])map.get("children");
        if(null != rawChildren) {
            for (Object childMap : rawChildren ) {
                Comment child = new Comment((Map) childMap, this.entryId);
                result.add(child);
            }
        }
        return result;
    }

    public void addChild(Comment child) {
        children.add(child);
    }


    public Comment findChild(Integer dtalkid) {
        for (Comment comment : children) {
           if (dtalkid.equals(comment.getDtalkid())){
               return comment;
           }
        }

        //Обход в ширину
        for (Comment comment : children) {
           Comment result = comment.findChild(dtalkid);
           if (result != null) return result;
        }

        return null;
    }

    public Integer getParentDtalkId() {
        return parentdtalkid;
    }


    public Integer getPages() {
        return pages;
    }

    public Integer getDatePostUnix() {
        return datePostUnix;
    }

    public Date getDate() {
        return new Date(datePostUnix.longValue() * 1000);
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
        if (subject == null)
            return "subject ";
        else
            return subject;
    }

    public String getBody() {
        return body;
    }
    public String getHTMLBody() {
        return Util.toHTML(body);
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

    public Integer getEntryId() {
        return entryId;
    }

    public String toString() {
        return "Comment: { " +
                    "parentdtalkid =>"        + Util.nullString((parentdtalkid)) +
                    ", pages =>"          + Util.nullString(pages) +
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


    public boolean  equals(Object obj){
        if(this == obj) return true;
        if((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        }
        Comment test = (Comment) obj;
        return  (test.entryId == this.entryId)
                || (test.datePostUnix == this.datePostUnix)
                || (test.dtalkid == this.dtalkid);

    }


    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (null == entryId ? 0 : entryId.hashCode())
                            + (null == datePostUnix ? 0 : datePostUnix.hashCode()
                            + (null == dtalkid ? 0 : dtalkid.hashCode()));
        return hash;
    }


}
