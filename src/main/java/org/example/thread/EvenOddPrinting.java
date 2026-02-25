package org.example.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EvenOddPrinting {

    private int val =1;
    private final Lock lock = new ReentrantLock();
    private final Condition oneCondition = lock.newCondition();
    private final Condition twoCondition = lock.newCondition();
    private final Condition threeCondition = lock.newCondition();


    public void one(){
        while (val<20){
            lock.lock();
            try {
            if( val%3 ==1){
                System.out.println(Thread.currentThread().getName() + " :" +val);
                val+=1;
                twoCondition.signal();
            }
                oneCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        }
    }

    public void two(){
        while (val<20){
            lock.lock();
            try {
                if( val%3 ==2){
                    System.out.println(Thread.currentThread().getName() + " :" +val);
                    val+=1;
                    threeCondition.signal();
                }
                twoCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        }
    }

    public void three(){
        while (val<20){
            lock.lock();
            try {
            if( val%3 ==0){
                System.out.println(Thread.currentThread().getName() + " :" +val);
                val+=1;
                oneCondition.signal();
            }
            threeCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        }
    }
}

class MainExample{
    public static void main(String[] args) {
        EvenOddPrinting print = new EvenOddPrinting();
        new Thread(print::one, "Thread-1").start();
        new Thread(print::two, "Thread-2").start();
        new Thread(print::three, "Thread-3").start();
    }
}