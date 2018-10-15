package Classes;

import java.io.File;
import java.io.Serializable;
import java.security.Timestamp;
import java.util.ArrayList;

public class music implements Serializable {
    private int id;
    private boolean publico;
    private String songname;
    private ArrayList<Integer> CCs;
    private album album;
    private String editora;
    private ArrayList<String> compositores;
    private String lyrics;
    private ArrayList<concert> concertos;
    private Timestamp timelenght;

    public music(int id, String songname, boolean publico, int primeiroCC, album album, String editora,ArrayList<String> compositores,  String lyrics, Timestamp timelenght){
        this.songname = songname;
        this.id = id;
        this.publico = publico;
        this.CCs = new ArrayList<>();
        this.CCs.add(primeiroCC);
        this.album = album;
        this.editora = editora;
        this.compositores = compositores;
        this.lyrics = lyrics;
        this.timelenght = timelenght;
        concertos = new ArrayList<>();
    }

    public void addConcert(concert newConcert){
        this.concertos.add(newConcert);
    }

    public Classes.album getAlbum() {
        return album;
    }

    public ArrayList<concert> getConcertos() {
        return concertos;
    }

    public ArrayList<Integer> getCCs() {
        return CCs;
    }

    public ArrayList<String> getCompositores() {
        return compositores;
    }

    public boolean isPublico() {
        return publico;
    }

    public int getId() {
        return id;
    }

    public String getEditora() {
        return editora;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getSongname() {
        return songname;
    }

    public Timestamp getTimelenght() {
        return timelenght;
    }

    public void setAlbum(Classes.album album) {
        this.album = album;
    }

    public void setCCs(ArrayList<Integer> CCs) {
        this.CCs = CCs;
    }

    public void setCompositores(ArrayList<String> compositores) {
        this.compositores = compositores;
    }

    public void setConcertos(ArrayList<concert> concertos) {
        this.concertos = concertos;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public void setTimelenght(Timestamp timelenght) {
        this.timelenght = timelenght;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
