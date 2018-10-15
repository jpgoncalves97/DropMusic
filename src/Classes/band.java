package Classes;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.ArrayList;

public class band implements Serializable{
    private String nomedabanda;
    private ArrayList<author> authors;
    private ArrayList<album> albuns;
    private Timestamp creation_date;
    private ArrayList<String> info;

    public band(String nomedabanda){
        this.nomedabanda = nomedabanda;
        authors = new ArrayList<>();
        albuns = new ArrayList<>();
        info = new ArrayList<>();
    }

    public ArrayList<author> getMusicos() {
        return authors;
    }
    public ArrayList<author> getAuthors() {
        return authors;
    }

    public void setAlbuns(ArrayList<album> albuns) {
        this.albuns = albuns;
    }

    public ArrayList<album> getAlbuns() {
        return albuns;
    }

    public ArrayList<String> getInfo() {
        return info;
    }

    public String getNomedabanda() {
        return nomedabanda;
    }

    public Timestamp getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Timestamp creation_date) {
        this.creation_date = creation_date;
    }

    public void setInfo(ArrayList<String> info) {
        this.info = info;
    }

    public void setNomedabanda(String nomedabanda) {
        this.nomedabanda = nomedabanda;
    }
    public void addInfo(String moreInfo){
        this.info.add(moreInfo);
    }

    public void setAuthors(ArrayList<author> authors) {
        this.authors = authors;
    }
    public void setMusicos(ArrayList<author> musicos) {
        this.authors = musicos;
    }
    public void addMusicos(author user_musico){
        this.authors.add(user_musico);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
