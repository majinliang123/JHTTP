package org.messtin.jhttp.pool;

import org.messtin.jhttp.config.Config;
import org.messtin.jhttp.process.Processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    // init firstly so that could process request more fast.
    private static ExecutorService pool =
            Executors.newFixedThreadPool(Config.THREAD_NUM);
    public static void submit(Processor processer) {
        pool.submit(() -> processer.process());
    }

    public static void submit(Runnable run){
        pool.submit(run);
    }
}
