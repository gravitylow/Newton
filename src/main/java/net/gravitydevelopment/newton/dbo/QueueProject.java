package net.gravitydevelopment.newton.dbo;

public class QueueProject extends QueueObject {
    private int id;
    private String title;
    private String url;
    private String author;
    
    public QueueProject(int id, String date, String title, String url, String author, String reviewer) {
        super(date, reviewer);
        this.id = id;
        this.title = title;
        this.url = url;
        this.author = author;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getUrl() {
        return url;
    } 
        
    public String getAuthor() {
        return author;
    } 
    
    public int getId() {
        return id;
    }
}
