package entidades;

import java.time.LocalDate;

public class Relatorio {

    private Aluno aluno;
    private String descricao;
    private LocalDate dataEnvio;

    public Relatorio(Aluno aluno, String descricao) {
       if (aluno == null) {
            throw new IllegalArgumentException("Aluno inválido");
        }

        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição inválida");
        }
        
        this.aluno = aluno;
        this.descricao = descricao;
        this.dataEnvio = LocalDate.now();
    }

    public Aluno getAluno() {
        return aluno;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    @Override
    public String toString() {
        return dataEnvio + " - " + aluno.getNome() + ": " + descricao;
    }
}