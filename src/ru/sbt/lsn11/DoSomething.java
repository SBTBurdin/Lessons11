package ru.sbt.lsn11;

import java.util.Random;

public class DoSomething implements Runnable {

    private final int id;

    DoSomething(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println( id + "\tStart \t" + Thread.currentThread().getName());
        doSome();
        System.out.println(id + "\tEnd \t" + Thread.currentThread().getName());
    }

//    private void doSome() {
//        try {
//            Thread.sleep(3_000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    private void doSome() {
        final int COUNT = 50_000;
        int[] arr = new int[COUNT];
        Random random = new Random();

        for (int i = 0; i < COUNT; i++) {
            arr[i] = random.nextInt();
        }

        for (int i = 0; i < COUNT; i++) {
            for (int j = 0; j < COUNT; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
}
