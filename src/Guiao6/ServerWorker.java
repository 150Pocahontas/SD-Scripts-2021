package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker implements Runnable {
    private Socket socket;

    public ServerWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream()); //aumulado aqui as linhas

            int sum = 0;
            int n = 0;
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println("Data: " + line)
                try {
                    int Number = Integer.parseInt(line);
                    n++;
                    sum += Number;
                    out.println(sum);
                    out.flush();
                }catch (NumberFormatException e) {//ignore invalid integers}

                    if (n < 1) n = 1;
                    out.println((double) sum / n);
                    out.flush();

                    socket.shutdownOutput();
                    socket.shutdownInput();
                    socket.close();

                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
