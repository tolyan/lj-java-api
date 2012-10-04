package ru.anglerhood.lj.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.anglerhood.lj.api.XMLRPCClient;
import ru.anglerhood.lj.api.xmlrpc.SecurityType;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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

    private static final String ENTRY = "entry";
    private final static String BLOG_ENTRY_SCHEME =  ENTRY + " (" +
                                                                "itemid integer," +
                                                                "permalink string," +
                                                                "anum integer," +
                                                                "body string," +
                                                                "date datetime," +
                                                                "subject string," +
                                                                "security string," +
                                                                "allowmask integer," +
                                                                "poster string" +
                                                                ")";



    private Date date;
    private String subject;
    private SecurityType security;
    private Integer allowmask;
    private String poster;


    public SQLiteWriter(String journal) {
        this.journal = journal;
    }

    @Override
    public void init(){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + journal + ".db");
            Statement st = conn.createStatement();
            st.setQueryTimeout(30);
            st.executeUpdate("drop table if exists " + ENTRY);
            st.executeUpdate("create table " + BLOG_ENTRY_SCHEME);
            st.close();
        } catch (ClassNotFoundException e) {
            logger.error("Could find JDBC driver for SQLite! " + e.getMessage());
        } catch (SQLException e) {
            logger.error("Invalid SQL: " + e.getMessage());
        } finally {
            if (null != conn ) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("SQLite could close connection: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void write(BlogEntry entry) {


    }

    @Override
    public void write(Comment comment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
