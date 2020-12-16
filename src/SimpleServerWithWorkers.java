package guiao8;

import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServerWithWorkers {
    final static int WORKERS_PER_CONNECTION = 3; //variavel estatica que nos diz quantas threads temos por conexao. até agora tinhamos uma por conexao

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(12345);

        while(true) {
            Socket s = ss.accept();
            FramedConnection c = new FramedConnection(s); //ativamos a middle web

            //new class implements runnuble.
            Runnable worker = () -> {
                try (c) {
                    for (;;) {
                        byte[] b = c.receive();
                        String msg = new String(b); //parte aplicacionavel,estamos no serverApp e não no middle web
                        System.out.println("Replying to: " + msg);
                        c.send(msg.toUpperCase().getBytes()); //converter para upperCase e retornar em carateres minusculos
                    }
                } catch (Exception ignored) { }
            };

            for (int i = 0; i < WORKERS_PER_CONNECTION; ++i)
                new Thread(worker).start();
        }
    }
}
