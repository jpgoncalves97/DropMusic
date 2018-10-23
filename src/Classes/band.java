package Classes;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class band implements Serializable{
    private static final long serialVersionUID = 1L;
    private String nome;
    private ArrayList<author> authors;
    private ArrayList<album> albuns;

    public band(String nome){
        this.nome = nome;
        this.authors = new ArrayList<>();
        albuns = new ArrayList<>();
    }

    public ArrayList<author> getAuthors() {
        return authors;
    }
    public ArrayList<album> getAlbuns() {
        return albuns;
    }
    public String getNome() { return nome; }

    public void setAlbuns(ArrayList<album> albuns) {
        this.albuns = albuns;
    }
    public void setAuthors(ArrayList<author> authors) {
        this.authors = authors;
    }
    public void addAuthor(author user_musico){
        this.authors.add(user_musico);
    }
    public void addAlbum(album album){
        this.albuns.add(album);
    }


    @Override
    public String toString() {
        String ret = "Nome: " + nome + "\n";
        ret += "Artistas: " + "\n";
        for (author a : authors){
            ret += a.getNome() + "\n";
        }
        ret += "Albuns: " + "\n";
        for (album a : albuns){
            ret += a.getNome() + "\n";
        }
        return ret;
    }
}
