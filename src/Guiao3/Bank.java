package Guiao3;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class Bank {

    private static class Account {
        private int balance;
        private ReentrantLock lock = new ReentrantLock(); //lock ao nivel da conta

        Account(int balance) {
            this.balance = balance;
        }

        int balance(){
            return balance;
        }

        boolean deposit(int value) {
            balance += value;
            return true;
        }

        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    private Map<Integer, Account> map = new HashMap<Integer, Account>(); //mapeia um objeto a uma conta
    private int nextId = 0;
    private ReentrantLock lockBank = new ReentrantLock(); //lock ao nivel do banco

    // create account and return account id
    //Não pode ser atribuido o mm id a contas diferentes - lock banco
    public int createAccount(int balance) {
        Account c = new Account(balance);
        int id;
        lockBank.lock();
        try {
            id = nextId;
            nextId += 1;
            map.put(id, c);

        }finally {
            lockBank.unlock();
        }
        return id;
    }

    // close account and return balance, or 0 if no such account
    // ão podemos eliminar uma conta que não exista - lock banco
    // Não podemos fechar a conta com operações a acontecer ao mesmo tempo, ou estando esta bloqueada - lock conta
    public int closeAccount(int id) {
        Account c;
        lockBank.lock();
        try{
            c = map.remove(id);
            if (c == null)
                return 0;
            c.lock.lock();
        }finally{
            lockBank.unlock();
        }
        try{
            return c.balance();
        }finally {
            c.lock.unlock();
        }
    }

    // account balance; 0 if no such account
    // Conta tem de existir - lock banco
    public int balance(int id){
        Account c;
        lockBank.lock();
        try {
             c = map.get(id);
        }finally {
            lockBank.unlock();
        }
        if (c == null)
            return 0;
        return c.balance();
    }

    // deposit; fails if no such account
    // tem de existir na conta
    // Não é atómica ao nivel da conta: permite que outras operações sejam executadas
    public boolean deposit(int id, int value) {
        Account c;
        lockBank.lock();
        try {
            c = map.get(id);
        }finally {
            lockBank.unlock();
        }
        if (c == null)
            return false;
        return c.deposit(value);
    }

    // withdraw; fails if no such account or insufficient balance
    // ver se conta existe no banco
    // enquanto levanta não pode haver outras operações
    public boolean withdraw(int id, int value) {
        Account c;
        lockBank.lock();
        try {
            c = map.get(id);
            if (c == null)
                return false;
            c.lock.lock();
        }finally {
            lockBank.unlock();
        }
        try{
            return c.withdraw(value);
        }finally {
            c.lock.unlock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    // as contas tem de existir
    // temos de proteger a from ao nivel da conta
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        lockBank.lock();
        try{
            cfrom = map.get(from);
            cto = map.get(to);
        }finally {
            lockBank.unlock();
        }
        if (cfrom == null || cto ==  null)
            return false;
        if(from<to){
            cfrom.lock.lock();
            cto.lock.lock();
        }else{
            cto.lock.lock();
            cfrom.lock.lock();
        }
        try{
            return cfrom.withdraw(value) && cto.deposit(value);
        }finally {
            cfrom.lock.unlock();
            cto.lock.unlock();
        }
    }

    // sum of balances in set of accounts; 0 if some does not exist
    // conta tem de exitir

    public int totalBalance(int[] ids) {
        int total = 0;
        lockBank.lock();
        try{
            for (int i : ids) {
                Account c = map.get(i);
                if (c == null)
                    return 0;
                total += c.balance();
            }
        }finally {
            lockBank.lock();
        }
        return total;
    }
}
