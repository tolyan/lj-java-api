package ru.anglerhood.lj.client.sql;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

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
public class BlogEntryHandler implements ResultSetHandler {


    private static Log logger = LogFactory.getLog(BlogEntryHandler.class);


    @Override
    public List<BlogEntry> handle(ResultSet resultSet) throws SQLException {
        List<BlogEntry> result = new LinkedList<BlogEntry>();
        while(resultSet.next()) {
            Map raw = new HashMap();
            raw.put(BlogEntry.ITEMID, resultSet.getInt(BlogEntry.ITEMID));
            raw.put(BlogEntry.PERMALINK, resultSet.getString(BlogEntry.PERMALINK));
            raw.put(BlogEntry.ANUM, resultSet.getInt(BlogEntry.ANUM));
            raw.put(BlogEntry.BODY, resultSet.getString(BlogEntry.BODY));
            raw.put(BlogEntry.DATE, BlogEntry.DATEFORMAT.format(resultSet.getDate(BlogEntry.DATE)));
            raw.put(BlogEntry.SUBJECT, resultSet.getString(BlogEntry.SUBJECT));
            raw.put(BlogEntry.REPLY_COUNT, resultSet.getInt(BlogEntry.REPLY_COUNT));
            try {
                result.add(new BlogEntry(raw));
            } catch (UnsupportedEncodingException e) {
                logger.error(String.format("Blog entry %s contains text in unsupported encoding, %s", resultSet.getInt(BlogEntry.ITEMID), e.getMessage()));
            } catch (ParseException e) {
                logger.error(String.format("Error while parsing date %s, %s", resultSet.getDate(BlogEntry.DATE), e.getMessage()));
            }
        }
        return result;
    }
}
