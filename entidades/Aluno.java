package entidades;

import interfaces.UsuarioInterface;
import java.util.ArrayList;
import java.util.List;

public class Aluno extends Usuario implements UsuarioInterface {

    private List<Projeto> projetosParticipando;
    private List<Projeto> historicoProjetos;

    public Aluno(String nome, String email, String senha) {
        super(nome, email, senha);
        this.projetosParticipando = new ArrayList<>();
        this.historicoProjetos = new ArrayList<>();
    }

    public List<Projeto> getProjetosParticipando() {
        return new ArrayList<>(projetosParticipando);
    }

    public List<Projeto> getHistoricoProjetos() {
        return new ArrayList<>(historicoProjetos);
    }

    public void solicitarParticipacao(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Projeto inválido");
        }

        if (!this.ativo) {
            throw new IllegalStateException("Aluno inativo não pode participar");
        }

        projeto.adicionarAluno(this);
    }

    public void adicionarProjeto(Projeto projeto) {
        if (projeto == null) {
           throw new IllegalArgumentException("Projeto inválido");
        }
        if (!projetosParticipando.contains(projeto)) {
            projetosParticipando.add(projeto);
        }
    }

    public void sairDoProjeto(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Projeto inválido");
        }
        if (projetosParticipando.remove(projeto)) {
            historicoProjetos.add(projeto);
        }
    }

    public void enviarRelatorio(Projeto projeto, String relatorio) {
        if (projeto == null || relatorio == null || relatorio.isEmpty()) {
            throw new IllegalArgumentException("Dados inválidos para relatório");
        }

        if (!projetosParticipando.contains(projeto)) {
            throw new IllegalStateException("Aluno não participa do projeto");
        }

        projeto.receberRelatorio(this, relatorio);
    }

    @Override
    public void exibirMenu() {
        System.out.println("=== Menu do Aluno ===");
        System.out.println("1 - Ver projetos disponíveis");
        System.out.println("2 - Solicitar participação");
        System.out.println("3 - Enviar relatório");
        System.out.println("4 - Ver notificações");
        System.out.println("0 - Sair");
    }
}