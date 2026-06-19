package entidades;

import java.util.ArrayList;
import java.util.List;

public class Coordenador extends Usuario {


    public Coordenador(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    public void listarProjetos(SistemaProjetos sistema) {
        List<Projeto> projetos = sistema.getProjetos();
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado no sistema.");
        } else {
            for (Projeto p : projetos) {
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

    public void exibirEstatisticas(SistemaProjetos sistema) {
        List<Projeto> projetos = sistema.getProjetos();
        List<Usuario> usuarios = sistema.getUsuarios();

        List<Aluno> alunos = new ArrayList<>();
        List<Professor> professores = new ArrayList<>();
        
        for (Usuario u : usuarios) {
            if (u instanceof Aluno) {
                alunos.add((Aluno) u);
            } else if (u instanceof Professor) {
                professores.add((Professor) u);
            }
        }

        int totalProjetos = projetos.size();
        int projetosAtivos = 0;
        int projetosEncerrados = 0;

        for (Projeto p : projetos) {
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
        return "Coordenador: " + getNome() + " | Email: " + getEmail();
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