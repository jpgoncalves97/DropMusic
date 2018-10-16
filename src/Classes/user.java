package Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class user implements Serializable{
    public boolean online_state;
    private boolean editor;
    private int idade;
    private String username;
    private String password;
    private String nome;
    private int phone_num;
    private String address;
    private int num_cc;
    private ArrayList<playlist> playlists;

    public user(){

    }

    public user(boolean editor, int idade, String username, String password, String nome, int phone_num, String address, int num_cc){
        online_state = false;
        this.editor = editor;
        this.idade = idade;
        this.username=username;
        this.password=password;
        this.nome=nome;
        this.phone_num=phone_num;
        this.address=address;
        this.num_cc=num_cc;
        playlists = new ArrayList<>();
    }

    public boolean isOnline_state() {
        return online_state;
    }

    public void setOnline_state(boolean online_state) {
        this.online_state = online_state;
    }

    public boolean isEditor() {
        return editor;
    }

    public int getIdade() {
        return idade;
    }

    public void setEditor(boolean editor) {
        this.editor = editor;
    }
    public void setIdade(int idade) {
        this.idade = idade;
    }

    public ArrayList<playlist> getPlaylists() {
        return playlists;
    }
    public void setPlaylists(ArrayList<playlist> playlists) {
        this.playlists = playlists;
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
    public int getNum_cc() {
        return num_cc;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setNum_cc(int num_cc) {
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