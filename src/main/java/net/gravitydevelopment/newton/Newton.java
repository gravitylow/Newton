package net.gravitydevelopment.newton;

import net.gravitydevelopment.newton.channel.AntiCheat;
import net.gravitydevelopment.newton.channel.BukkitDevStaff;
import net.gravitydevelopment.newton.channel.Gravity;
import net.gravitydevelopment.newton.channel.NChannel;
import net.gravitydevelopment.newton.channel.PrivateMessages;
import net.gravitydevelopment.newton.util.BukkitDev;
import net.gravitydevelopment.newton.util.Constants;
import net.gravitydevelopment.newton.util.Version;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;

import java.io.UnsupportedEncodingException;

public class Newton {
    private final PircBotX bot = new PircBotX();
    private NChannel[] channels;
    private Version version;
    private BukkitDev dbo;
    
    public Newton() {
        connect();
    }
    
    private void connect() {
        System.out.println("Connecting...");
        
        bot.setName(Constants.NICKSERV_NAME);
        bot.identify(Constants.NICKSERV_PASSWORD);
        bot.setLogin(Constants.SERVER_LOGIN);
        try {
            bot.setEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            bot.connect(
                Constants.SERVER_ADDRESS, 
                Constants.SERVER_PORT, 
                Constants.SERVER_PASSWORD, 
                new UtilSSLSocketFactory().trustAllCertificates()
            );
        } catch (Exception ex) {
        }
        
        System.out.println("Connected!");
        
        channels = new NChannel[] {
            new AntiCheat(this), 
            new Gravity(this),
            new PrivateMessages(this),
            new BukkitDevStaff(this),
        };
        
        System.out.println("Loading scrapers...");
        
        version = new Version(this);
        
        dbo = new BukkitDev(this);
        
        System.out.println("Finished loading!");
    }
    
    public NChannel[] getChannels() {
        return channels;
    }
    
    public PircBotX getBot() {
        return bot;
    }
    
    public Version getVersion() {
        return version;
    }
    
    public BukkitDev getDBO() {
        return dbo;
    }
}
