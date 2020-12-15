package Guiao6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SumServerThr {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);
            while (true){
                Socket socket = ss.accept();
                Thread serverWorker = new Thread(new ServerWorker(socket));
                serverWorker.start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
