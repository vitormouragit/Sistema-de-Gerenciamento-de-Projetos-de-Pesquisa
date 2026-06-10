package entidades;

import excecao.ProjetoCheioException;
import excecao.ProjetoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Projeto {

    private String titulo;
    private String area;
    private Professor orientador;
    private LocalDate dataInicio;
    private LocalDate prazo;
    private int maxParticipantes;

    private List<Relatorio> relatorios;
    private List<Aluno> participantes;
    private boolean ativo;

    public Projeto(String titulo, String area, Professor orientador,
                   LocalDate dataInicio, LocalDate prazo, int maxParticipantes) {

        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("Título inválido");
        }
        if (area == null || area.isEmpty()) {
            throw new IllegalArgumentException("Área inválida");
        }
        if (orientador == null) {
            throw new IllegalArgumentException("Professor inválido");
        }
        if (prazo.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Prazo não pode ser antes da data de início");
        }

        if (maxParticipantes <= 0) {
            throw new IllegalArgumentException("Máximo de participantes inválido");
        }

        if (dataInicio == null) {
            throw new IllegalArgumentException("Data de início inválida");
        }

        this.titulo = titulo;
        this.area = area;
        this.orientador = orientador;
        this.dataInicio = dataInicio;
        this.prazo = prazo;
        this.maxParticipantes = maxParticipantes;
        this.participantes = new ArrayList<>();
        this.ativo = true;
        this.relatorios = new ArrayList<>();
        
        orientador.adicionarProjeto(this);
    }

    public String getTitulo() { return titulo; }
    public String getArea() { return area; }
    public Professor getOrientador() { return orientador; }
    public List<Aluno> getParticipantes() { return new ArrayList<>(participantes); }
    public List<Relatorio> getRelatorios() { return new ArrayList<>(relatorios); }
    public boolean isAtivo() { return ativo; }

    public void adicionarAluno(Aluno aluno) {
        if (!ativo) {
            throw new ProjetoException("Inscrição recusada: O projeto " + titulo + " já está encerrado.");
        }
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno inválido");
        }
        if (participantes.contains(aluno)) {
            throw new ProjetoException("Aluno já está inscrito neste projeto.");
        }
        if (participantes.size() >= maxParticipantes) {
            throw new ProjetoCheioException("Não há vagas disponíveis no projeto: " + titulo);
        }

        participantes.add(aluno);
        aluno.adicionarProjeto(this);
        aluno.adicionarNotificacao("Você foi adicionado ao projeto: " + titulo);
    }

    public void removerAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno inválido");
        }

        if (participantes.remove(aluno)) {
            aluno.sairDoProjeto(this);
            aluno.adicionarNotificacao("Você foi removido do projeto: " + titulo);
        }
    }

    public void receberRelatorio(Aluno aluno, String descricao) {
        if (!ativo) {
            throw new ProjetoException("Projeto encerrado.");
        }

        if (descricao == null || descricao.isEmpty()) {
            throw new IllegalArgumentException("Descrição inválida");
        }

        if (!participantes.contains(aluno)) {
            throw new ProjetoException("Aluno não participa deste projeto.");
        }
        if (LocalDate.now().isAfter(prazo)) {
            throw new ProjetoException("Envio bloqueado: Prazo de entrega expirou.");
        }

        Relatorio relatorio = new Relatorio(aluno, descricao);
        relatorios.add(relatorio);
        orientador.adicionarNotificacao("Novo relatório de " + aluno.getNome() + " no projeto: " + titulo);
    }

    public void encerrarProjeto() {
        this.ativo = false;
        for (Aluno aluno : participantes) {
            aluno.adicionarNotificacao("O projeto '" + titulo + "' foi concluído/encerrado.");
        }
    }

    @Override
    public String toString() {
        return "Projeto: " + titulo +
               " | Área: " + area +
               " | Orientador: " + orientador.getNome() +
               " | Vagas: " + participantes.size() + "/" + maxParticipantes +
               " | Status: " + (ativo ? "Ativo" : "Encerrado");
    }
}