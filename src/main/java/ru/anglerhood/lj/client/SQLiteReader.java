package ru.anglerhood.lj.client;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
public class SQLiteReader implements BlogEntryReader {

    private static Log logger = LogFactory.getLog(SQLiteReader.class);
    private Connection connection;
    private String journal;


    private static String SELECT_ENTRY = "SELECT * from " + SQLiteWriter.ENTRY +
                                            " where " + SQLiteWriter.ITEMID + " = ?;";

    public SQLiteReader(String journal) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + journal + ".db");
        } catch (ClassNotFoundException e) {
            logger.error("Could find JDBC driver for SQLite! " + e.getMessage());
        } catch (SQLException e) {
            logger.error("Invalid SQL: " + e.getMessage());
        }
        this.journal = journal;
    }
    @Override
    public BlogEntry readEntry(int entryId) {
        BlogEntry entry = null;
        try {
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<List<BlogEntry>>  handler = new BlogEntryHandler();
            List<BlogEntry> result = runner.query(connection, SELECT_ENTRY, handler , entryId);
            entry = result.get(0);
        } catch (SQLException e) {
            logger.error(String.format("SQL Error: %s", e.getMessage()));
        }
        return entry;
    }

    @Override
    public List<Comment> readComments(int entryId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
