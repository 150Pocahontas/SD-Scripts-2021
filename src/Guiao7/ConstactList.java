package Guiao7;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


class ContactList { //etado global, pelo que precisa de controlo de concorrencia
    private List<Contact> contacts;
    private ReentrantLock lock = new ReentrantLock();

    public ContactList() {
        contacts = new ArrayList<>();//lista de contactos

        contacts.add(new Contact("John", 20, 253123321, null, new ArrayList<>(Arrays.asList("john@mail.com"))));
        contacts.add(new Contact("Alice", 30, 253987654, "CompanyInc.", new ArrayList<>(Arrays.asList("alice.personal@mail.com", "alice.business@mail.com"))));
        contacts.add(new Contact("Bob", 40, 253123456, "Comp.Ld", new ArrayList<>(Arrays.asList("bob@mail.com", "bob.work@mail.com"))));
    }

    // @TODO
    public void addContact(Contact contact) throws IOException {
        lock.lock();
        try {
            this.contacts.add(contact);
        } finally {
            lock.unlock();
        }
    }

    // @TODO
    public void getContacts(DataOutputStream out) throws IOException {
        lock.lock();
        try {
  			/*/escrever tamanho da lista e os contactos
  			out.writeInt(this.constacts.size());
  			for(Contact c : this.contacts){
  				c.serialize(out);
  			}
  			out.flush();*/
            //fazer copia da lista
        } finally {
            lock.unlock();
        }
    }    //se tivermos demasiados contactos bloqueia no serialize, ou seja nao fazemos unlock
}
//termos uma thread a escrever num socket, que está lento
//podiamos fazer um timeout, mas o cliente podia estar a tentar enrar sempre e pode acabar com a conexao
//a solução é fazer uma copia do que está dentro do lock
