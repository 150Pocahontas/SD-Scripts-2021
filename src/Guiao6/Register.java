package Guiao6;

import java.util.concurrent.locks.ReentrantLock;

public class Register {
    private ReentrantLock lock = new ReentrantLock();
    private int sum = 0;
    private int n = 0;

    public void sum(int value){
        lock.lock();
        try{
            sum += value;
            n++;
        }finally{
            lock.unlock();
        }
    }

    public double arg(){
        lock.lock();
        try{
            if(n<1) return 0;
            n++;
            return (double) sum/n-1;
        }finally{
            lock.unlock();
        }
    }
}
