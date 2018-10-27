package Classes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class album implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private ArrayList<music> musicas;
    private ArrayList<critica> criticas;
    private ArrayList<author> authors;
    private String genero;
    private String descricao;
    private ArrayList<String> editores;
    private Timestamp launchDate;

    public album(String nome, String genero, Timestamp launchDate){
        this.nome = nome;
        this.launchDate = launchDate;
        this.genero = genero;
        this.descricao = "";
        this.editores = new ArrayList<>();
        this.musicas = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.criticas = new ArrayList<>();
        this.launchDate = launchDate;
    }

    public void setDescricao(String descricao, String user){
        this.descricao = descricao;
        for (String s : editores){
            if (s.equals(user)) return;
        }
        editores.add(user);
    }

    public ArrayList<critica> getCriticas() {
        return criticas;
    }
    public void setCriticas(ArrayList<critica> criticas) {
        this.criticas = criticas;
    }
    public void addCritica(critica newcritica){
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
    public ArrayList<String> getEditores() {
        return editores;
    }

    public ArrayList<author> getAuthors() {
        return authors;
    }

    public String getGenero() {
        return genero;
    }

    public String getNome() {
        return nome;
    }

    public float getPontuacaoMedia(){
        float total = 0;
        for (critica c : criticas){
            total+= c.getPoints();
        }
        return total/criticas.size();
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        String ret = "Nome: " + nome + "\nGenero: " + genero + "\n";
        for (music m : musicas){
            ret += m.getNome() + "\n";
        }
        ret += "Autores: " + "\n";
        for (author a : authors){
            ret += a.getNome() + "\n";
        }
        ret += "Pontuação média: " + getPontuacaoMedia() + "\n";
        for (critica c : criticas){
            ret += c.toString() + "\n";
        }
        return ret;
    }
}
