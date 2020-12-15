package Guiao2;
import java.util.Random;

class Mover implements Runnable {
    BankLockConta b;
    int s; // Number of accounts

    public Mover(BankLockConta b, int s) {
        this.b = b;
        this.s = s;
    }

    public void run() {
        final int moves = 100000;
        int from, to;
        Random rand = new Random();

        for (int m = 0; m < moves; m++) {
            from = rand.nextInt(s); // Get one
            while ((to = rand.nextInt(s)) == from) ; // Slow way to get distinct
            b.transfer(from, to, 1);
        }
    }
}