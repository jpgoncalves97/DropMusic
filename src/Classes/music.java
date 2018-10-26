package Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class music implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean publico;
    private String nome;
    private ArrayList<String> users;
    private String owner;
    private album album;
    private band band;
    private author author;
    private String lyrics;

    public music(String nome, band b, boolean publico, album album, String lyrics, String username){
        this.nome = nome;
        this.publico = publico;
        this.band = b;
        this.author = null;
        this.users = new ArrayList<>();
        this.album = album;
        this.lyrics = lyrics;
        this.owner = username;
    }

    public music(String nome, author a, boolean publico, album album, String lyrics, String username){
        this.nome = nome;
        this.publico = publico;
        this.author = a;
        this.band = null;
        this.users = new ArrayList<>();
        this.album = album;
        this.lyrics = lyrics;
        this.owner = null;
    }

    public void addUser(String user){ users.add(user); }
    public boolean canGetMusic(String user){
        if (publico || (owner != null && owner.equals(user))) return true;
        else return false;
    }

    public album getAlbum() { return album; }
    public ArrayList<String> getUsers() { return users; }

    public boolean isPublico() { return publico; }
    public String getLyrics() { return lyrics; }
    public String getNome() { return nome; }
    public author getAuthor(){ return author; }
    public band getBand() { return band; }
    public String getOwner(){ return owner; }
    public void setAlbum(Classes.album album) { this.album = album; }
    public void setLyrics(String lyrics) { this.lyrics = lyrics; }
    public void setPublico(boolean publico) { this.publico = publico; }
    public void setNome(String nome) { this.nome = nome; }

    public String pacote_String(boolean banda){
        //true=banda
        return this.nome+";"+banda+";"+((banda) ? this.band : this.author)+";"+this.lyrics;
    }

    @Override
    public String toString() {
        String ret = "Nome: " + this.nome + "\nPublico: " + publico + "\nAutores: \n";
        if (author != null){
            ret += author.getNome() + "\n";
        } else {
            for (author a : band.getAuthors()){
                ret += a.getNome() + "\n";
            }
        }
        ret += "Users: \n";
        for (String u : users){
            ret += u + "\n";
        }
        ret += "Letra: \n" + lyrics;
        return ret;
    }
}
