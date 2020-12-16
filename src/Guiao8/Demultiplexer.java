package guiao8;
//demultiplexer - dispositivo eletronico que recebe de um canal e coloca a mensagem em multiplos canais
//esta do lado do cliente

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import static guiao8.TaggedConnection.Frame;

public class Demultiplexer implements AutoCloseable {

    private final TaggedConnection conn;
    private final Map<Integer,Entry> buffer = new HashMap<>();
    private ReentrantLock lock = new ReentrantLock(); //fora da entry por causa dos multiplos gets que poderia gerar
    private IOException exception;

    private class Entry{
        final ArrayDeque<byte[]> queue = new ArrayDeque<byte[]>();
        final Condition cond = lock.newCondition();
    }

    public Demultiplexer(TaggedConnection taggedConnection) throws IOException {
        this.conn = taggedConnection;
    }
    public void start() {
            new Thread(() ->{
                while(true) {
                    //1. ler frame da conexao
                    try {
                        Frame frame = this.conn.receive();
                        lock.lock();
                        try {
                            //2. colocar os dados da frame (byte[]) no canal de saida de correspondete
                            Entry e = get(frame.tag);
                            e.queue.add(frame.data); //ad mensgem à queue
                            //3. notifica thread que aguarda dados começa tag
                            e.cond.signalAll();
                        } finally {
                            lock.unlock();
                        }
                    }catch(IOException e){
                        this.exception = e;
                    }

                }
            }).start();

    }

    private Entry get (int tag){
        Entry e = buffer.get(tag);
        if (e == null) {

            e = new Entry();
            buffer.put(tag,e);
        }
        return e;
    }

    public void send(Frame frame) throws IOException {
        this.conn.send(frame);
    }
    public void send(int tag, byte[] data) throws IOException {
        this.conn.send(tag,data);
    }
    public byte[] receive(int tag) throws IOException, InterruptedException {


        lock.lock();
        try {
            //1. obter queue da tag
            Entry e = get(tag); //dá-nos a tag
            //2.bloquearaté que haja mensagens para ler
            while (e.queue.isEmpty() && this.exception == null) {
                e.cond.await();
            }
            if (this.exception != null) {
                //3.ao extir, retorna dados
                byte[] res = e.queue.poll();
                return res;
            }
            //return this.exception;
        }finally {
            lock.unlock();
        }
        return null;
    }

    public void close() throws IOException {
        this.close();
    }
}