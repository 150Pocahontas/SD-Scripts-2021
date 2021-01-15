package Guiao7;

import java.io.IOException;
import java.net.ServerSocket;

public class BackupListener implements Runnable{
    ServerSocket clientServerSocket;

    public void ClientSListener(ServerSocket clientServerSocket){
        this.clientServerSocket  = clientServerSocket;
    }

    @Override
    public void run(){
        try{
            while (true) {
                java.net.Socket socket = clientServerSocket.accept();
                ContactList contactList = new ContactList();
                Thread worker = new Thread(new ServerWriteWorker(socket, contactList)); //clientes atentidos concorrentemente
                worker.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
