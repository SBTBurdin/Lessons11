package ru.sbt.lsn11.threadPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Количество запущенных потоков может быть увеличено от минимального к максимальному,
 * если при добавлении нового задания в очередь нет свободного потока для исполнения этого задания.
 * При отсутствии задания в очереди, количество потоков опять должно быть уменьшено до значения min.
 */

public class ScalableThreadPool implements ThreadPool {
    private final int minThreadCount;
    private final int maxThreadCount;
    private final ArrayList<PoolWorker> threads;
    private final Queue<Runnable> queue = new LinkedList<>();

    public ScalableThreadPool(int min, int max) {
        this.minThreadCount = min;
        this.maxThreadCount = max;
        threads = new ArrayList<>(max);
        for (int i = 0; i < minThreadCount; i++) {
            threads.add(new PoolWorker());
        }
    }

    @Override
    public void start() {
        for (PoolWorker thread : threads)
            thread.start();
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (queue) {
            queue.add(runnable);
            queue.notify();
            checkThreadCount();
        }
    }

    private void checkThreadCount() {
        if (queue.size() > threads.size() && threads.size() < maxThreadCount) {
            threads.add(new PoolWorker());
            threads.get(threads.size() - 1).start();
        }
        if (queue.size() < threads.size() && threads.size() > minThreadCount) {
            threads.get(threads.size() - 1).interrupt();
            threads.remove(threads.size() - 1);
        }
    }

    private class PoolWorker extends Thread {
        @Override
        public void run() {
            Runnable r;

            while (!Thread.interrupted()) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            checkThreadCount();
                            queue.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    r = queue.remove();
                }
                r.run();
            }
        }
    }
}
