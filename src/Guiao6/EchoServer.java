package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345); //para cada novo cliente

            while(true){ //idealmente os servidores estão sempre abertos
                Socket socket = ss.accept(); //Quando chamado, o servidor comunica com o cliente.
                //Um de cada vez
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //guardar o que foi lido a mais
                //socket: só se passam bytes. É o endPoint de cada um dos lados
                //adaptadores opara fazer buffering do que lê
                PrintWriter out = new PrintWriter(socket.getOutputStream()); //Acomulado aqui as linhas

                String line;
                while ((line = in.readLine()) != null) { //null quando chaga ao EOF - quando o cliente fecha a conexão.Fecha outPutStream
                    System.out.println("Data: " + line);
                    out.println(line);
                    out.flush(); // quando se invoca o  flush,diz que se quer enviar todos os seu conteúdo naquele momento
                    //Notas: o out é do printWrite
                }

                //fechar deste lado
                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
