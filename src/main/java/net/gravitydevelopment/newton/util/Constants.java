package net.gravitydevelopment.newton.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String SERVER_PASSWORD = System.getenv("SERVER_PASSWORD");
    public static final String SERVER_ADDRESS = System.getenv("SERVER_ADDRESS");
    public static final int SERVER_PORT = Integer.parseInt(System.getenv("SERVER_PORT"));
    public static final String SERVER_LOGIN = System.getenv("SERVER_LOGIN");
    
    public static final String NICKSERV_NAME = System.getenv("NICKSERV_NAME");
    public static final String NICKSERV_PASSWORD = System.getenv("NICKSERV_PASSWORD");
    
    public static final String BITLY_USER = System.getenv("BITLY_USER");
    public static final String BITLY_KEY = System.getenv("BITLY_KEY");    
    
    public static final String BUKKITDEV_API_KEY = System.getenv("BUKKITDEV_API_KEY");
    
    // 
    
    public static final String BUKKITDEV_QUEUE_URL = "http://dev.bukkit.org/admin/approval-queue/";
    public static final String BUKKITDEV_REPORTS_URL = "http://dev.bukkit.org/admin/reports/";
    
    public static final int BYTES_IN_GIB = 1073741824;
    public static final int BYTES_IN_MIB = 1048576;
    public static final int BYTES_IN_KIB = 1024;
    
    public static final String[] GOODBYE_MESSAGES = new String[] {
        "See you later!",
        "I'll be right back!",
        "I'm late for my flight!",
        "I have a train to catch!",
        "Catch you on the flip side!",
        "Adios!",
        "See you later!",
        "Sayonara!",
        "It's been real!",
        "Until next time!"
    };
    
    public static final String[] PERMISSION_MESSAGES = new String[] {
        "You can't do that...",
        "I don't know who you are, sorry!",
        "And you are...?",
        "Huh?",
        "Just who do you think you are?",
        "How about no.",
        "What's your name again?",
        "Shock me, say something intelligent.",
        "I'd like to help you out. Which way did you come in?",
        "That was strange, I thought I heard a little buzzing noise..",
        "Yeeeaaaahhhhh no."
    };    
    
    public static final String[] WAIT_MESSAGES = new String[] {
        "Just a moment...",
        "Just one second...",
        "Hold on...",
        "On it...",
        "Wait for it...",
        "You betcha...",
        "Sure thing...",
        "My life for Aiur...",
        "Aak! Ya scared me...",
        "Big job, huh...",
        "In the rear with the gear...",
        "Ready to serve...",
        "My lord...",
        "Job's done...",
        "Leave me alone...",
        "I don't want to do that...",
        "I'm not listening...",
        "At once, sire...",
        "You played the shareware, now buy the game...",
        "I have work to do...",
        "As you wish...",
        "Move out...",
        "Time is of the essence..."
    }; 
    
    public static final String[] PRAISE_MESSAGES = new String[] {
        "high-fives ",
        "does a little dance with ",
        "hugs ",
        "gives a cookie to ",
        "gives a gold star to "
    };   
    
    public static final String[] LOOK_MESSAGES = new String[] {
        "could you help out with that?",
        "mind working on that?",
        "do you have some time for the queue?",
        "have you heard about our lord and savior, the queue?",
        "would you mind?",
        "could you take a look?"
    };     
    
    public static final Map<String, String> BUKKITDEV_NAME = new HashMap<String, String>();
    
    static {
        BUKKITDEV_NAME.put("tnt", "TnTBass");
        BUKKITDEV_NAME.put("t00thpick1", "toothplck1");
        BUKKITDEV_NAME.put("eviltechie", "PlantAssassin");
        BUKKITDEV_NAME.put("milkywayz", "imilkywayz");
        BUKKITDEV_NAME.put("deathmarine", "Death_marine");
        BUKKITDEV_NAME.put("jacek", "wide_load");
        BUKKITDEV_NAME.put("gravity", "gravity_low");
        BUKKITDEV_NAME.put("gj", "gmcferrin");
        BUKKITDEV_NAME.put("zarius", "ZariusT");
        BUKKITDEV_NAME.put("huskehhh", "_Husky_");
        BUKKITDEV_NAME.put("recon", "recon88");
        BUKKITDEV_NAME.put("squidicuz", "squidicc");
        BUKKITDEV_NAME.put("rogue_", "1rogue");
        BUKKITDEV_NAME.put("auke", "s0f4r");
    }
    
    public static final String[] PM_USERS = new String[] {
        "~gravity",
        "~tnt"
    };
}
