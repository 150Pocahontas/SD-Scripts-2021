package Guiao8;
//demultiplexer - dispositivo eletronico que recebe de um canal e coloca a mensagem em multiplos canais
//esta do lado do cliente

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class Demultiplexer implements AutoCloseable {

    private final TaggedConnection conn;
    private final Map<Integer,Entry> buffer = new HashMap<>(); //mapa que mapeia a cada tag
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
                        TaggedConnection.Frame frame = this.conn.receive(); //rececao de frame continua
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

    public void send(TaggedConnection.Frame frame) throws IOException {
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
            //2.bloqueara té que haja mensagens para ler
            while (e.queue.isEmpty() && this.exception == null) {
                e.cond.await();
            }
            //3.ao extir, retorna dados
            return e.queue.poll();
        }finally {
            lock.unlock();
        }
    }

    public void close() throws IOException {
        this.close();
    }
}
