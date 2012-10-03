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

package ru.anglerhood.lj.api.xmlrpc.arguments;

/**
 * Created with IntelliJ IDEA.
 * User: anglerhood
 * Date: 10/3/12
 * Time: 12:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class GetCommentsArgument extends BaseArgument {

    public void setDItemId(Integer id) {
        struct.put("ditemid", id);
    }

    public void setItemId(String id) {
        struct.put("itemid", id);
    }

    public void setPage(String page) {
        struct.put("page", page);
    }

    public void setPageSize(String pageSize) {
        struct.put("page_size", pageSize);
    }

    public void setJournal(String journal) {
        struct.put("journal", journal);
    }


    /*
        Possible values thread, list
     */
    public void setFormat(String format) {
        struct.put("format", format);
    }
    /*
        Expand strategy for comment threads
        Possible values mobile, mobile_thread, expand_all, by_level, detailed, default
     */
    public void setExpandStrategy(String strat) {
        struct.put("expand_strategy", strat);
    }

    /*
        Endings for lines.
        Possible values unix,mac, space, dots
        Default value: pc
     */

    public void setLineEndings(String lineendings) {
        struct.put("lineenings", lineendings);
    }
}
