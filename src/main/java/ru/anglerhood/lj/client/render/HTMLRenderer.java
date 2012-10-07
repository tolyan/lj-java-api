package ru.anglerhood.lj.client.render;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.w3c.dom.DocumentType;
import ru.anglerhood.lj.api.xmlrpc.results.BlogEntry;
import ru.anglerhood.lj.api.xmlrpc.results.Comment;

import javax.management.relation.RelationNotFoundException;
import java.io.StringWriter;
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
public class HTMLRenderer implements LJRenderer {
    private static final Log logger = LogFactory.getLog(HTMLRenderer.class);

    private static final String DOCTYPE = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN http://www.w3.org/TR/html4/loose.dtd\">";
    private final VelocityEngine ve;

    public HTMLRenderer(){
        ve = new VelocityEngine();
        ve.setProperty("resource.loader", "file");
        ve.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        ve.setProperty("file.resource.loader.path","./src/main/java/ru/anglerhood/lj/client/render/templates");
        ve.setProperty("file.resource.loader.cache", "true");
        ve.init();
    }


    @Override
    public String renderBlogEntry(BlogEntry entry) {
        VelocityContext entryContext = new VelocityContext();
        entryContext.put("entry", entry);
        Template template = loadTemplate("full_entry.vm");

        StringWriter writer = new StringWriter();
        template.merge(entryContext, writer);
        return writer.toString();
    }

    @Override
    public String renderComments(List<Comment> comments) {
        VelocityContext commentsContext = new VelocityContext();
        commentsContext.put("commentList", comments);
        StringWriter writer = new StringWriter();
        Template template = loadTemplate("comments.vm");
        template.merge(commentsContext, writer);
        return writer.toString();
    }

    @Override
    public String renderFullEntry(BlogEntry entry, List<Comment> comments) {
        VelocityContext fullContext = new VelocityContext();
        fullContext.put("entry", entry);
        fullContext.put("commentList", comments);
        StringWriter writer = new StringWriter();
        Template template = loadTemplate("full_entry.vm");
        template.merge(fullContext, writer);
        return writer.toString();

    }

    private Template loadTemplate(String template) {
        Template result = null;
        try{
            result = ve.getTemplate(template);
        } catch(ResourceNotFoundException e) {
            logger.error(String.format("Coudn't find template: %s", e.getMessage()));
        } catch(ParseErrorException e) {
            logger.error(String.format("Cound't parse template: %s", e.getMessage()));
        } catch(MethodInvocationException e) {
            logger.error(String.format("Error in invocation: %s", e.getMessage()));
        }
        return result;
    }
}
