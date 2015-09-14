package com.flatstack.flatstackvk.service.background;

import android.os.Process;
import android.support.annotation.NonNull;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BackgroundExecutorService extends ThreadPoolExecutor {

    private static final String LOG_TAG = "BackgroundExecutor";

    private static final int DEFAULT_THREAD_COUNT = 3;

    public BackgroundExecutorService() {
        super(DEFAULT_THREAD_COUNT, DEFAULT_THREAD_COUNT, 0, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>(), new BackgroundExecutorThreadFactory());
    }

    @NonNull
    @Override
    public Future<?> submit(@NonNull final Runnable task) {
        final BackgroundFutureTask future = new BackgroundFutureTask((BackgroundJob<?>) task);
        execute(future);
        return future;
    }

    private static final class BackgroundFutureTask extends FutureTask<BackgroundJob>
            implements Comparable<BackgroundFutureTask> {

        private final BackgroundJob<?> mBackgroundJob;

        public BackgroundFutureTask(final BackgroundJob<?> backgroundJob) {
            super(backgroundJob, null);
            mBackgroundJob = backgroundJob;
        }

        @Override
        public int compareTo(@NonNull final BackgroundFutureTask other) {
            final BackgroundJob<?> bj1 = mBackgroundJob;
            final BackgroundJob<?> bj2 = other.mBackgroundJob;

            // High-priority requests are "lesser" so they are sorted to the front.
            // Equal priorities are sorted by sequence number to provide FIFO ordering.
            return (bj1 == bj2 ? bj1.hashCode() - bj2.hashCode() :
                    compare(bj1.getPriority(), bj2.getPriority()));
        }

        /* package */ static int compare(final long lhs, final long rhs) {
            return (lhs < rhs ? -1 : (lhs == rhs ? 0 : 1));
        }

    }

    /* package */ static class BackgroundExecutorThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(@NonNull final Runnable r) {
            return new BackgroundThread(r);
        }

    }

    private static class BackgroundThread extends Thread {

        public BackgroundThread(@NonNull final Runnable r) {
            super(r);
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            super.run();
        }

    }

}
