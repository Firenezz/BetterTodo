package bettertodo.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import bettertodo.core.Todo;

public class BTThreadedIO {

    /// Single threaded executor
    public static final BTThreadedIO SEQUENTIAL_EXECUTOR = new BTThreadedIO() {

        @Override
        public void init() {
            if (exService == null || exService.isShutdown()) {
                exService = Executors.newSingleThreadExecutor(
                    new ThreadFactoryBuilder().setNameFormat("BT-SEQ-%d")
                        .build());
            }
        }
    };
    /// Multi threaded executor for IO ops
    public static final BTThreadedIO DISK_IO = new BTThreadedIO() {

        @Override
        public void init() {
            if (exService == null || exService.isShutdown()) {
                exService = Executors.newCachedThreadPool(
                    new ThreadFactoryBuilder().setNameFormat("BT-IO-%d")
                        .build());
            }
        }
    };

    ExecutorService exService;

    public BTThreadedIO() {
        this.init();
    }

    public void init() {
        Todo.LOG.warn("init() wasn't overriden for BTThreadedIO");
    }

    public void shutdown() {
        exService.shutdownNow();
    }

    public void enqueue(Runnable job) {
        if (exService == null || exService.isShutdown()) {
            throw new RuntimeException("Attempted to schedule task before service was initialised!");
        } else if (job == null) {
            throw new NullPointerException("Attempted to schedule null job!");
        }

        exService.submit(job);
    }

    public <T> Future<T> enqueue(Callable<T> job) {
        if (exService == null || exService.isShutdown()) {
            throw new RuntimeException("Attempted to schedule task before service was initialised!");
        } else if (job == null) {
            throw new NullPointerException("Attempted to schedule null job!");
        }

        return exService.submit(job);
    }
}
