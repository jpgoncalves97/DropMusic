package Classes;

import java.util.ArrayList;

public class album {
    private ArrayList<music> musicas;
    private String genero;

    public album(String genero){
        this.genero = genero;
        this.musicas = new ArrayList<>();

    }
}
