package net.gravitydevelopment.newton.channel;

import net.gravitydevelopment.newton.Newton;
import net.gravitydevelopment.newton.dbo.QueueFile;
import net.gravitydevelopment.newton.dbo.QueueProject;
import net.gravitydevelopment.newton.util.Util;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class BukkitDevStaff extends NChannel {
    private User lastUser;
    private User lastPing;

    public BukkitDevStaff(Newton newt) {
        super(newt, "#bukkitdev-staff", null, null, false, false);
    }

    @Override
    public void onChannelMessage(MessageEvent<PircBotX> event, User user, String command, String[] args) {
        if(!getChannel().getOps().contains(user) && user != getBot().getUserBot()) {
            lastUser = user;
        }

        if(command.equalsIgnoreCase("!help") || command.equalsIgnoreCase("!h")) {
            user.sendMessage("+----------------+--------+--------+--------------------------------------+");
            user.sendMessage("| Command        | Alias  | Args   | Description                          |");
            user.sendMessage("+----------------+--------+--------+--------------------------------------+");
            user.sendMessage("| !queue         | !q     |        | Show queue stats                     |");
            user.sendMessage("| !fullqueue     | !fq    |        | Show full queue stats                |");
            user.sendMessage("| !projects      | !p     |        | Show project stats                   |");
            user.sendMessage("| !fullprojects  | !fp    |        | Show full project stats              |");
            user.sendMessage("| !files         | !f     |        | Show file stats                      |");
            user.sendMessage("| !fullfiles     | !ff    |        | Show full file stats                 |");
            user.sendMessage("| !reports       | !r     |        | Show report stats                    |");
            user.sendMessage("| !oldest        | !o     |        | Show oldest file info                |");
            user.sendMessage("| !size          | !s     |        | Show queue size                      |");
            user.sendMessage("| !sizeclaimed   | !sc    |        | Show claimed queue size              |");
            user.sendMessage("| !sizeunclaimed | !su    |        | Show unclaimed queue size            |");
            user.sendMessage("| !claimed       | !c     | [name] | Show claimed files/projects          |");
            user.sendMessage("| !whip          | !w     | [name] | Use the whip                         |");
            user.sendMessage("| !thank         | !t     | [name] | Thank someone for all their hardwork |");
            user.sendMessage("| !update        | !u     |        | Update stats                         |");
            user.sendMessage("| !help          | !h     |        | Show this information                |");
            user.sendMessage("| !botsnack      | !snack |        | Botsnack                             |");
            user.sendMessage("| !listclaimed   | !lc    |        | Lists users who've claimed files     |");
            user.sendMessage("| !stats         | !st    |        | Get link to approval stats           |");
            user.sendMessage("+----------------+--------+--------+--------------------------------------+");
        }
        else if(command.equalsIgnoreCase("!queue") || command.equalsIgnoreCase("!q")) {
            getChannel().sendMessage(getNewton().getDBO().formatQueue());
        } else if(command.equalsIgnoreCase("!projects") || command.equalsIgnoreCase("!p")) {
            getChannel().sendMessage(getNewton().getDBO().formatProjects());
        } else if(command.equalsIgnoreCase("!files") || command.equalsIgnoreCase("!f")) {
            getChannel().sendMessage(getNewton().getDBO().formatFiles());
        } else if(command.equalsIgnoreCase("!reports") || command.equalsIgnoreCase("!r")) {
            getChannel().sendMessage(getNewton().getDBO().formatReports());
        } else if(command.equalsIgnoreCase("!update") || command.equalsIgnoreCase("!u")) {
            getChannel().sendMessage(Util.getWaitMessage());
            getNewton().getDBO().parse();
            getChannel().sendMessage(getNewton().getDBO().formatQueue());
        } else if(command.equalsIgnoreCase("!fullqueue") || command.equalsIgnoreCase("!fq")) {
            for(String string : getNewton().getDBO().formatFullQueue()) {
                getChannel().sendMessage(string);
            }
        } else if(command.equalsIgnoreCase("!fullprojects") || command.equalsIgnoreCase("!fp")) {
            getChannel().sendMessage(getNewton().getDBO().formatFullProjects());
        } else if(command.equalsIgnoreCase("!fullfiles") || command.equalsIgnoreCase("!ff")) {
            getChannel().sendMessage(getNewton().getDBO().formatFullFiles());
        } else if(command.equalsIgnoreCase("!claimed") || command.equalsIgnoreCase("!c")) {
            getChannel().sendMessage(getNewton().getDBO().formatUnderReview(args.length == 1 ? args[0] : user.getNick()));
        } else if(command.equalsIgnoreCase("!whip") || command.equalsIgnoreCase("!w")) {
            if(args.length >= 1) {
                String x = "cracks the whip at " + args[0];
                if(args.length > 1) {
                    x+=" for "+Util.argsToString(args, 1);
                }
                getNewton().getBot().sendAction(getChannel(), x);
            } else {
                getNewton().getBot().sendAction(getChannel(), "cracks the whip");
            }
        } else if(command.equalsIgnoreCase("!thank") || command.equalsIgnoreCase("!t")) {
            String praise = Util.getPraiseMessage();
            if(args.length >= 1) {
                String x = praise + args[0];
                if(args.length > 1) {
                    x+=" for "+Util.argsToString(args, 1);
                }
                getNewton().getBot().sendAction(getChannel(), x);
            } else {
                getNewton().getBot().sendAction(getChannel(), praise + "everyone");
            }
        } else if(command.equalsIgnoreCase("!botsnack") || command.equalsIgnoreCase("!snack")) {
            getNewton().getBot().sendAction(getChannel(), "om nom nom nom!");
        }  else if(command.equalsIgnoreCase("!oldest") || command.equalsIgnoreCase("!o")) {
            QueueFile oldestFile = getNewton().getDBO().getOldestUnclaimedFile();
            QueueProject oldestProject = getNewton().getDBO().getOldestUnclaimedProject();

            getChannel().sendMessage("The oldest file awaiting review is " +
                    Colors.GREEN +
                    oldestFile.getAge() +
                    Colors.NORMAL +
                    " hours old: " +
                    oldestFile.getTitle() +
                    " (" +
                    oldestFile.getSizeString() +
                    ")");

            getChannel().sendMessage("The oldest project awaiting review is " +
                    Colors.GREEN +
                    oldestProject.getAge() +
                    Colors.NORMAL +
                    " hours old: " +
                    oldestProject.getTitle());
        } else if(command.equalsIgnoreCase("!size") || command.equalsIgnoreCase("!s")) {
            String size = Util.bytesToSize(getNewton().getDBO().getSize());
            getChannel().sendMessage("There are " +
                    Colors.GREEN +
                    getNewton().getDBO().getFiles() +
                    Colors.NORMAL +
                    " files in the queue with a total size of " +
                    size);
        } else if(command.equalsIgnoreCase("!sizeclaimed") || command.equalsIgnoreCase("!sc")) {
            String size = Util.bytesToSize(getNewton().getDBO().getClaimedSize());
            getChannel().sendMessage("There are " +
                    Colors.GREEN +
                    getNewton().getDBO().getFilesUnderReview() +
                    Colors.NORMAL +
                    " claimed files in the queue with a total size of " +
                    size);
        } else if(command.equalsIgnoreCase("!sizeunclaimed") || command.equalsIgnoreCase("!su")) {
            String size = Util.bytesToSize(getNewton().getDBO().getUnclaimedSize());
            getChannel().sendMessage("There are " +
                    Colors.GREEN +
                    (getNewton().getDBO().getFiles() - getNewton().getDBO().getFilesUnderReview()) +
                    Colors.NORMAL +
                    " unclaimed files in the queue with a total size of " +
                    size);
        } else if(command.equalsIgnoreCase("!listclaimed") || command.equalsIgnoreCase("!lc")) {
            for(String line : getNewton().getDBO().formatClaimedList()) {
                getBot().sendRawLine("PRIVMSG #bukkitdev-staff :"+line);
            }
        } else if(command.equalsIgnoreCase("!stats") || command.equalsIgnoreCase("!st")) {
            event.respond("Approval stats: http://bit.ly/1dH2auQ");
        }
    }

    public User getLastUser() {
        User user = lastUser;
        if(user == lastPing) {
            return null;
        } else {
            lastPing = lastUser;
            lastUser = null;
            return user;
        }
    }
}
