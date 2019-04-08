package ru.sbt.lsn11.threadPool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Количество потоков задается в конструкторе и не меняется.
 */

public class FixedThreadPool implements ThreadPool {
    private final int threadCount;
    private final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    private Boolean isRunning = true;

    public FixedThreadPool(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public void start() {
        for (int i = 0; i < threadCount; i++) {
            new Thread(new TaskWorker()).start();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        if (isRunning) {
            synchronized (queue) {
                queue.offer(runnable);
                queue.notify();
            }
        }
    }

    public void shutdown() {
        isRunning = false;
    }

    private final class TaskWorker implements Runnable {

        @Override
        public void run() {
            while (isRunning) {

                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                Runnable nextTask = queue.poll();
                if (nextTask != null) {
                    nextTask.run();
                }
            }
        }
    }

}
