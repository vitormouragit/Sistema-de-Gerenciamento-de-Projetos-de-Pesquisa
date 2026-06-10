package entidades;

import interfaces.Autenticavel;
import interfaces.UsuarioNotificacao;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario implements UsuarioNotificacao, Autenticavel {
    private String nome;
    private String email;
    private String senha;
    protected boolean ativo;
    private List<String> notificacoes;
    private static int contador = 0;
    private int id;

    public Usuario(String nome, String email, String senha) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("Senha inválida");
        }

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
        this.notificacoes = new ArrayList<>();
        this.id = ++contador;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("Senha inválida");
        }
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public void adicionarNotificacao(String mensagem) {
        if (mensagem != null && !mensagem.isEmpty()) {
            this.notificacoes.add(mensagem);
        }
    }

    @Override
    public List<String> getNotificacoes() {
        return new ArrayList<>(this.notificacoes); // protege a lista
    }

    @Override
    public void limparNotificacoes() {
        this.notificacoes.clear();
    }

    @Override
    public boolean autenticar(String emailDigitado, String senhaDigitada) {
        if (emailDigitado == null || senhaDigitada == null) {
            return false;
        }

        return this.email.equals(emailDigitado) &&
               this.senha.equals(senhaDigitada) &&
               this.ativo;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + " | Email: " + email;
    }

    public abstract void exibirMenu();
}