package Classes;

import java.io.Serializable;
import java.security.Timestamp;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;

public class band {
    private ArrayList<author> authors;
    private Timestamp creation_date;
    public band(){
        authors = new ArrayList<>();
    }

    public ArrayList<author> getMusicos() {
        return authors;
    }
    public ArrayList<author> getAuthors() {
        return authors;
    }
    public void setAuthors(ArrayList<author> authors) {
        this.authors = authors;
    }
    public void setMusicos(ArrayList<author> musicos) {
        this.authors = musicos;
    }
    public void add_musicos(author user_musico){
        this.authors.add(user_musico);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
