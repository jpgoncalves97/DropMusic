package Classes;

import java.util.ArrayList;

public class author {

    private ArrayList<music> singles;
    private ArrayList<album> albuns;

    public author(String username, String password, String nome, int phone_num, String address, String num_cc){
        //super(username,password,nome,phone_num,address,num_cc);
        this.singles = new ArrayList<>();
        this.albuns = new ArrayList<>();
    }

    public ArrayList<album> getAlbuns() {
        return albuns;
    }
    public ArrayList<music> getSingles() {
        return singles;
    }
    public void setAlbuns(ArrayList<album> albuns) {
        this.albuns = albuns;
    }
    public void setSingles(ArrayList<music> singles) {
        this.singles = singles;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
