package net.gravitydevelopment.newton.dbo;

public class QueueReport extends QueueObject {
    private String title;
    private String url;
    private String target;
    private String user;
    private String category;
    
    public QueueReport(String title, String url, String target, String user, String category, long date) {
        super(date, null); // no reviewer
        this.title = title;
        this.url = url;
        this.target = target;
        this.user = user;
        this.category = category;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getUser() {
        return user;
    }
    
    public String getCategory() {
        return category;
    } 
    
    public String getTarget() {
        return target;
    }
}
