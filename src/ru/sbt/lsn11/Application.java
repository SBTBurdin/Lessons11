package ru.sbt.lsn11;

import ru.sbt.lsn11.threadPool.FixedThreadPool;
import ru.sbt.lsn11.threadPool.ScalableThreadPool;

public class Application {
    public static void main(String[] args) {
        fixed();
//        scalable();
    }

    private static void fixed() {
        FixedThreadPool pool = new FixedThreadPool(3);
        for (int i = 0; i < 7; i++) {
            pool.execute(new DoSomething(i));
        }
        pool.start();
        try {
            Thread.sleep(2_000);
            for (int i = 100; i < 103; i++) {
                pool.execute(new DoSomething(i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void scalable() {
        ScalableThreadPool pool = new ScalableThreadPool(4, 6);
        pool.start();
        for (int i = 0; i < 10; i++) {
            pool.execute(new DoSomething(i));
        }
        try {
            Thread.sleep(10000);
            for (int i = 100; i < 111; i++) {
                pool.execute(new DoSomething(i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
