package Guiao1;

import java.util.concurrent.locks.ReentrantLock;

public class DepositosLock implements Runnable{
    private Bank bank;
    private ReentrantLock lock = new ReentrantLock();

    public DepositosLock(Bank b, ReentrantLock l) {
        this.bank = b;
        this.lock = l;
    }

    public void run() {
        lock.lock();
        try{
            for (int i = 0; i < 1000; i++)
                bank.deposit(100);
        }finally{
            lock.unlock();
        }
    }
}
