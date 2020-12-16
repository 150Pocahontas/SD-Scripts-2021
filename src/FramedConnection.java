package guiao8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class FramedConnection implements AutoCloseable{

    private final Socket s;
    private final DataInputStream in;
    private final DataOutputStream out;
    private ReentrantLock sendLock = new ReentrantLock();
    private ReentrantLock receiveLock = new ReentrantLock();


    public FramedConnection(Socket socket) throws IOException {
        //obter inputStream e outputStream
        this.s =socket;
        this.in =new DataInputStream(new DataInputStream(socket.getInputStream()));
        this.out =new DataOutputStream(new DataOutputStream(socket.getOutputStream()));

        //etodo de lertura e escrita de inteiros, e outo de ler arrays de bytes
    }

    public void send(byte[] data) throws IOException {

        sendLock.lock();
        try{
            this.out.writeInt(data.length);
            this.out.write(data);
            this.out.flush();
        }finally{
            sendLock.unlock();
        }
    }

    public byte[] receive() throws IOException {
        byte[] data;

        receiveLock.lock();
        try{
            data = new byte[this.in.readInt()];
            this.in.readFully(data); //bloquante até preencher o data todo, ou der exception
        }finally{
            receiveLock.unlock();
        }
        return data;
    }

    public void close() throws IOException {
        this.s.close();
    }
}
