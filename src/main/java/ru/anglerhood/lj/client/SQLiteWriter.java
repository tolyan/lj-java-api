package ru.anglerhood.lj.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.anglerhood.lj.api.XMLRPCClient;
import ru.anglerhood.lj.api.xmlrpc.SecurityType;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;

import java.sql.*;
import java.util.Date;
import java.util.List;

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
public class SQLiteWriter implements BlogEntryWriter {
    private static Log logger = LogFactory.getLog(SQLiteWriter.class);
    private String journal;
    private Connection connection;

    private static final String ENTRY = "entry";
    private final static String BLOG_ENTRY_SCHEME =  ENTRY + " (" +
                                                                "itemid integer PRIMARY KEY," +
                                                                "permalink string," +
                                                                "anum integer," +
                                                                "body string," +
                                                                "date datetime," +
                                                                "subject string" +
                                                                ")";
    private static final String COMMENT = "comment" ;
    public final static String COMMENT_SCHEME = COMMENT + " (" +
                                                                "entryid integer, " +
                                                                "dtalkid integer, " +
                                                                "parent_dtalkid integer, " +
                                                                "posterid integer, " +
                                                                "postername string, " +
                                                                "datepostunix integer, " +
                                                                "level integer, " +
                                                                "subject string, " +
                                                                "body string, " +
                                                                "FOREIGN KEY(entryid) REFERENCES " + ENTRY + "(entryid)" +
                                                                ")";





    private static final String INSERT_ENTRY = "INSERT into " + ENTRY +
                                               " values(?, ?, ?, ?, ?, ?);";
    private static final String INSERT_COMMENT = "INSERT into " + COMMENT +
                                               " values(?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public SQLiteWriter(String journal) {
        this.journal = journal;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + journal + ".db");

        } catch (ClassNotFoundException e) {
            logger.error("Could find JDBC driver for SQLite! " + e.getMessage());
        } catch (SQLException e) {
            logger.error("Invalid SQL: " + e.getMessage());
        } finally {
            if (null != connection ) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("SQLite could close connection: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void init(){
        Connection conn = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + journal + ".db");
            Statement st = createTimeoutStatetment();
            st.executeUpdate("drop table if exists " + ENTRY);
            st.executeUpdate("create table " + BLOG_ENTRY_SCHEME);
            st.executeUpdate("drop table if exists " + COMMENT);
            st.executeUpdate("create table " + COMMENT_SCHEME);
            st.close();
        } catch (SQLException e) {
            logger.error("Invalid SQL: " + e.getMessage());
        }
    }

    @Override
    public void write(BlogEntry entry) {
        try {
            logger.debug("Write blog entry to DB");
            PreparedStatement st = connection.prepareStatement(INSERT_ENTRY);
            st.setInt(1, entry.getItemid());
            st.setString(2, entry.getPermalink());
            st.setInt(3, entry.getAnum());
            st.setString(4, entry.getBody());
            st.setDate(5, new java.sql.Date(entry.getDate().getTime()));
            st.setString(6, entry.getSubject());
            st.execute();
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        }


    }

    @Override
    public void write(Comment comment, int entryid) {
        try {
            logger.debug("Write comment entry to DB");
            PreparedStatement st = connection.prepareStatement(INSERT_COMMENT);
            st.setInt(1, entryid);
            st.setInt(2, comment.getDtalkid());
            st.setInt(3, comment.getParentDtalkId());
            st.setInt(4, comment.getPosterid());
            st.setString(5, comment.getPostername());
            st.setInt(6, comment.getDatePostUnix());
            st.setInt(7, comment.getLevel());
            st.setString(8, comment.getSubject());
            st.setString(9, comment.getBody());
            st.execute();
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        }

    }

    @Override
    public void write(List<Comment> comments, int entryid) {
        for (Comment comment : comments) {
            write(comment, entryid);
            write(comment.getChildren(), entryid);
        }

    }

    private Statement createTimeoutStatetment() throws SQLException {
        Statement st = connection.createStatement();
        st.setQueryTimeout(30);
        return st;
    }


}
