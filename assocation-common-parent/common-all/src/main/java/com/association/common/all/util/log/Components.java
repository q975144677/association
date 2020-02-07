package com.association.common.all.util.log;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Components {
    public static ThreadPoolExecutor defaultThreadPool = defaultThreadPool();
    public static ThreadPoolExecutor defaultThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }
}
