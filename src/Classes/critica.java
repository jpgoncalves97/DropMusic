package Classes;

import java.io.Serializable;

public class critica implements Serializable {
    private static final long serialVersionUID = 1L;
    private int points;
    private String comentario;
    private String autor;

    public critica(int points, String comentario) {
        this.points = points;
        this.comentario = comentario;
    }

    public String getAutor() { return autor; }

    public int getPoints() {
        return points;
    }

    public String getComentario() {
        return comentario;
    }

    public void setAutor(String autor) { this.autor = autor; }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return autor + "\n" + "Pontuação: " + points + "\n" + comentario;
    }
}
