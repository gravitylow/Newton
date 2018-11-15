package net.gravitydevelopment.newton.channel;

import net.gravitydevelopment.newton.Newton;
import net.gravitydevelopment.newton.util.Constants;
import net.gravitydevelopment.newton.util.Util;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class PrivateMessages extends NChannel {
    public PrivateMessages(Newton newt) {
        super(newt, null, null, null, false, false);
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception {
        User user = event.getUser();
        boolean permission = false;
        for(String string : Constants.PM_USERS) {
            if(user.getLogin().toLowerCase().equalsIgnoreCase(string)) {
                permission = true;
                break;
            }
        }
        if(permission) {
            final String command = event.getMessage().split(" ")[0];
            final String[] args = event.getMessage().replaceAll(command+" ", "").split(" ");
            if(command.equalsIgnoreCase("join")) {
                if(args.length == 1) {
                    getBot().joinChannel(args[0]);
                    event.respond("Joined "+args[0]);
                } else {
                    event.respond("Usage: join <#channel>");
                }
            } else if(command.equalsIgnoreCase("part")) {
                if(args.length == 1) {
                    getBot().partChannel(getBot().getChannel(args[0]));
                    event.respond("Parted "+args[0]);
                } else {
                    event.respond("Usage: part <#channel>");
                }
            } else if(command.equalsIgnoreCase("say")) {
                if(args.length >= 2) {
                    if(getBot().getChannelsNames().contains(args[0])) {
                        getBot().getChannel(args[0]).sendMessage(Util.argsToString(args, 1));
                        event.respond("Message sent to "+args[0]);
                    } else {
                        event.respond("I'm not in "+args[0]);
                    }
                } else {
                    event.respond("Usage: say <#channel> <msg>");
                }
            } else if(command.equalsIgnoreCase("die")) {
                event.respond(Util.getGoodbyeMessage());
                getBot().disconnect();
                getBot().shutdown();
                System.out.println("Shutting down...");
            }else if(command.equalsIgnoreCase("botsnack")) {
                event.respond("om nom nom nom!");
            } else if(command.equalsIgnoreCase("help")) {
                event.respond("Command list:");
                event.respond("join <#channel>      | join a channel");
                event.respond("part <#channel>      | part a channel");
                event.respond("say <#channel> <msg> | speak");
                event.respond("botsnack             | botsnack");
                event.respond("die                  | disconnect from the server");
            } else {
                event.respond("I didn't catch that. Try \"help\"");
            }
        } else {
            event.respond(Util.getPermissionMessage());
            getBot().getUser("Gravity").sendMessage(user.getLogin() + " tried to use PM command: " + event.getMessage());
        }
    }
}
