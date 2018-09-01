package org.messtin.jhttp.container;

import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.entity.HttpSession;
import org.messtin.jhttp.pool.SchedulePool;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SessionContainer {
    private static final volatile ConcurrentHashMap<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    static {
        SchedulePool.schedule(()->{
            sessionMap.values().stream()
                    .filter(session -> session.getEndTime().before(new Date()))
                    .map(HttpSession::getSessionId)
                    .forEach(sessionMap::remove);
        }, Config.SESSION_CHECK_DURING, TimeUnit.MINUTES);
    }

    public static void put(String sessionId) {
        HttpSession session = new HttpSession(sessionId);
        sessionMap.put(sessionId, session);
    }

    public static String get(String sessionId) {
        HttpSession session = sessionMap.get(sessionId);
        if (session != null && session.getEndTime().after(new Date())) {
            return sessionId;
        } else {
            return null;
        }
    }

    public static String create() {
        String sessionId = UUID.randomUUID().toString();
        put(sessionId);
        return sessionId;
    }
}
