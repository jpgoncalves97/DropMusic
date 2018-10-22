package Classes;

public class notificacao {

    private String username;
    private String mensagem;

    public notificacao(String username, String mensagem){
        this.username = username;
        this.mensagem = mensagem;
    }

    public String getUsername(){
        return username;
    }

    public String getMensagem(){
        return mensagem;
    }
}
