package bettertodo.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import bettertodo.core.Todo;

public class BTScheduledJob {

    /// Multi threaded executor for periodic background jobs
    public static final BTScheduledJob SCHEDULED_JOB = new BTScheduledJob() {

        @Override
        public void init() {
            if (exService == null || exService.isShutdown()) {
                exService = Executors.newScheduledThreadPool(
                    10,
                    new ThreadFactoryBuilder().setNameFormat("BT-BACK-%d")
                        .build());
            }
        }
    };

    ScheduledExecutorService exService;

    public BTScheduledJob() {
        this.init();
    }

    public void init() {
        Todo.LOG.warn("init() wasn't overriden for BTThreadedIO");
    }

    public void shutdown() {
        exService.shutdownNow();
    }

    /// Submits a one-shot task that becomes enabled after the given delay.
    ///
    /// @throws RuntimeException if the executor is null
    /// or the executor is shutdown
    public void enqueue(@NotNull Runnable job, long delay, @NotNull TimeUnit unit) {
        if (exService == null) {
            throw new RuntimeException("Attempted to schedule task before service was initialised!");
        }

        exService.schedule(job, delay, unit);
    }

    /// Submits a periodic action that becomes enabled first after the given initial delay,
    /// and subsequently with the given period; that is, executions will commence after initialDelay,
    /// then `initialDelay + period`, then `initialDelay + 2 * period`, and so on.
    ///
    /// @throws RuntimeException if the executor is null
    /// or the executor is shutdown
    public void enqueueAtFixedRate(@NotNull Runnable job, long initialDelay, long period, @NotNull TimeUnit unit) {
        if (exService == null || exService.isShutdown()) {
            throw new RuntimeException("Attempted to schedule task before service was initialised!");
        }

        exService.scheduleAtFixedRate(job, initialDelay, period, unit);
    }

    /// Submits a periodic action that becomes enabled first after the given initial delay,
    /// and subsequently with the given delay between the termination of one execution and
    /// the commencement of the next.
    ///
    /// @throws RuntimeException if the executor is null
    /// or the executor is shutdown
    public void enqueueWithFixedDelay(@NotNull Runnable job, long initialDelay, long delay, @NotNull TimeUnit unit) {
        if (exService == null || exService.isShutdown()) {
            throw new RuntimeException("Attempted to schedule task before service was initialised!");
        }

        exService.scheduleWithFixedDelay(job, initialDelay, delay, unit);
    }
}
