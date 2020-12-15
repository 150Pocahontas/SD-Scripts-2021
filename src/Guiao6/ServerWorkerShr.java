package Guiao6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;

public class ServerWorkerShr implements Runnable{
    private Socket socket;
    private Register register;

    public ServerWorkerShr(Socket socket, Register register){
        this.socket = socket;
        this.register = register;
    }

    @Override
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream()); //aumulado aqui as linhas

            int sum = 0;
            int n = 0;
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println("Data: " + line)
                try {
                    int number = Integer.parseInt(line);
                    n++;
                    sum += number;
                    register.sum(number);
                    out.println(sum);
                    out.flush();
                } catch (NumberFormatException e) {//ignore invalid integers}
                    out.println(sum);
                    out.flush();
                }

                out.println(register.arg());
                out.flush();

                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
