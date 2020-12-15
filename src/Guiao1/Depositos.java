package Guiao1;

//pode ser executado ao mesmo tempo por threads diferentes
public class Depositos implements Runnable{
    private Bank bank;

    public Depositos(Bank b) {
        this.bank = b;
    }

    public void run() {

        for (int i = 0; i<1000; i++)
            bank.deposit(100);
    }
}
