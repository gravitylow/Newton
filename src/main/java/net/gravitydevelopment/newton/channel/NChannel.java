package net.gravitydevelopment.newton.channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import net.gravitydevelopment.newton.Newton;
import net.gravitydevelopment.newton.util.Util;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;

public class NChannel extends ListenerAdapter<PircBotX> implements Listener<PircBotX> {
    private final Newton newt;
    private final Channel channel;
    private final String[] voice;
    private final String[] op;
    private final boolean statusServ;
    private final boolean commands;
    
    public NChannel(Newton newt, String channel, String[] voice, String[] op, boolean statusServ, boolean commands) {
        this.newt = newt;
        this.voice = voice;
        this.op = op;
        this.statusServ = statusServ;
        this.commands = commands;
        
        if(channel != null) {
            getBot().joinChannel(channel);
            this.channel = getBot().getChannel(channel);
            System.out.println("Joined channel "+channel);
        } else {
            this.channel = null;
        }
        getBot().getListenerManager().addListener(this);
    }
    
    @Override
    public void onJoin(JoinEvent<PircBotX> event) 
    {
        if(event.getChannel() == channel) {
            User user = event.getUser();
            if(statusServ) {
                if(isOp(user)) {
                    getBot().op(channel, user);
                }
                if(isVoice(user)) {
                    getBot().voice(channel, user);
                }       
            }
        }
    }
    
    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception
    {
        if(event.getChannel() == channel && !event.getUser().getLogin().equals("~newton")) {
            final String command = event.getMessage().split(" ")[0];
            String[] args = Arrays.copyOfRange(event.getMessage().split(" "), 1, event.getMessage().split(" ").length);

            final User user = event.getUser();
            if(commands) {
                if(command.equalsIgnoreCase(".time")) {
                    String time = new java.util.Date().toString();
                    event.respond("The time is "+Colors.BOLD+time);
                } else if(command.equalsIgnoreCase(".kick")) {
                    if(isVoice(user) || isOp(user)) {
                        if(args.length > 0) {
                            User target = getBot().getUser(args[0]);
                            if(user != null) {
                                if(args.length == 2) {
                                    getBot().kick(channel, target, args[1]);
                                } else {
                                    getBot().kick(channel, target);
                                }
                            } else {
                                event.respond("User not found.");
                            }
                        } else {
                            event.respond("Usage: .kick <user> [reason]");
                        }
                    } else {
                        event.respond(Util.getPermissionMessage());
                    }
                } else if(command.equalsIgnoreCase(".ban")) {
                    if(isOp(user)) {
                        if(args.length > 0) {
                            User target = getBot().getUser(args[0]);
                            if(user != null) {
                                getBot().ban(channel, target.getHostmask());
                                if(args.length == 2) {
                                    getBot().kick(channel, target, args[1]);
                                } else {
                                    getBot().kick(channel, target);
                                }
                            } else {
                                event.respond("User not found.");
                            }
                        } else {
                            event.respond("Usage: .ban <user> [reason]");
                        }
                    } else {
                        event.respond(Util.getPermissionMessage());
                    }
                } else if(command.equalsIgnoreCase(".part")) {
                    if(isOp(user)) {
                        event.respond(Util.getGoodbyeMessage());
                        getBot().partChannel(channel);
                        System.out.println("Parted "+channel);
                    } else {
                        event.respond(Util.getPermissionMessage());
                    }
                } else if(command.equalsIgnoreCase(".die")) {
                    if(isOp(user)) {
                        event.respond(Util.getGoodbyeMessage());
                        getBot().disconnect();
                        getBot().shutdown();
                        System.out.println("Shutting down...");
                    } else {
                        event.respond(Util.getPermissionMessage());
                    }
                } else if(command.equalsIgnoreCase(".botsnack")) {
                    event.respond("om nom nom nom!");
                }
            }
            onChannelMessage(event, user, command, args);
        }
    }
    
    public void onChannelMessage(MessageEvent<PircBotX> event, User user, String command, String[] args) {
        //
    }
    
    public boolean onRegex(User user, String message, String regex) {
        return true;
    }
    
    public PircBotX getBot() {
        return newt.getBot();
    }
    
    public Newton getNewton() {
        return newt;
    }
    
    public boolean isOp(String name) {
        for(String string : op) {
            if(string.equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isOp(User user) {
        return isOp(user.getLogin());
    }
    
    public boolean isVoice(String name) {
        for(String string : voice) {
            if(string.equals(name)) {
                return true;
            }
        }
        return false;
    }  
    
    public boolean isVoice(User user) {
        return isVoice(user.getLogin());
    }
    
    public Channel getChannel() {
        return channel;
    }
}
