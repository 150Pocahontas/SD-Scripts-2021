package Guiao7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

class Contact {
    private String name;
    private int age;
    private long phoneNumber;
        //nome/age/phoneNumber
        //Joao__/50/986568223/Andre_/12/254684868
        //6bytes/2bytes/....
        //em java uma string pode ter um tamanho maior
        //Joao Nuno/50/986568223/Andre Maria/12/254684868
        //O servidortem de saber onde termina o nome: Pomos um inteiro antes do nome
        //20/Joao Nuno/50/986568223/30/Andre Maria/12/254684868
        //writeUTF = writeInt(string.length)+write(string.)

    private String company;// Pode ser null
    private List<String> emails;
        //nome/age/phoneNumber/hasCompany[company]/emailSize/{emails}
        //nome/age/phoneNumber/hasCompany/company/emailSize/email1/email2

        //Joao Nuno/50/986568223/1/email@ola.com/Andre Maria/12/254684868/Google/1/andrM@gmail.com

    public Contact(String name, int age, long phone_number, String company, List<String> emails){
            this.name = name;
            this.age = age;
            this.phoneNumber = phone_number;
            this.company = company;
            this.emails = new ArrayList<>(emails);
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.name); //converte string para bits e faz lenght e poe prefixo
        out.writeInt(this.age);
        out.writeLong(this.phoneNumber);
        //hasCompany + Company
        if(this.company == null) out.writeBoolean(false);
        else{
            out.writeBoolean(true);
            out.writeUTF(this.company);
        }
        //emiail list
        //listaSize + email + ...emailN
        out.writeInt(this.emails.size());
        for(String email: this.emails){
            out.writeUTF(email);
        }
        out.flush();
    }

    public static Contact deserialize(DataInputStream in) throws IOException {
        String name = in.readUTF();
        int age = in.readInt();
        long phoneNum = in.readInt();
        String company = null;
        boolean hasCompany = in.readBoolean();
        if(hasCompany){
            company = in.readUTF();
        }
        List<String> emails = new ArrayList();
        int emailSize = in.readInt();
        for(int i= 0 ; i < emailSize; i++){
            emails.add(in.readUTF());
        }
        return new Contact(name,age,phoneNum, company, emails);
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";");
        builder.append(this.age).append(";");
        builder.append(this.phoneNumber).append(";");
        builder.append(this.company).append(";");
        builder.append("{");
        for (String s : this.emails) {
            builder.append(s).append(";");
        }
        builder.append("}");
        return builder.toString();
    }

}

