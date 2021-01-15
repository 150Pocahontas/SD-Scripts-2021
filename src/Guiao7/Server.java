package Guiao7;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main (String[] args) throws IOException {

        ServerSocket clientServerSocket = new ServerSocket(12345);
        ServerSocket backupSereverSocket = new ServerSocket(23456);
        ContactList contactList = new ContactList(); //lista de contactos

        Thread clientSocketListener = new Thread(new ClientSlistener(clientServerSocket));
        Thread backupSocketListener = new Thread(new BackupListener(backupSereverSocket,contactList));
        //--thread para aceita clientes normais
        //cria threads para cada um dos aceites
        //--aceita clientes backup
        //ter dois workers em sockets diferentes

        //strat--ver

    }
}
