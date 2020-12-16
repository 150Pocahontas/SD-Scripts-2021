package guiao8;

//Gestão da conexão
//trabalhar com uma camada interior entre o cliente e o servidor
//temos a nossa cliApp e o ServerApp, onde até agora tinhamos uma conexão e faziamos serialize nos proprios
//agora vamos separar e trabalhas com middle web de ambas as App e a conexão é feita entre as camdaas middle web
//esta camda trablha com pacotes de bytes (frames) Frame é uma mensagem
//nao temos de expor Input, outputStream
//serve para qualquer tipo de serialização de dados

import java.net.Socket;

public class SimpleClient {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 12345);
        FramedConnection c = new FramedConnection(s);//decora o frame e poe no socket
        // send requests
        c.send("Ola".getBytes()); //envia pedido em bytes, não é preciso flush
        c.send("Hola".getBytes());
        c.send("Hello".getBytes());

        // get replies
        byte[] b1 = c.receive();
        byte[] b2 = c.receive();
        byte[] b3 = c.receive();
        System.out.println("Some Reply: " + new String(b1));
        System.out.println("Some Reply: " + new String(b2));
        System.out.println("Some Reply: " + new String(b3));

        c.close();
    }
}
