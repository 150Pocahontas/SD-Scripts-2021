package Guiao1;
import java.util.ArrayList;
import java.util.List;

public class IncrementerMain {
    public static void main(String[] args) throws InterruptedException{
        List<Thread> t = new ArrayList<>();

        //criar 10 threads
        for(int N=10; N>0; N--){
            Thread tn = new Thread(new Incrementer());
            t.add(tn);
            tn.start(); // tn começa a executar
        }

        for(Thread tn : t) //esperar por todas ads threads de t
            tn.join(); // espera pela thread morrer

        System.out.println("FIM!!");
    }
}
