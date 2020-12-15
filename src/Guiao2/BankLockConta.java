package Guiao2;

import java.util.concurrent.locks.ReentrantLock;

public class BankLockConta{
    private static class Account {
        private int balance;
        private ReentrantLock lock = new ReentrantLock(); // lock ao nivel da conta

        Account(int balance) {
            this.balance = balance;
        }

        int balance() {
            return balance;
        }

        boolean deposit(int value) {
            lock.lock();
            try {
                balance += value;
                return true;
            }finally {
                lock.unlock();
            }
        }

        boolean withdraw(int value){
            if (value > balance)
                return false;
            lock.lock();
            try {
                balance -= value;
                return true;
            }finally {
                lock.unlock();
            }
        }
    }

    // Bank slots and vector of accounts
    private int slots;
    private BankLockConta.Account[] av;

    public BankLockConta(int n) {
        slots=n;
        av = new BankLockConta.Account[slots];
        for (int i=0; i<slots; i++) av[i]=new BankLockConta.Account(0);
    }

    // Account balance
    //fazer o lock quando o balance é feito
    public int balance(int id) {
        if (id < 0 || id >= slots)
            return 0;
        return av[id].balance();
    }

    // Deposit
    boolean deposit(int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        return av[id].deposit(value);
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        return av[id].withdraw(value);
    }

    public boolean transfer(int from, int to, int value){
        if(withdraw(from,value)){
            if(deposit(to,value))
                return true;
            else
                deposit(from,value);
        }
        return false;
    }

    public int totalBalance(){
        int total = 0;
        for(BankLockConta.Account c: av)
            total += c.balance();
        return total;
    }
}
