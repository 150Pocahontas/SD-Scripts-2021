package Guiao1;

public class Incrementer implements Runnable {
    public void run() {
        final long I=100;

        for (long i = 1; i< 1+I; i++)
            System.out.println(i);
    }
}
