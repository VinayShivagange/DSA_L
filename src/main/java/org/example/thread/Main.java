package org.example.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        AtomicInteger val = new AtomicInteger(0);
        new Main().executeThread(val);
    }

    public void executeThread(AtomicInteger val){

        Thread t1 = new Thread( () ->
        {
            synchronized (this) {
                while (val.get() < 20) {
                    if (val.get() % 2 == 0) {
                        System.out.println(val.incrementAndGet());
                        notifyAll();
                    }
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        );
        Thread t2 = new Thread( () ->
        {
            synchronized (this) {
                while (val.get() < 20) {
                    if (val.get() % 2 == 1) {
                        System.out.println(val.incrementAndGet());
                        notifyAll();
                    }
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        );
        t1.start();
        t2.start();


    }
}
