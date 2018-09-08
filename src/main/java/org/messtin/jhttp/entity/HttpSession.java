package org.messtin.jhttp.entity;

import org.messtin.jhttp.config.Config;

import java.util.Date;

/**
 * The session of http
 *
 * @author majinliang
 */
public class HttpSession {
    private String sessionId;
    private Date startTime;
    private Date endTime;

    public HttpSession(String sessionId) {
        this.sessionId = sessionId;
        startTime = new Date();
        endTime = new Date(startTime.getTime() + Config.SESSION_VALIDATION_TIME * 60 * 1000);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
