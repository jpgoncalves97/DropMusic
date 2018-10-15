package Classes;

import java.io.Serializable;

public class critica implements Serializable{
    private int points;
    private String comentario;

    public critica(int points, String comentario){
        this.points = points;
        this.comentario = comentario;
    }

    public int getPoints() {
        return points;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
