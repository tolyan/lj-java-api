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


package ru.anglerhood.lj.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.anglerhood.lj.api.XMLRPCClient;
import ru.anglerhood.lj.api.XMLRPCClientImpl;
import ru.anglerhood.lj.api.xmlrpc.arguments.GetCommentsArgument;
import ru.anglerhood.lj.api.xmlrpc.arguments.GetDayCountsArgument;
import ru.anglerhood.lj.api.xmlrpc.arguments.GetEventsArgument;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;
import ru.anglerhood.lj.api.xmlrpc.results.DayCount;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Convenient client to use LJ API
 */

public class Client {
    private final XMLRPCClient client = new XMLRPCClientImpl();
    private BlogEntryWriter writer;

    private Log logger = LogFactory.getLog(Client.class);
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

    /**
     * Gets blog entry with specified id
     * @param entryId id of BlogEntry
     * @return BlogEntry
     */
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

    /**
     * Retrieves all blog entries published on the given date
     *
     * @param date    certain date
     * @return an array of blog entries
     * @see ru.anglerhood.lj.api.xmlrpc.results.BlogEntry
     */
    public BlogEntry[] getBlogEntriesOn(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        GetEventsArgument argument = new GetEventsArgument();
        argument.setCreds(user, passwd);
        argument.setSelecttype(GetEventsArgument.Type.DAY);
        argument.setYear(calendar.get(Calendar.YEAR));
        argument.setMonth(calendar.get(Calendar.MONTH) + 1);
        argument.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        return client.getevents(argument, TIMEOUT);
    }


    /**
     * Gets comments collection for specified blog entry
     * @param entryId id of BlogEntry
     * @param anum property of BlogEntry
     * @return List of Comments
     */

    public List<Comment> getComments(int entryId, int anum) {
        GetCommentsArgument arg = new GetCommentsArgument();
        arg.setCreds(user, passwd);
        arg.setDItemId(entryId, anum);
        arg.setExpandStrategy("expand_all");
        arg.setLineEndings("unix");
        arg.setJournal(user);
        return client.getcomments(arg, TIMEOUT);
    }

    /**
     * Gets comments collection for specified blog entry
     * @param entry  BlogEntry
     * @return List of Comments
     */
    public List<Comment> getComments(BlogEntry entry) {
        return getComments(entry.getItemid(), entry.getAnum());
    }

    public void storeFullEntry(int entryId, Class writerClass) {
        try {
            BlogEntry entry = getBlogEntry(entryId);
            List<Comment> comments = getComments(entry);
            Constructor<BlogEntryWriter> ctor = writerClass.getConstructor(String.class);
            writer = ctor.newInstance(user);
            writer.write(entry);
            writer.write(comments);

        } catch (InstantiationException e) {
            logger.error("Could not instatiate class " + writerClass.getName());
        } catch (IllegalAccessException e) {
            logger.error("Illegal access to class " + writerClass.getName());
        } catch (NoSuchMethodException e) {
            logger.error("Constructor is not defined for " + writerClass.getName());
        } catch (InvocationTargetException e) {
           logger.error("Error while invocing constructor for " + writerClass.getName());
        }

    }
    
    public void initWriter(String journal, Class writerClass) {
        try {
            Constructor<BlogEntryWriter> ctor = writerClass.getConstructor(String.class);
            writer = ctor.newInstance(journal);
            writer.init();
        } catch (InstantiationException e) {
            logger.error("Could not instatiate class " + writerClass.getName());
        } catch (IllegalAccessException e) {
            logger.error("Illegal access to class " + writerClass.getName());
        } catch (NoSuchMethodException e) {
            logger.error("Constructor is not defined for " + writerClass.getName());
        } catch (InvocationTargetException e) {
            logger.error("Error while invocing constructor for " + writerClass.getName());
        }  
    }

    /**
     * Retrieves the number of journal entries per day.
     *
     * @return DayCount struct
     * @see ru.anglerhood.lj.api.xmlrpc.results.DayCount
     */
    public DayCount[] getDayCounts() {
        GetDayCountsArgument argument = new GetDayCountsArgument();
        argument.setCreds(user, passwd);
        argument.setUsejournal(user);
        return client.getdaycounts(argument, TIMEOUT);
    }


        /**
        * Retrieves the number of journal entries per day.
        *
        * @return DayCount struct
        * @see ru.anglerhood.lj.api.xmlrpc.results.DayCount
        */
    public DayCount[] getDayCounts(String journalName) {
        GetDayCountsArgument argument = new GetDayCountsArgument();
        argument.setCreds(user, passwd);
        argument.setUsejournal(journalName);
        return client.getdaycounts(argument, TIMEOUT);
    }

    public void storeJournal() {
        storeJournal(user);
    }

    public void storeJournal(String journalName) {
        DayCount [] counts = getDayCounts();
        for(DayCount count : counts) {
            Date date = count.getDate();
            BlogEntry [] entries = getBlogEntriesOn(date);
            for (BlogEntry entry : entries) {
                storeFullEntry(entry.getItemid(), SQLiteWriter.class);
            }
        }

    }

    //TODO impelement full journal download

    public String getUser() {
        return user;
    }


}
