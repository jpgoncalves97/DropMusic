package Classes;

public class music {
    private boolean publico;
    private String nome;


    public music(String nome, boolean publico){
        this.nome = nome;
        this.publico = publico;
    }

    @Override
    public String toString(){
        return nome + ";" + (publico == true ? "true" : "false");
    }
}
