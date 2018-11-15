package net.gravitydevelopment.newton.util;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.gravitydevelopment.newton.Newton;
import net.gravitydevelopment.newton.channel.BukkitDevStaff;
import net.gravitydevelopment.newton.dbo.QueueFile;
import net.gravitydevelopment.newton.dbo.QueueObject;
import net.gravitydevelopment.newton.dbo.QueueProject;
import net.gravitydevelopment.newton.dbo.QueueReport;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

public class BukkitDev {
    private List<QueueFile> files = new ArrayList<QueueFile>();
    private List<QueueProject> projects = new ArrayList<QueueProject>();
    private List<QueueReport> reports = new ArrayList<QueueReport>();
    private BukkitDevStaff channel;

    private final Newton newt;

    public BukkitDev(final Newton newt) {
        this.newt = newt;
        channel = ((BukkitDevStaff)newt.getChannels()[3]);

        parse();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(15 * 60 * 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Version.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // Keep report list for comparison
                    List<QueueReport> reportsTemp = reports;

                    parse();
                    if(getProjects()-getProjectsUnderReview() > 25 || getFiles()-getFilesUnderReview() > 100) {
                        User user = channel.getLastUser();
                        if(user != null) {
                           newt.getBot().sendMessage(channel.getChannel(), formatQueue());
                        }
                    }

                    for(QueueReport r1 : reports) {
                        boolean has = false;
                        for(QueueReport r2 : reportsTemp) {
                            if(r1.getUrl().equals(r2.getUrl())) {
                                has = true;
                                break;
                            }
                        }
                        if(!has) {
                            newt.getBot().sendMessage(channel.getChannel(), formatReport(r1));
                        }
                    }
                }
            }
        };

        new Thread(run).start();
    }

    public String formatReport(QueueReport report) {
        return
            report.getUser() +
                " filed a report on " +
                report.getTarget() +
                " for " +
                report.getCategory() +
                ". " +
                Util.shorten(report.getUrl());
    }

    public String formatQueue() {
        return
            formatProjects() +
                " " +
                formatFiles() +
                " Projects: http://bit.ly/1mZMF8j Files: http://bit.ly/Ms4xBW";
    }

    public String[] formatFullQueue() {
        return new String[] {
            formatFullProjects(),

            formatFullFiles(),

            "http://bit.ly/Ms4xBW"
        };
    }

    public String formatFullProjects() {
        return
            "There are " +
                Colors.GREEN +
                getProjects() +
                Colors.NORMAL +
                " projects in the queue. There are " +
                Colors.GREEN +
                getProjectsUnderReview() +
                Colors.NORMAL +
                " projects under review, and " +
                Colors.RED +
                (getProjects()-getProjectsUnderReview()) +
                Colors.NORMAL +
                " unclaimed projects.";
    }

    public String formatFullFiles() {
        return
            "There are " +
                Colors.GREEN +
                getFiles() +
                Colors.NORMAL +
                " files in the queue. There are " +
                Colors.GREEN +
                getFilesUnderReview() +
                Colors.NORMAL +
                " files under review, and " +
                Colors.RED +
                (getFiles()-getFilesUnderReview()) +
                Colors.NORMAL +
                " unclaimed files.";
    }

    public String formatReports() {
        return
            "There are " +
                Colors.GREEN +
                getReports() +
                Colors.NORMAL +
                " unclaimed reports - http://bit.ly/15hyyDc";
    }

    public String formatProjects() {
        return
            "There are " +
                Colors.GREEN +
                (getProjects()-getProjectsUnderReview()) +
                Colors.NORMAL +
                " unclaimed projects in the queue.";
    }

    public String formatFiles() {
        return
            "There are " +
                Colors.GREEN +
                (getFiles()-getFilesUnderReview()) +
                Colors.NORMAL +
                " unclaimed files in the queue.";
    }

    public String[] formatClaimedList() {
        HashMap<String, Integer> projectsList = new HashMap<String, Integer>();
        HashMap<String, Integer> filesList = new HashMap<String, Integer>();
        HashMap<String, Integer> fileSizes = new HashMap<String, Integer>();
        for(QueueProject p : projects) {
            if(p.isUnderReview()) {
                int amount = projectsList.containsKey(p.getReviewer()) ? (projectsList.get(p.getReviewer()) + 1) : 1;
                projectsList.put(p.getReviewer(), amount);
            }
        }

        for(QueueFile f : files) {
            if(f.isUnderReview()) {
                int amount = filesList.containsKey(f.getReviewer()) ? (filesList.get(f.getReviewer()) + 1) : 1;
                int totalSize = fileSizes.containsKey(f.getReviewer()) ? (fileSizes.get(f.getReviewer()) + f.getBytes()) : f.getBytes();
                filesList.put(f.getReviewer(), amount);
                fileSizes.put(f.getReviewer(), totalSize);
            }
        }

        HashSet<String> names = new HashSet<String>();
        names.addAll(projectsList.keySet());
        names.addAll(filesList.keySet());
        String[] lines = new String[names.size()];
        int i = 0;
        for(String name : names) {
            int projects = (projectsList.containsKey(name)) ? projectsList.get(name) : 0;
            int files = (filesList.containsKey(name)) ? filesList.get(name) : 0;
            int bytes = (fileSizes.containsKey(name)) ? fileSizes.get(name) : 0;

            if(Constants.BUKKITDEV_NAME.containsValue(name)) { //special users
                String ircName = Util.getKeyByValue(Constants.BUKKITDEV_NAME, name);
                lines[i] = Util.getSafeName(name) + " (aka " + Util.getSafeName(ircName) + ") has claimed " + formatNumberItems("project", projects) + " and " + formatNumberItems("file", files) + " (" + Util.bytesToSize(bytes) + ")";
            } else {
                lines[i] = Util.getSafeName(name) + " has claimed " + formatNumberItems("project", projects) + " and " + formatNumberItems("file", files) + " (" + Util.bytesToSize(bytes) + ")";
            }
            ++i;
        }
        return lines;

    }

    private String formatNumberItems(String type, int number) {
        if(number == 0) {
            return Colors.RED + "0 " + Colors.NORMAL + type + "s";
        } else if(number == 1) {
            return Colors.GREEN + "1 " + Colors.NORMAL + type;
        } else {
            return Colors.GREEN + number + " " + Colors.NORMAL + type + "s";
        }
    }

    public String formatUnderReview(String name) {
        name = name.toLowerCase();
        if(Constants.BUKKITDEV_NAME.containsKey(name)) {
            name = Constants.BUKKITDEV_NAME.get(name);
        }

        int proj = 0;
        int file = 0;
        long size = 0;

        for(QueueProject p : projects) {
            if(p.isUnderReview() && p.getReviewer().equalsIgnoreCase(name)) {
                proj++;
            }
        }

        for(QueueFile f : files) {
            if(f.isUnderReview() && f.getReviewer().equalsIgnoreCase(name)) {
                file++;
                size+=f.getBytes();
            }
        }

        return
            name +
                " has claimed " +
                Colors.GREEN +
                proj +
                Colors.NORMAL +
                " projects and " +
                Colors.GREEN +
                file +
                Colors.NORMAL +
                " files (total size = " +
                Util.bytesToSize(size) +
                ").";
    }

    public int getProjects() {
        return projects.size();
    }

    public int getFiles() {
        return files.size();
    }

    public int getReports() {
        return reports.size();
    }

    public int getProjectsUnderReview() {
        int i = 0;
        for(QueueProject p : projects) {
            if(p.isUnderReview()) {
                i++;
            }
        }
        return i;
    }

    public int getFilesUnderReview() {
        int i = 0;
        for(QueueFile f : files) {
            if(f.isUnderReview()) {
                i++;
            }
        }
        return i;
    }

    public QueueFile getOldestUnclaimedFile() {
        return (QueueFile)getOldestUnclaimedObject(files);
    }

    public QueueProject getOldestUnclaimedProject() {
        return (QueueProject)getOldestUnclaimedObject(projects);
    }

    public QueueReport getOldestUnclaimedReport() {
        return (QueueReport)getOldestUnclaimedObject(reports);
    }

    public QueueObject getOldestUnclaimedObject(List<? extends QueueObject> objects) {
        QueueObject oldest = null;
        for(QueueObject object : objects) {
            if(!object.isUnderReview()) {
                if(oldest == null || object.getAge() > oldest.getAge()) {
                    oldest = object;
                }
            }
        }
        return oldest;
    }

    public long getSize() {
        long s = 0;
        for(QueueFile file : files) {
            s+=file.getBytes();
        }
        return s;
    }

    public long getClaimedSize() {
        long s = 0;
        for(QueueFile file : files) {
            if(file.isUnderReview()) {
                s+=file.getBytes();
            }
        }
        return s;
    }

    public long getUnclaimedSize() {
        long s = 0;
        for(QueueFile file : files) {
            if(!file.isUnderReview()) {
                s+=file.getBytes();
            }
        }
        return s;
    }

    public void parse() {
        parseQueue();
        parseReports();
    }

    public void parseQueue() {
        List<QueueFile> filesTemp = new ArrayList<QueueFile>();
        List<QueueProject> projectsTemp = new ArrayList<QueueProject>();

        try {
            Connection c = Jsoup.connect(Constants.BUKKITDEV_QUEUE_URL + "?api-key=" + Constants.BUKKITDEV_API_KEY);
            c.timeout(180000);
            c.maxBodySize(5242880);
            Document doc1 = c.get();

            // Parse files
            Element filesTable = doc1.getElementById("files");
            if(filesTable == null) {
                files = filesTemp;
                projects = projectsTemp;
                return;
            }

            filesTable = filesTable.getElementsByTag("tbody").get(0);
            Elements e = filesTable.getElementsByClass("row-joined-to-next");
            if(e.isEmpty()) {
                files = filesTemp;
                projects = projectsTemp;
                return;
            }
            for(Element file : e) {
                Elements infoBlocks = file.getElementsByTag("td");
                if(infoBlocks.size() != 7) {
                    throw new Exception("Wrong number of info blocks.");
                }

                int fileId = Integer.parseInt(infoBlocks.get(0).child(0).getAllElements().get(0).attr("value"));

                String projectName = infoBlocks.get(1).text();
                String projectURL = infoBlocks.get(1).child(0).attr("href");
                String fileTitle = infoBlocks.get(2).child(0).text();
                String filePageURL = "http://dev.bukkit.org" + infoBlocks.get(2).child(0).attr("href");
                String fileDirectLink = infoBlocks.get(3).child(0).attr("href");
                String uploader = infoBlocks.get(5).text().trim();
                long date = Long.parseLong(infoBlocks.get(6).child(0).attr("data-epoch"));
                String claimed = infoBlocks.get(3).text();
                String size = infoBlocks.get(4).text().trim();

                if(!claimed.contains("(Under Review")) {
                    claimed = null;
                } else {
                    claimed = claimed.substring(claimed.indexOf("(Under Review by ") + 17).trim();
                    claimed = claimed.substring(0, claimed.length() - 1);
                }

                filesTemp.add(new QueueFile(fileId, date, projectName, projectURL, filePageURL, fileDirectLink, fileTitle, uploader, claimed, size));
            }

            files = filesTemp;

            // Parse projects
            Element projectsTable = doc1.getElementById("projects");
            if(projectsTable == null) {
                files = filesTemp;
                projects = projectsTemp;
                return;
            }

            projectsTable = projectsTable.getElementsByTag("tbody").get(0);
            e = projectsTable.getElementsByClass("row-joined-to-next");
            if(e.isEmpty()) {
                files = filesTemp;
                projects = projectsTemp;
                return;
            }
            for(Element project : e) {
                Elements infoBlocks = project.getElementsByTag("td");

                int projectId = Integer.parseInt(infoBlocks.get(0).child(0).getAllElements().get(0).attr("value"));

                String projectName = infoBlocks.get(1).child(1).text();
                String projectURL = "http://dev.bukkit.org" + infoBlocks.get(1).child(1).attr("href");
                String uploader = infoBlocks.get(4).child(0).text().trim();
                String date = infoBlocks.get(2).text();
                String claimed = infoBlocks.get(1).text();

                if (!claimed.contains("(Under Review")) {
                    claimed = null;
                } else {
                    claimed = claimed.substring(claimed.indexOf("(Under Review by ") + 17).trim();
                    claimed = claimed.substring(0, claimed.length() - 1);
                }

                projectsTemp.add(new QueueProject(projectId, date, projectName, projectURL, uploader, claimed));
            }

            files = filesTemp;
            projects = projectsTemp;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseReports() {
        List<QueueReport> reportsTemp = new ArrayList<QueueReport>();

        try {
            Connection c = Jsoup.connect(Constants.BUKKITDEV_REPORTS_URL + "?api-key=" + Constants.BUKKITDEV_API_KEY);
            c.timeout(180000);
            c.maxBodySize(5242880);
            Document doc1 = c.get();

            // Parse files
            Element table = doc1.select("table").first();
            if(table == null) {
                reports = reportsTemp;
                return;
            }

            table = table.getElementsByTag("tbody").get(0);
            for(String clazz : new String[]{"odd", "even"}) {
                Elements e = table.getElementsByClass(clazz);
                if(e.isEmpty()) {
                    continue;
                }

                for(Element file : e) {
                    Elements infoBlocks = file.getElementsByTag("td");

                    String name = infoBlocks.get(0).text();
                    String url = infoBlocks.get(0).child(0).attr("href");
                    long date = Long.parseLong(infoBlocks.get(2).child(0).attr("data-epoch"));
                    String target = infoBlocks.get(3).text();
                    String user = infoBlocks.get(4).text();
                    String category = infoBlocks.get(5).text();

                    reportsTemp.add(new QueueReport(name, url, target, user, category, date));
                }
            }

            reports = reportsTemp;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasUnderReview(String name) {
        int proj = 0;
        int file = 0;

        for(QueueProject p : projects) {
            if(p.isUnderReview() && p.getReviewer().equalsIgnoreCase(name)) {
                proj++;
            }
        }

        for(QueueFile f : files) {
            if(f.isUnderReview() && f.getReviewer().equalsIgnoreCase(name)) {
                file++;
            }
        }

        return proj + file > 4;
    }
}
