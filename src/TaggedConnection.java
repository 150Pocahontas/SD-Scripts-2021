package guiao8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable{
    private final Socket s;
    private final DataInputStream in;
    private final DataOutputStream out;
    private ReentrantLock sendLock = new ReentrantLock();
    private ReentrantLock receiveLock = new ReentrantLock();

    public static class Frame {

        public final int tag;
        public final byte[] data;

        public Frame(int tag, byte[] data){
            this.tag = tag; this.data = data;
        }
    }

    public TaggedConnection(Socket socket) throws IOException {
        this.s =socket;
        this.in =new DataInputStream(new DataInputStream(socket.getInputStream()));
        this.out =new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
    }
    public void send(Frame frame) throws IOException {
        send(frame.tag, frame.data);
    }
    public void send(int tag, byte[] data) throws IOException {
        sendLock.lock();
        try{
            this.out.writeInt(4+data.length); //o int é 4 bytes
            this.out.writeInt(tag);
            this.out.write(data);
            this.out.flush();
        }finally{
            sendLock.unlock();
        }
    }

    public Frame receive() throws IOException {
        byte[] data;
        int tag;

        receiveLock.lock();
        try{
            data = new byte[this.in.readInt() - 4];
            tag = this.in.readInt();
            this.in.readFully(data);
        }finally{
            receiveLock.unlock();
        }
        return new Frame(tag,data);
    }

    public void close() throws IOException {
        this.s.close();
    }
}
