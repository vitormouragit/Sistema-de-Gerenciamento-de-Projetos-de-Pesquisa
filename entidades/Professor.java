package entidades;

import java.util.ArrayList;
import java.util.List;
import interfaces.UsuarioInterface;

public class Professor extends Usuario implements UsuarioInterface {

    private String departamento;
    private List<Projeto> projetosOrientados;

    public Professor(String nome, String email, String senha, String departamento) {
        super(nome, email, senha);

        if (departamento == null || departamento.isEmpty()) {
            throw new IllegalArgumentException("Departamento inválido");
        }

        this.departamento = departamento;
        this.projetosOrientados = new ArrayList<>();
    }

    public String getDepartamento() {
        return departamento;
    }

    public List<Projeto> getProjetosOrientados() {
        return new ArrayList<>(projetosOrientados);
    }

    public void adicionarProjeto(Projeto projeto) {
        if (projeto == null) {
            throw new IllegalArgumentException("Projeto inválido");
        }

        if (!projetosOrientados.contains(projeto)) {
            projetosOrientados.add(projeto);
        }
    }

    public void visualizarNotificacoes() {
        if (getNotificacoes().isEmpty()) {
            System.out.println("Sem notificações.");
        } else {
            for (String n : getNotificacoes()) {
                System.out.println(n);
            }
        }
    }

    @Override
    public void exibirMenu() {
        System.out.println("\n=== MENU DO PROFESSOR ===");
        System.out.println("1 - Criar novo projeto");
        System.out.println("2 - Editar projeto existente");
        System.out.println("3 - Visualizar inscritos");
        System.out.println("4 - Enviar notificações");
        System.out.println("5 - Sair");
        System.out.print("Escolha uma opção: ");
    }
}