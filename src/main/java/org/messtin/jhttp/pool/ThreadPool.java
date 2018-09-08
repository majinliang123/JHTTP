package org.messtin.jhttp.pool;

import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.process.AbstractProcessor;

import java.util.concurrent.*;

/**
 * Thread Pool to process all the thread in the server
 *
 * @author majinliang
 */
public class ThreadPool {

    private static ExecutorService pool =
            new ThreadPoolExecutor(Config.THREAD_NUM, Config.THREAD_NUM,
                    0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                    command -> new Thread(command, "JHttp_Thread_Pool_" + command.hashCode()));

    public static void submit(AbstractProcessor processor) {
        pool.submit(() -> processor.process());
    }

    public static void submit(Runnable run) {
        pool.submit(run);
    }
}
