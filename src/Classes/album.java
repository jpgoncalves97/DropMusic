package Classes;

import java.io.Serializable;
import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;

public class album implements Serializable {
    private String nomeAlbum;
    private ArrayList<music> musicas;
    private ArrayList<critica> criticas;
    private ArrayList<author> authors;
    private String genero;
    private Timestamp launchDate;

    public album(String nomeAlbum, String genero, Timestamp launchDate){
        this.nomeAlbum = nomeAlbum;
        this.launchDate = launchDate;
        this.genero = genero;
        this.musicas = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.criticas = new ArrayList<>();
    }

    public ArrayList<critica> getCriticas() {
        return criticas;
    }
    public void setCriticas(ArrayList<critica> criticas) {
        this.criticas = criticas;
    }
    public void addCriticas(critica newcritica){
        this.criticas.add(newcritica);
    }

    public void setMusicas(ArrayList<music> musicas) {
        this.musicas = musicas;
    }

    public ArrayList<music> getMusicas() {
        return musicas;
    }
    public void addMusicas(music newmusica){
        this.musicas.add(newmusica);
    }
    public void setAuthors(ArrayList<author> authors) {
        this.authors = authors;
    }

    public ArrayList<author> getAuthors() {
        return authors;
    }

    public String getGenero() {
        return genero;
    }

    public String getNomeAlbum() {
        return nomeAlbum;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setNomeAlbum(String nomeAlbum) {
        this.nomeAlbum = nomeAlbum;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
