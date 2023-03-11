package com.kursaha.config;

import java.util.concurrent.ConcurrentLinkedDeque;

public class CountQueue<T> implements Runnable{

    //    private static int count = 0;
    private final ConcurrentLinkedDeque<T> dequeue;

    public CountQueue(ConcurrentLinkedDeque<T> dequeue) {
        this.dequeue = dequeue;
    }
    @Override
    synchronized public void run() {
        if (dequeue.size()<=500) {
            this.notifyAll();
        }
        if(dequeue.size() == 0) {
            Thread.currentThread().interrupt();
        }
    }
}
