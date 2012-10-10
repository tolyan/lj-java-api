package ru.anglerhood.lj.client.pictures;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.anglerhood.lj.api.LJHelpers;
import sun.misc.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;



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
public class PictureStorage {
    private static Log logger = LogFactory.getLog(PictureStorage.class);

    /**
     * Downloads picture from  URL, detects format and stores on filesystem under the name of MD5 hashsum.
      * @param url
     * @throws IOException
     */
    public void storePicture(URL url) throws IOException {
        byte [] image = bufferImage(url);
        String filename = getFilename(image, url);
        filename = filename.toLowerCase();
        logger.debug(String.format("Writing %s file", filename));
        org.apache.commons.io.IOUtils.write(image, new FileOutputStream(new File(filename)));
    }


    private byte[] bufferImage(URL url) throws IOException {
        logger.debug(String.format("Processing URL: %s", url.toString()));
        InputStream is = url.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte [] buffer = new byte[1024];
        int i = is.read(buffer);
        while(i != -1) {
            out.write(buffer, 0, i);
            i = is.read(buffer);
        }
        is.close();
        byte [] result = out.toByteArray();
        out.close();
        return result;
    }

    private String getFilename(byte [] image, URL url) throws IOException {
        ImageInputStream imageStream = new MemoryCacheImageInputStream(new ByteArrayInputStream(image));
        Iterator<ImageReader> it = ImageIO.getImageReaders(imageStream);
        String format = "";
        while(it.hasNext()) {
            ImageReader reader = it.next();
            format = reader.getFormatName();
            logger.debug(String.format("Found picture with %s format", format));
        }

        if (format.equals("")){
            logger.warn(String.format("Got picture of unknown format from URL: %s", url.toString()));
            format = "unknown";
        }

        String hashType = "MD5";
        String hash = "";
        try {
            hash = getHash(image, hashType);
        }  catch (NoSuchAlgorithmException e) {
            logger.error(String.format("Unknown hashing algorithm %s", hashType));
        }
        return hash+ "." + format;
    }

    private String getHash(byte [] image, String hashType) throws NoSuchAlgorithmException {
        final java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
        byte [] digest = md.digest(image);
        return LJHelpers.bytesToHex(digest);
    }



}
