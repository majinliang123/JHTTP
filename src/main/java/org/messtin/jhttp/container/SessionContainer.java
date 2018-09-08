package org.messtin.jhttp.container;

import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.entity.HttpSession;
import org.messtin.jhttp.pool.ScheduledPool;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * The container of {@link HttpSession}
 *
 * @author majinliang
 */
public class SessionContainer {

    /**
     * A map from:
     * session id -> http session
     */
    private static volatile ConcurrentHashMap<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * Will init a scheduled task to clean the container
     * every {@link Config#SESSION_CHECK_DURING}
     */
    static {
        ScheduledPool.schedule(()->{
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
