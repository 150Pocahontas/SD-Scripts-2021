package Guiao2;

import java.util.concurrent.locks.ReentrantLock;

//pouco eficiente
class Bank {
    private static class Account {
        private int balance;

        Account(int balance) {
            this.balance = balance;
        }

        int balance() {
            return balance;
        }

        boolean deposit(int value) {
            balance += value;
            return true;
        }

        boolean withdraw(int value){
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    // Bank slots and vector of accounts
    private int slots;
    private Account[] av;
    private ReentrantLock lock = new ReentrantLock(); // lock ao nivel do banco

    public Bank(int n) {
        slots=n;
        av = new Account[slots];
        for (int i=0; i<slots; i++) av[i]=new Account(0);
    }

    // Account balance
    //fazer o lock quando o balance é feito
    public int balance(int id) {
        if (id < 0 || id >= slots)
            return 0;
        lock.lock();
        try {
            return av[id].balance();
        }finally {
            lock.unlock();
        }
    }

    // Deposit
    //fazer o lock quando o depósito é feito
    boolean deposit(int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        lock.lock();
        try {
            return av[id].deposit(value);
        }finally {
            lock.unlock();
        }

    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        lock.lock();
        try {
            return av[id].withdraw(value);
        }finally {
            lock.unlock();
        }
    }

    public boolean transfer(int from, int to, int value){
        lock.lock();
        try{
            if(withdraw(from,value)){
                if(deposit(to,value))
                    return true;
                else
                    deposit(from,value);
            }
        }finally {
            lock.unlock();
        }
        return false;
    }

    public int totalBalance(){
        lock.lock();
        try{
            int total = 0;
            for(Account c: av)
                total += c.balance();
            return total;
        }finally {
            lock.unlock();
        }
    }
}

