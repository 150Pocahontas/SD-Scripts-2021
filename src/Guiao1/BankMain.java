package Guiao1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class BankMain {
    public static void main(String[] args) throws InterruptedException{
        List<Thread> t = new ArrayList<>();
        Bank bank = new Bank();
        ReentrantLock lock = new ReentrantLock();

        //criar 10 threads
        for(int N=0; N<10; N++){
            //Thread tn = new Thread(new Depositos(bank));
            Thread tn = new Thread(new DepositosLock(bank,lock));
            t.add(tn);
            tn.start();
        }

        for(Thread tn : t)
            tn.join();
        System.out.println(String.format("O valor da conta é %d",bank.balance()));
    }
}
