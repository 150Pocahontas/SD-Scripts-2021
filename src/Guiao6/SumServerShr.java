package Guiao6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SumServerShr {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);
            Register register = new Register();
            while (true){
                Socket socket = ss.accept();
                Thread serverWorker = new Thread(new ServerWorkerShr(socket, register));
                serverWorker.start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
