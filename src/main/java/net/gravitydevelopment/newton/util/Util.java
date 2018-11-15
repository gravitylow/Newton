package net.gravitydevelopment.newton.util;

import com.rosaloves.bitlyj.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    private static final Random RANDOM = new Random();
    
    /**
     * Convert a DBO size to number of bytes (stolen from Chekkit)
     * 
     * @param size size string
     * @return bytes
     */
    public static int sizeToBytes(String size) {
        // IEC denotes the i to show binary (2^x) sizes:
        // 1 KiB = 1024 bytes
        if(size.contains("KiB")) { // KibiBytes
            size = size.substring(0, size.indexOf("KiB") - 1);
            try {
                float f = Float.parseFloat(size);
                return (int) f * Constants.BYTES_IN_KIB;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        if(size.contains("MiB")) { // MebiBytes
            size = size.substring(0, size.indexOf("MiB") - 1);
            try {
                float f = Float.parseFloat(size);
                return (int) f * Constants.BYTES_IN_MIB;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        if(size.contains("GiB")) { // Gibibyte !?!?
            size = size.substring(0, size.indexOf("GiB") - 1);
            try {
                float f = Float.parseFloat(size);
                return (int) f * Constants.BYTES_IN_GIB;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

        }

        // Else, we're assuming *B*ytes

        if(!size.contains("B")) {
            return -1;
        }

        size = size.substring(0, size.indexOf("B") - 1);
        try {
            float f = Float.parseFloat(size);
            return (int) f;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }
    
    /**
     * Convert a number of bytes to DBO size
     * 
     * @param bytes bytes
     * @return size string
     */
    public static String bytesToSize(long bytes) {
        if(bytes > Constants.BYTES_IN_GIB) {
            return Math.round((bytes/Constants.BYTES_IN_GIB)) + " GiB";
        } else if(bytes > Constants.BYTES_IN_MIB) {
            return Math.round((bytes/Constants.BYTES_IN_MIB)) + " MiB";
        }  else if(bytes > Constants.BYTES_IN_KIB) {
            return Math.round((bytes/Constants.BYTES_IN_KIB)) + " KiB";
        } else {
            return bytes + " B";
        }
    }
    
    /**
     * Parse the date listed on a project submission into a Java Date
     * 
     * @param date the date scraped from the project
     * @return a parsed Date
     */
    public static Date parseProjectDate(String date) {
        try {          
            return new SimpleDateFormat("dd MMM yyyy kk:mm", Locale.ENGLISH).parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Parse an epoch time into a Java Date
     * 
     * @param date the date scraped from the file/report
     * @return a parsed Date
     */    
    public static Date parseEpochDate(long date) {
        return new Date(date * 1000);   
    }
    
    /**
     * Shorten a URL with bit.ly
     * 
     * @param url the url to shorten
     * @return a bit.ly link
     */
    public static String shorten(String url) {
        return Bitly.as(Constants.BITLY_USER, Constants.BITLY_KEY).call(Bitly.shorten(url)).getShortUrl();
    }
    
    /**
     * Get a random number between two values
     * 
     * @param i1 low limit
     * @param i2 high limit
     * @return a random number
     */
    public static int getRandom(int i1, int i2) {
        return RANDOM.nextInt(i2 - i1) + i1;
    }
    
    /**
     * Get a message to say goodbye
     * 
     * @return goodbye message
     */
    public static String getGoodbyeMessage() {
        return getRandomMessage(Constants.GOODBYE_MESSAGES);
    }
    
    /**
     * Get a message for waiting on something
     * 
     * @return waiting message
     */
    public static String getWaitMessage() {
        return getRandomMessage(Constants.WAIT_MESSAGES);
    }    
    
    /**
     * Get a rude permission message
     * 
     * @return permission message
     */
    public static String getPermissionMessage() {
        return getRandomMessage(Constants.PERMISSION_MESSAGES);
    }   
    
    /**
     * Get a "thank" message
     * 
     * @return praise message
     */
    public static String getPraiseMessage() {
        return getRandomMessage(Constants.PRAISE_MESSAGES);
    }
    
    /**
     * Get a message to ask someone to look at the queue
     * 
     * @return look message
     */
    public static String getLookMessage() {
        return getRandomMessage(Constants.LOOK_MESSAGES);
    } 
    
    /**
     * Get a random message from an array of messages
     * 
     * @param array array to use
     * @return random string from array
     */
    private static String getRandomMessage(String [] array) {
        return array[getRandom(0, array.length)];
    }

    /**
     * Concat an args array into a string
     * 
     * @param args array of strings
     * @param start starting point for concat
     * @return message
     */
    public static String argsToString(String [] args, int start) {
        String x = "";
        for(int i=start;i<args.length;i++) {
            x+=args[i];
            if(i < args.length-1) {
                x+=" ";
            }
        }
        return x;
    }

    /**
     * Grabs the key from a given value
     * @param map map to look in
     * @param value value to search for
     * @param <T> key type
     * @param <E> value type
     * @return key
     */
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Get a ping-safe name.
     * @param string user's name
     * @return non-pinging name
     */
    public static String getSafeName(String string) {
        StringBuilder nameBuilder = new StringBuilder();
        for(char c : string.toCharArray()) {
            nameBuilder.append(c);
            nameBuilder.append("\u200B");
        }
        return nameBuilder.toString();
    }
}
