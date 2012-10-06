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

    /**
     * Represents commentary collection for specific BlogEntry
     * @param map
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    public Comment(Map map) throws ParseException, UnsupportedEncodingException {
        parentdtalkid = (Integer) map.get("parentdtalkid");
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

    public Integer getParentDtalkId() {
        return parentdtalkid;
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
        if (subject == null)
            return "";
        else
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







}
