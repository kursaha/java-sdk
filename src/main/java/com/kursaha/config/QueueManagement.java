package com.kursaha.config;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * QueueManagement is a class that breaks the ScheduledThreadPoolExecutor.scheduleWithFixedDelay. It breaks the
 * "circular-wait" deadlock condition. If scheduleWithFixedDelay runs for a "maxRunCount" time, it interrupts the executor.
 */
public class QueueManagement<T> implements Runnable {
    private final AtomicInteger runCount = new AtomicInteger();
    private final CountQueue<T> countQueue;
    private volatile ScheduledFuture<?> self;
    private final int maxRunCount;

    public QueueManagement(int maxRunCount, ConcurrentLinkedDeque<T> dequeue) {
        this.countQueue = new CountQueue<>(dequeue);
        this.maxRunCount = maxRunCount;
    }

    @Override
    public void run() {
        countQueue.run();
        if (runCount.incrementAndGet() == maxRunCount) {
            boolean interrupted = false;
            try {
                while (self == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                }
                self.cancel(false);
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
