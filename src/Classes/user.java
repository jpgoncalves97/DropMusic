package Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class user implements Serializable{
    private String username;
    private String password;
    private String nome;
    private int phone_num;
    private String address;
    private String num_cc;

    public user(String username, String password, String nome, int phone_num, String address, String num_cc){
        this.username=username;
        this.password=password;
        this.nome=nome;
        this.phone_num=phone_num;
        this.address=address;
        this.num_cc=num_cc;
    }

    public String getUsername(){
        return this.username;
    }
    public int getPhone_num() {
        return phone_num;
    }
    public String getNome(){
        return nome;
    }
    public String getPassword() {
        return password;
    }
    public String getAddress() {
        return address;
    }
    public String getNum_cc() {
        return num_cc;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setNum_cc(String num_cc) {
        this.num_cc = num_cc;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPhone_num(int phone_num) {
        this.phone_num = phone_num;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
