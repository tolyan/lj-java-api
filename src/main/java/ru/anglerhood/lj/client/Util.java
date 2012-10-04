package ru.anglerhood.lj.client;

/**
 * Created with IntelliJ IDEA.
 * User: anglerhood
 * Date: 10/4/12
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    public static String nullString(Object obj) {
        if (obj == null)
            return "";
        else return obj.toString();
    }
}
