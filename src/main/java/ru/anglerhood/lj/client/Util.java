package ru.anglerhood.lj.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: anglerhood
 * Date: 10/4/12
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    //see http://stackoverflow.com/a/4731164/600464
    private static final String whitespace_chars =  ""       /* dummy empty string for homogeneity */
            + "\\u0009" // CHARACTER TABULATION
            + "\\u000A" // LINE FEED (LF)
            + "\\u000B" // LINE TABULATION
            + "\\u000C" // FORM FEED (FF)
            + "\\u000D" // CARRIAGE RETURN (CR)
            + "\\u0020" // SPACE
            + "\\u0085" // NEXT LINE (NEL)
            + "\\u00A0" // NO-BREAK SPACE
            + "\\u1680" // OGHAM SPACE MARK
            + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
            + "\\u2000" // EN QUAD
            + "\\u2001" // EM QUAD
            + "\\u2002" // EN SPACE
            + "\\u2003" // EM SPACE
            + "\\u2004" // THREE-PER-EM SPACE
            + "\\u2005" // FOUR-PER-EM SPACE
            + "\\u2006" // SIX-PER-EM SPACE
            + "\\u2007" // FIGURE SPACE
            + "\\u2008" // PUNCTUATION SPACE
            + "\\u2009" // THIN SPACE
            + "\\u200A" // HAIR SPACE
            + "\\u2028" // LINE SEPARATOR
            + "\\u2029" // PARAGRAPH SEPARATOR
            + "\\u202F" // NARROW NO-BREAK SPACE
            + "\\u205F" // MEDIUM MATHEMATICAL SPACE
            + "\\u3000" // IDEOGRAPHIC SPACE
            ;
    /* A \s that actually works for Java’s native character set: Unicode */
    private static final String     whitespace_charclass = "["  + whitespace_chars + "]";
    /* A \S that actually works for  Java’s native character set: Unicode */
    private static final String not_whitespace_charclass = "[^" + whitespace_chars + "]";



    public static String nullString(Object obj) {
        if (obj == null)
            return "";
        else return obj.toString();
    }

    public static String toHTML(String input) {
        if (input == null) return null;
        StringBuilder result = new StringBuilder();
        String body = input.replaceAll("\\n", "\n<br>");
        Pattern pattern = Pattern.compile("<lj user=\"([\\w]+)\">|<lj user=([\\w]+)>");
        Matcher matcher = pattern.matcher(body);
        int end = 0;
        while(matcher.find()) {
            String ljuser = null != matcher.group(1)? matcher.group(1):matcher.group(2);
            result.append(body.substring(end, matcher.start()));
            result.append("<span class=\"ljuser\">");
            result.append(ljuser);
            result.append("</span>");
            end = matcher.end();
        }
        result.append(body.substring(end));

        pattern = Pattern.compile("<lj comm=\"([\\w]+)\">");
        matcher = pattern.matcher(result);
        while(matcher.find()) {
            result.replace(matcher.start(), matcher.end(), "<span class=\"ljcomm\">" + matcher.group(1) + "</span>");
        }
        return result.toString();
    }

    public static String replaceJournalLinks(String input, String journal){
        if (input == null) return null;

        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("a href=\"http://" + journal + "\\.livejournal\\.com/([\\d\\w.]+)\"");
        Matcher matcher = pattern.matcher(input);
        int end = 0;
        while(matcher.find()) {
            String href = matcher.group(1).replaceAll("\\?thread=\\d+", "").replaceAll("\\?view=\\d+", "");
            result.append(input.substring(end, matcher.start()));
            result.append("<a href=\"").append(href).append("\"");
            end = matcher.end();
        }
        result.append(input.substring(end));

        String temp = result.toString();
        result = new StringBuilder();

        pattern = Pattern.compile("a href=\"http://community\\.livejournal\\.com/" + journal + "/([\\w.?=#]+)\"");
        matcher = pattern.matcher(temp);
        end = 0;
        while(matcher.find()) {
            String href = matcher.group(1).replaceAll("\\?thread=\\d+", "").replaceAll("\\?view=\\d+", "");
            result.append(temp.substring(end, matcher.start()));
            result.append("<a href=\"");
            result.append(href);
            result.append("\"");
            end = matcher.end();
        }
        result.append(temp.substring(end));

        temp = result.toString();
        result = new StringBuilder();

        pattern = Pattern.compile("http://" + journal + ".livejournal.com/([\\w.?=#]+)");
        matcher = pattern.matcher(temp);
        end = 0;
        while(matcher.find()) {
            String href = matcher.group(1).replaceAll("\\?thread=\\d+", "").replaceAll("\\?view=\\d+", "");
            result.append(temp.substring(end, matcher.start()));
            result.append("<a href=\"");
            result.append(href);
            result.append("\">");
            result.append(href);
            result.append("</a>");
            end = matcher.end();
        }
        result.append(temp.substring(end));

        return result.toString();
    }



}
