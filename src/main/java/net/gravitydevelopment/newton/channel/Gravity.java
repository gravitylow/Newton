package net.gravitydevelopment.newton.channel;

import net.gravitydevelopment.newton.Newton;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class Gravity extends NChannel {
    private static final String CHANNEL = "#gravity";
    private static final boolean STATUS_SERV = true;
    private static final boolean COMMANDS = true;

    private static final String[] op = new String[] {
        "h31ix"
    };    
    private static final String[] voice = new String[] {
        "mbaxter"
    };
    
    public Gravity(Newton newt) {
        super(newt, CHANNEL, voice, op, STATUS_SERV, COMMANDS);
    }
    
    @Override
    public void onChannelMessage(MessageEvent<PircBotX> event, User user, String command, String[] args) {
        if(command.equalsIgnoreCase(".version")) {
            for(String string : getNewton().getVersion().format()) {
                getChannel().sendMessage(string);
            }
        }        
    }    
}
