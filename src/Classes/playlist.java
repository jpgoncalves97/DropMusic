package Classes;

import javax.print.DocFlavor;
import java.io.Serializable;
import java.util.ArrayList;

public class playlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<music> musicas;
    private boolean privado;
    private String nome;
    private String owner;
    public playlist(String nome, boolean privado, String owner){
        this.nome = nome;
        this.privado = privado;
        this.owner = owner;
        musicas = new ArrayList<>();
    }

    public String getOwner(){ return owner; }
    public String getNome(){ return nome; }
    public ArrayList<music> getMusicas() {
        return musicas;
    }

    public boolean isPrivado() {
        return privado;
    }

    public void setPrivado(boolean privado) {
        this.privado = privado;
    }
    public void addMusica(music musica) {
        this.musicas.add(musica);
    }
}
