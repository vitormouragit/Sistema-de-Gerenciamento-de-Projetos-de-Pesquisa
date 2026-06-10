package interfaces;

import java.util.List;

public interface UsuarioNotificacao {
    void adicionarNotificacao(String mensagem);
    List<String> getNotificacoes();
    void limparNotificacoes();
}