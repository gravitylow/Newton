package net.gravitydevelopment.newton.dbo;

import net.gravitydevelopment.newton.util.Util;

public class QueueFile extends QueueObject {
    private int id;
    private String project;
    private String projectUrl;
    private String fileUrl;
    private String fileUrlDirect;
    private String title;
    private String author;
    private String size;
    private int bytes;
    
    public QueueFile(int id, long date, String project, String projectUrl, String fileUrl, String fileUrlDirect, String title, String author, String reviewer, String size) {
        super(date, reviewer);
        this.id = id;
        this.project = project;
        this.projectUrl = projectUrl;
        this.fileUrl = fileUrl;
        this.fileUrlDirect = fileUrlDirect;
        this.title = title;
        this.author = author;
        this.size = size;
        this.bytes = Util.sizeToBytes(size);
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getFileUrl() {
        return fileUrl;
    } 
        
    public String getFileUrlDirect() {
        return fileUrlDirect;
    } 
    
    public String getAuthor() {
        return author;
    }
       
    public String getProject() {
        return project;
    }     
    
    public String getProjectUrl() {
        return projectUrl;
    } 
    
    public String getSizeString() {
        return size;
    }
    
    public int getBytes() {
        return bytes;
    }
    
    public int getId() {
        return id;
    }
}
