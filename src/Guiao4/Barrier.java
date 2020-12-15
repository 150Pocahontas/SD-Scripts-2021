package Guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
    private int n;
    private int threadCounter;
    private int lastAllowedThreadId;
    private ReentrantLock lock = new ReentrantLock();
    private Condition waitForLastThread = lock.newCondition();

    public Barrier (int N){
        this.n = N;
        this.lastAllowedThreadId = -1;
        this.threadCounter = 0;

    }

    void await() throws InterruptedException{
        int myId;
        lock.lock();
        try{
            myId = threadCounter++;
            this.threadCounter++;
            if(myId % this.n != 0){
                while(myId < this.lastAllowedThreadId){
                    System.out.println("Thread is waiting.....");
                    waitForLastThread.await();
                }
                System.out.println("Thread is exiting.....");
            }
            else{
                this.lastAllowedThreadId = myId;
                waitForLastThread.signalAll();
                System.out.println("Thread is waiking up all threads....");
            }
        }finally {
            lock.unlock();
        }
    }
}
