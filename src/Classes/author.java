package Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class author implements Serializable {
    private static final long serialVersionUID = 1L;
    private band band;
    private String nome;
    private ArrayList<music> singles;
    private ArrayList<album> albuns;

    public author(String nome){
        this.nome = nome;
        this.band = null;
        this.singles = new ArrayList<>();
        this.albuns = new ArrayList<>();
    }

    public author(String nome, band band){
        this.nome = nome;
        this.band = band;
        this.singles = new ArrayList<>();
        this.albuns = new ArrayList<>();
    }

    public String getNome(){ return nome; }
    public band getBand() { return band; }
    public ArrayList<album> getAlbuns() {
        return albuns;
    }
    public ArrayList<music> getSingles() {
        return singles;
    }
    public void setNome(String nome){ this.nome = nome; }
    public void setBand(band band) { this.band = band; }
    public void setAlbuns(ArrayList<album> albuns) {
        this.albuns = albuns;
    }
    public void setSingles(ArrayList<music> singles) {
        this.singles = singles;
    }
    public void addAlbum(album a) { albuns.add(a); }

    @Override
    public String toString() {
        return super.toString();
    }
}
