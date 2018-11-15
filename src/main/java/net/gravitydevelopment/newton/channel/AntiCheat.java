package net.gravitydevelopment.newton.channel;

import net.gravitydevelopment.newton.Newton;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class AntiCheat extends NChannel {
    private static final String CHANNEL = "#anticheat";
    private static final boolean STATUS_SERV = true;
    private static final boolean COMMANDS = true;
    
    private static final String TOPIC_DETAIL = " | Dev builds: http://ci.h31ix.net | Bug reporting: http://goo.gl/Q7EQc | If you're interested in helping with quality tests, contact @TnT";
    
    private static final String[] op = new String[] {
        "h31ix",
        "TnT",
        "SuPaHsPii"
    };    
    private static final String[] voice = new String[] {
        "mbaxter"
    };
    
    public AntiCheat(Newton newt) {
        super(newt, CHANNEL, voice, op, STATUS_SERV, COMMANDS);
    }
    
    @Override
    public void onChannelMessage(MessageEvent<PircBotX> event, User user, String command, String[] args) {
        if(command.equalsIgnoreCase(".version")) {
            if(args.length == 0) {
                for(String string : getNewton().getVersion().format()) {
                    getChannel().sendMessage(string);
                }
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("set") && isOp(user)) {
                    updateVersion(getNewton().getVersion().getVersion(), args[1], "http://goo.gl/rGRSd");
                }
            }
        }        
    }
    
    public void updateVersion(String old, String version, String url) {
        String topic = "AntiCheat latest version - " + version + " " + url + TOPIC_DETAIL;
        this.getBot().setTopic(getChannel(), topic);
        
        getChannel().sendMessage("AntiCheat Update: " + old + " --> " + Colors.GREEN + version);
        getChannel().sendMessage("Download: " + url);
    }
}
