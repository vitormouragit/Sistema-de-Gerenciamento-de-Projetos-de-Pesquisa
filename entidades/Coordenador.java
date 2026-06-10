package entidades;

import java.util.ArrayList;
import java.util.List;

public class Coordenador extends Usuario{

    private List<Projeto> projetosGerenciados;

    public Coordenador(String nome, String email, String senha) {
        super(nome, email, senha);
        this.projetosGerenciados = new ArrayList<>();
    }

    public void adicionarProjeto(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Projeto inválido");
        }

        if (!projetosGerenciados.contains(projeto)) {
            projetosGerenciados.add(projeto);
        }
    }

    public void removerProjeto(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Projeto inválido");
        }

        projetosGerenciados.remove(projeto);
    }

    public void listarProjetos() {
        if (projetosGerenciados.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
        } else {
            for (Projeto p : projetosGerenciados) {
                System.out.println(p);
            }
        }
    }

    public void encerrarProjeto(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Projeto inválido");
        }

        projeto.encerrarProjeto();
    }

    public void exibirEstatisticas(List<Aluno> alunos, List<Professor> professores) {

        int totalProjetos = projetosGerenciados.size();

        int projetosAtivos = 0;
        int projetosEncerrados = 0;

        for (Projeto p : projetosGerenciados) {
            if (p.isAtivo()) {
                projetosAtivos++;
            } else {
                projetosEncerrados++;
            }
        }

        int totalAlunos = alunos.size();

        int alunosParticipando = 0;
        for (Aluno a : alunos) {
            if (!a.getProjetosParticipando().isEmpty()) {
                alunosParticipando++;
            }
        }

        int totalProfessores = professores.size();

        int totalProjetosProfessores = 0;
        for (Professor prof : professores) {
            totalProjetosProfessores += prof.getProjetosOrientados().size();
        }

        double mediaProjetos = totalProfessores > 0 ?
            (double) totalProjetosProfessores / totalProfessores : 0;

        System.out.println("\n=== ESTATÍSTICAS GERAIS ===");

        System.out.println("\nProjetos:");
        System.out.println("Total: " + totalProjetos);
        System.out.println("Ativos: " + projetosAtivos);
        System.out.println("Encerrados: " + projetosEncerrados);

        System.out.println("\nAlunos:");
        System.out.println("Total: " + totalAlunos);
        System.out.println("Participando de projetos: " + alunosParticipando);

        System.out.println("\nProfessores:");
        System.out.println("Total: " + totalProfessores);
        System.out.println("Média de projetos por professor: " + mediaProjetos);
    }

    @Override
    public String toString() {
        return "Coordenador: " + getNome() +
            " | Email: " + getEmail() +
            " | Projetos gerenciados: " + projetosGerenciados.size();
    }

    @Override
    public void exibirMenu() {
        System.out.println("\n=== MENU DO COORDENADOR ===");
        System.out.println("1 - Gerenciar projetos");
        System.out.println("2 - Gerenciar usuários");
        System.out.println("3 - Gerar relatórios");
        System.out.println("4 - Estatísticas gerais");
        System.out.println("5 - Sair");
        System.out.print("Escolha uma opção: ");
    }
}