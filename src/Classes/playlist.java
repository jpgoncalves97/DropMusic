package Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class playlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<music> musicas;
    private boolean privacy;
    public playlist(){
        musicas = new ArrayList<>();
    }

    public ArrayList<music> getMusicas() {
        return musicas;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public void addMusicas(music musica) {
        this.musicas.add(musica);
    }
}
