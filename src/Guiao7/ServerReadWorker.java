package Guiao7;

import jdk.internal.icu.text.UnicodeSet;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerReadWorker implements Runnable {
    private Socket socket;
    private ContactList contactList;

    public void ServerWorker (Socket socket, ContactList contactList) {
        this.socket = socket;
    }

    // @TODO
    @Override
    public void run() {
        try{
            socket.shutdownInput();

            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
            //escrever lista para o socket
            //ContactList list = this.contactList.getContacts(out);

            ContactList list = this.contactList.getContacts();
            //escrever tamanho da lista e os contactos
            out.writeInt(this.contactList.size());
            for(Contact c : this.contact){
                c.serialize(out);
            }
            out.flush();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
