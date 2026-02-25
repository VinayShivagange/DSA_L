package org.example.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class EvenOddThread  implements Runnable{

    AtomicInteger val;

    EvenOddThread(AtomicInteger val){
        this.val = val;
    }


    @Override
    public void run() {
        printEven();
        System.out.println("Running the Thread");
    }


    private void printEven() {
        while ( val.get() <20){
            if(val.get() %2 ==0 ){
                System.out.println(val.get());
                val.incrementAndGet();
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
