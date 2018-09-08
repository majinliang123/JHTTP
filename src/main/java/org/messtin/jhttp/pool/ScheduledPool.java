package org.messtin.jhttp.pool;

import org.messtin.jhttp.config.Config;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled Pool to process all the scheduled task for the server
 *
 * @author majinliang
 */
public class ScheduledPool {

    private static ScheduledExecutorService pool =
            new ScheduledThreadPoolExecutor(Config.SCHEDULE_NUM,
                    command -> new Thread(command, "JHttp_Scheduled_Pool_" + command.hashCode()));

    public static void schedule(Runnable runnable, int delay, TimeUnit timeUnit) {
        pool.scheduleAtFixedRate(runnable, 0, delay, timeUnit);
    }
}
