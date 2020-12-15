package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SumServer{

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);

            while (true) {
                Socket socket = ss.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream()); //aumulado aqui as linhas

                int sum = 0;
                int n = 0;
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Data: " + line);
                    //1. converter linha para interiro
                    int Number = Integer.parseInt(line);
                    //2. incrementar n e adicionar à soma
                    n++;
                    sum += Number;
                    //3.enviar a soma atual(+fush)
                    out.println(sum);
                    out.flush();
                }

                if(n < 1) n = 1;
                //4.enviar media final
                out.println((double) sum / n);
                out.flush();

                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

