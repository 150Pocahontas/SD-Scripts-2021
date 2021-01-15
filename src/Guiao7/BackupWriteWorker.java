package Guiao7;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class BackupWriteWorker implements Runnable {
    private Socket socket;
    private ContactList contactList;

    public void ServerWorker(Socket socket, ContactList contactList) {
        this.socket = socket;
        this.contactList = contactList;
    }

    // @TODO
    @Override
    public void run() {
        try{
            DataInputStream in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            boolean isOpen = true;
            while(isOpen){
                //ler contacto (ler os bytes e depois contruir o objeto)
                Contact newContact = Contact.deserialize(in);

                //System.out.println("name:" + name + "; age:" + age + "; Phone number:" + phNum)
                //System.out.printl(newContact);

                //adicionar à lista
                this.contactList = addContact(newContact);

            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
