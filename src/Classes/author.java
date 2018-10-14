package Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class author extends user implements Serializable {

    private ArrayList<music> singles;
    private ArrayList<album> albuns;
    private ArrayList<band> bandas;

    public author(boolean editor,int idade,  String username, String password, String nome, int phone_num, String address, int num_cc){
        super(editor, idade, username,password,nome,phone_num,address,num_cc);
        this.singles = new ArrayList<>();
        this.albuns = new ArrayList<>();
        this.bandas = new ArrayList<>();
    }

    public ArrayList<album> getAlbuns() {
        return albuns;
    }
    public ArrayList<music> getSingles() {
        return singles;
    }
    public ArrayList<band> getBandas() {
        return bandas;
    }

    @Override
    public boolean isEditor() {
        return super.isEditor();
    }

    @Override
    public int getIdade() {
        return super.getIdade();
    }

    @Override
    public void setEditor(boolean editor) {
        super.setEditor(editor);
    }

    @Override
    public void setIdade(int idade) {
        super.setIdade(idade);
    }

    @Override
    public ArrayList<playlist> getPlaylists() {
        return super.getPlaylists();
    }

    @Override
    public int getPhone_num() {
        return super.getPhone_num();
    }

    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public int getNum_cc() {
        return super.getNum_cc();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setAddress(String address) {
        super.setAddress(address);
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }

    @Override
    public void setNum_cc(int num_cc) {
        super.setNum_cc(num_cc);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public void setPhone_num(int phone_num) {
        super.setPhone_num(phone_num);
    }

    @Override
    public void setPlaylists(ArrayList<playlist> playlists) {
        super.setPlaylists(playlists);
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }


    public void setAlbuns(ArrayList<album> albuns) {
        this.albuns = albuns;
    }
    public void setSingles(ArrayList<music> singles) {
        this.singles = singles;
    }
    public void setBandas(ArrayList<band> bandas) {
        this.bandas = bandas;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
