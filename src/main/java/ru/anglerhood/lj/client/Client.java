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

import org.apache.commons.io.FileUtils;
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
import ru.anglerhood.lj.client.render.HTMLRenderer;
import ru.anglerhood.lj.client.render.LJRenderer;
import ru.anglerhood.lj.client.sql.SQLiteReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;


/**
 * Convenient client to use LJ API
 */

public class Client {
    private static final int THREADS_POOL_SIZE = 25;
    private final XMLRPCClient client = new XMLRPCClientImpl();
    private final BlogEntryWriter writer;

    private Log logger = LogFactory.getLog(Client.class);
    private static final int TIMEOUT = 0;

    private String user;
    private String passwd;
    private Constructor<BlogEntryWriter> writerClassConstructor;

    private final ExecutorService pool;
    private int threadNumber;
    private String journal;
    private int commentCounter;


    public Client(Class writerClass, String journal){
        //N.B.! IDEA starts without env variables set in bashrc/bash_profile.
        //Use bash --login to overcome this issue.
        this.journal = journal;

        user = System.getenv("LJ_USER");
        passwd = System.getenv("LJ_PASSWD");
        if (user == null || user.isEmpty()) logger.debug("LJ_USER is not set");
        if (passwd == null || passwd.isEmpty()) logger.debug("LJ_PASSWD is not set");

        try {
            writerClassConstructor = writerClass.getConstructor(String.class);
            writer = writerClassConstructor.newInstance(this.journal);
        }  catch (NoSuchMethodException e) {
            logger.error("Constructor is not defined for " + writerClass.getName());
            throw new RuntimeException("Couldn't initate writer");
        } catch (InstantiationException e) {
            logger.error("Could not instatiate class " + writerClassConstructor.getName());
            throw new RuntimeException("Couldn't initate writer");
        } catch (IllegalAccessException e) {
            logger.error("Illegal access to class " + writerClassConstructor.getName());
            throw new RuntimeException("Couldn't initate writer");
        } catch (InvocationTargetException e) {
            logger.error("Error while invocing constructor for " + writerClassConstructor.getName());
            throw new RuntimeException("Couldn't initate writer");
        }

        pool = Executors.newFixedThreadPool(THREADS_POOL_SIZE);
    }


    public BlogEntry getBlogEntry(int entryId) {
        return getBlogEntry(entryId, user);
    }

    /**
     * Gets blog entry with specified id
     * @param entryId id of BlogEntry
     * @return BlogEntry
     */
    public BlogEntry getBlogEntry(int entryId, String journal) {
        GetEventsArgument arg = new GetEventsArgument();
        arg.setCreds(user, passwd);
        arg.setUsejournal(journal);
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
    public BlogEntry[] getBlogEntriesOn(Date date, String journal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        GetEventsArgument argument = new GetEventsArgument();
        argument.setCreds(user, passwd);
        argument.setUsejournal(journal);
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
        arg.setJournal(journal);
        return client.getcomments(arg, TIMEOUT);
    }

    public List<Comment> getComments(BlogEntry entry) {
        return getComments(entry.getItemid(), entry.getAnum());
    }

    public boolean storeFullEntry(BlogEntry entry) {
        List<Comment> comments = getComments(entry);
        writer.write(entry);
        writer.write(comments);
        logger.debug(String.format("Stored entry %s", entry.getItemid()));
        return true;
    }

    
    public void initWriter() {
        writer.init();
        logger.debug(String.format("Inited writer for %s", journal));
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


    public void scrapJournal() {
        long time = System.nanoTime();
        int okayTasks = 0;

        ArrayList<Callable<Boolean>> todo = new ArrayList<Callable<Boolean>>();

        BlogEntry lastEntry = getBlogEntry(-1, journal);
        for(int id = lastEntry.getItemid(); id >= 1; id--) {
            todo.add(new Scrapper(id));
        }
        try {
            List<Future<Boolean>> results = pool.invokeAll(todo);

            for(Future<Boolean> result : results) {
                try {
                    if (result.get()){
                        okayTasks++;
                    }
                } catch (ExecutionException e) {
                    logger.error(String.format("Error while getting result of scrapping task %s", e.getMessage()));
                }
            }
            logger.debug(String.format("Executed %s scrapping tasks and stored %s entries", results.size(), okayTasks));
        } catch (InterruptedException e) {
            logger.error(String.format("Execution of scrapJournal interrupted, %s", e.getMessage()));
        }
        pool.shutdown();
        writer.shutdown();

        logger.debug(String.format("Stored journal with %s entries and %s comments for %s milliseconds",
                                    okayTasks, commentCounter, (System.nanoTime() - time)/1000000L));
    }

    public void storeJournal() {
        DayCount [] counts = getDayCounts(journal);
        for(DayCount count : counts) {
            Date date = count.getDate();
            BlogEntry [] entries = getBlogEntriesOn(date, journal);
            for (BlogEntry entry : entries) {
                logger.debug(String.format("Writing entry: %s", entry.getItemid()));
                storeFullEntry(entry);
            }
        }

    }

    public void renderJournal() {
        logger.debug(String.format("Started render of journal: %s", journal));
        String dir;
        try {
            FileUtils.forceMkdir(new File(journal));
            logger.debug(String.format("Created directory %s", journal));
            FileUtils.copyFile(new File("src/main/java/ru/anglerhood/lj/client/render/static/lj.css"), new File(journal + "/lj.css"));
            dir = journal;
        } catch (IOException e) {
            logger.error(String.format("Couldn't create directory %s, %s", journal, e.getMessage()));
            throw new RuntimeException(e);
        }
        BlogEntryReader reader = new SQLiteReader(journal);
        while(reader.hasNext()){
            BlogEntry entry = reader.next();
            LJRenderer renderer = new HTMLRenderer(journal, reader);
            List<Comment> comments = reader.readComments(entry.getItemid());
            String output = renderer.renderFullEntry(entry, comments);
            String filename = dir + "/" + String.valueOf(entry.getPermalink().replaceAll("#/", ""));
            try {
                logger.debug(String.format("Writing file %s: ", filename));
                FileUtils.writeStringToFile(new File(filename), output);
            } catch (IOException e) {
                logger.error(String.format("Couldn't write file %s, %s", filename, e.getMessage()));
            }
        }

        writeIndex(dir);

        logger.debug(String.format("Ended render of journal: %s", journal));

    }

    public void writeIndex(String dir) {
        BlogEntryReader indexReader = new SQLiteReader(journal);
        HTMLRenderer renderer = new HTMLRenderer(journal, indexReader);
        String output = renderer.renderIndex();
        String filename = dir + "/index.html";
        try {
            logger.debug(String.format("Writing file %s: ", filename));
            FileUtils.writeStringToFile(new File(filename), output);
        } catch (IOException e) {
            logger.error(String.format("Couldn't write file %s, %s", filename, e.getMessage()));
        }
    }


    public String getUser() {
        return user;
    }

    private class Scrapper implements Callable<Boolean> {

        private final int entryId;

        Scrapper(int entryId){
            this.entryId = entryId;

        }

        @Override
        public Boolean call() throws Exception {
            logger.debug(String.format("Running Scrapper thread #%s",threadNumber++));
            BlogEntry entry = getBlogEntry(entryId, journal);
            if(null == entry) return false;
            commentCounter = commentCounter + entry.getReply_count();
            return storeFullEntry(entry);
        }
    }


}
