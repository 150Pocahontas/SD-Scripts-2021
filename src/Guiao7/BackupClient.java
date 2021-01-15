package Guiao7;

import java.io.*;
import java.net.Socket;

public class BackupClient {

    public static void main (String[] args) throws IOException {
        Socket socket = new Socket("localhost", 23456);
        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        int elements = in.readInt();

        for(int i = 0 ; i<elements; i++){
            Contact contact =Contact.deserialize(in);
        }
        socket.close();
    }
}
