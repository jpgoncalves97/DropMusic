package Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class music implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean publico;
    private String nome;
    private ArrayList<String> users;
    private album album;
    private band band;
    private author author;
    private String lyrics;

    public music(String nome, band b, boolean publico, album album, String lyrics){
        this.nome = nome;
        this.publico = publico;
        this.band = b;
        this.author = null;
        this.users = new ArrayList<>();
        this.album = album;
        this.lyrics = lyrics;
    }

    public music(String nome, author a, boolean publico, album album, String lyrics){
        this.nome = nome;
        this.publico = publico;
        this.author = a;
        this.band = null;
        this.users = new ArrayList<>();
        this.album = album;
        this.lyrics = lyrics;
    }

    public album getAlbum() { return album; }
    public ArrayList<String> getUsers() { return users; }

    public boolean isPublico() { return publico; }
    public String getLyrics() { return lyrics; }
    public String getNome() { return nome; }
    public author getAuthor(){ return author; }
    public band getBand() { return band; }

    public void setAlbum(Classes.album album) { this.album = album; }
    public void setLyrics(String lyrics) { this.lyrics = lyrics; }
    public void setPublico(boolean publico) { this.publico = publico; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        String ret = "Nome: " + this.nome + "\nAutores: \n";
        if (author != null){
            ret += author.getNome() + "\n";
        } else {
            for (author a : band.getAuthors()){
                ret += a.getNome() + "\n";
            }
        }
        return ret;
    }
}
