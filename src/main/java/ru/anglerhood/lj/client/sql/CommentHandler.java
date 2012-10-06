package ru.anglerhood.lj.client.sql;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
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
public class CommentHandler  implements ResultSetHandler {
    private  Log logger = LogFactory.getLog(CommentHandler.class);

    @Override
    public List<Comment> handle(ResultSet resultSet) throws SQLException {
        List<Comment> result = new LinkedList<Comment>();
        while(resultSet.next()) {
            Map raw = new HashMap();
            raw.put(Comment.DTALKID, resultSet.getInt(Comment.DTALKID));
            raw.put(Comment.SUBJECT, resultSet.getString(Comment.SUBJECT));
            raw.put(Comment.DATEPOSTUNIX, resultSet.getInt(Comment.DATEPOSTUNIX));
            raw.put(Comment.BODY, resultSet.getString(Comment.BODY));
            raw.put(Comment.DATEPOSTUNIX, resultSet.getInt(Comment.DATEPOSTUNIX));
            raw.put(Comment.POSTERNAME, resultSet.getString(Comment.POSTERNAME));
            raw.put(Comment.PARENTDTALKID, resultSet.getInt(Comment.PARENTDTALKID));
            raw.put(Comment.LEVEL, resultSet.getInt(Comment.LEVEL));
            try {
                result.add(new Comment(raw,resultSet.getInt(Comment.ENTRYID)));
            } catch (UnsupportedEncodingException e) {
                logger.error(String.format("Blog entry %s contains text in unsupported encoding, %s", resultSet.getInt(Comment.DTALKID), e.getMessage()));
            }
        }
        return result;
    }
}
