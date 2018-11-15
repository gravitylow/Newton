package net.gravitydevelopment.newton.dbo;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import net.gravitydevelopment.newton.util.Util;

public class QueueObject {
    private Date date;
    private String reviewer;
    
    /**
     * Initialize a QueueObject with a formatted date
     * 
     * @param d formatted date
     */
    public QueueObject(String d, String reviewer) {
        this(Util.parseProjectDate(d), reviewer);
    }
    
    /**
     * Initialize a QueueObject with an epoch date
     * 
     * @param d epoch date
     */
    public QueueObject(long d, String reviewer) {
        this(Util.parseEpochDate(d), reviewer);
    }
    
    private QueueObject(Date date, String reviewer) {
        this.date = date;
        this.reviewer = reviewer;
    }
    
    /**
     * Get the date the object was submitted
     * 
     * @return submission date
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Get the hours that have passed since the object was submitted
     * 
     * @return age
     */
    public long getAge() {
        return TimeUnit.MILLISECONDS.toHours(new Date().getTime()-date.getTime());
    }
    
    /**
     * Determine whether the object is under review
     * 
     * @return true if the object is under review
     */
    public boolean isUnderReview() {
        return reviewer != null;
    }
    
    /**
     * Get the object's reviewer
     * 
     * @return reviewer
     */
    public String getReviewer() {
        return reviewer;
    }
}
