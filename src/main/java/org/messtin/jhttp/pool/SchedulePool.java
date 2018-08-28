package org.messtin.jhttp.pool;

import org.messtin.jhttp.config.Config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulePool {

    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(Config.SCHEDULE_NUM);

    public static void schedule(Runnable runnable, int delay, TimeUnit timeUnit) {
        pool.scheduleAtFixedRate(runnable, 0, delay, timeUnit);
    }
}
