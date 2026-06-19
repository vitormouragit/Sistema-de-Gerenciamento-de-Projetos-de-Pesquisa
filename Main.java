import entidades.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SistemaProjetos sistema = new SistemaProjetos();
        inicializarDados(sistema);

        boolean rodandoSistema = true;

        while (rodandoSistema) {
            String[] opcoesIniciais = {"Fazer Login", "Cadastrar Usuário", "Sair"};
            int escolhaInicial = JOptionPane.showOptionDialog(
                    null,
                    "Bem-vindo ao Sistema de Gestão de Projetos!",
                    "Tela Inicial",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opcoesIniciais,
                    opcoesIniciais[0]
            );

            if (escolhaInicial == 0) { 
                String email = JOptionPane.showInputDialog(null, "Digite seu e-mail:", "Login", JOptionPane.QUESTION_MESSAGE);
                if (email == null) continue; 

                String senha = JOptionPane.showInputDialog(null, "Digite sua senha:", "Login", JOptionPane.QUESTION_MESSAGE);
                if (senha == null) continue; 

                Usuario usuarioLogado = sistema.realizarLogin(email, senha);

                if (usuarioLogado != null) {
                    JOptionPane.showMessageDialog(null, "Login bem-sucedido!\nBem-vindo(a), " + usuarioLogado.getNome());
                    
                    if (usuarioLogado instanceof Aluno) {
                        menuAluno((Aluno) usuarioLogado, sistema);
                    } else if (usuarioLogado instanceof Professor) {
                        menuProfessor((Professor) usuarioLogado, sistema);
                    } else if (usuarioLogado instanceof Coordenador) {
                        menuCoordenador((Coordenador) usuarioLogado, sistema);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "E-mail, senha incorretos ou usuário inativo.", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                }
            } else if (escolhaInicial == 1) {
                menuCadastro(sistema);
            } else {
                rodandoSistema = false;
            }
        }
        JOptionPane.showMessageDialog(null, "Sistema encerrado. Até logo!");
    }

    private static void menuCadastro(SistemaProjetos sistema) {
        String[] perfis = {"Aluno", "Professor"};
        String perfilSelecionado = (String) JOptionPane.showInputDialog(
                null, "Deseja se cadastrar como:", "Cadastro de Usuário",
                JOptionPane.PLAIN_MESSAGE, null, perfis, perfis[0]
        );

        if (perfilSelecionado == null) return;

        try {
            String nome = JOptionPane.showInputDialog(null, "Digite o nome completo:", "Cadastro - " + perfilSelecionado, JOptionPane.QUESTION_MESSAGE);
            if (nome == null || nome.trim().isEmpty()) return;

            String email = JOptionPane.showInputDialog(null, "Digite o e-mail:", "Cadastro - " + perfilSelecionado, JOptionPane.QUESTION_MESSAGE);
            if (email == null || email.trim().isEmpty()) return;

            String senha = JOptionPane.showInputDialog(null, "Defina uma senha:", "Cadastro - " + perfilSelecionado, JOptionPane.QUESTION_MESSAGE);
            if (senha == null || senha.trim().isEmpty()) return;

            if (perfilSelecionado.equals("Aluno")) {
                Aluno novoAluno = new Aluno(nome, email, senha);
                sistema.cadastrarUsuario(novoAluno);
                JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso! Já pode fazer login.");
            } else if (perfilSelecionado.equals("Professor")) {
                String departamento = JOptionPane.showInputDialog(null, "Digite o seu departamento:", "Cadastro - Professor", JOptionPane.QUESTION_MESSAGE);
                if (departamento == null || departamento.trim().isEmpty()) return;

                Professor novoProfessor = new Professor(nome, email, senha, departamento);
                sistema.cadastrarUsuario(novoProfessor);
                JOptionPane.showMessageDialog(null, "Professor cadastrado com sucesso! Já pode fazer login.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void menuAluno(Aluno aluno, SistemaProjetos sistema) {
        boolean logado = true;
        while (logado) {
            String[] opcoes = {
                "1 - Ver projetos disponíveis",
                "2 - Solicitar participação",
                "3 - Enviar relatório",
                "4 - Ver notificações",
                "0 - Sair"
            };

            String escolha = (String) JOptionPane.showInputDialog(
                    null, "Selecione uma opção:", "Menu Aluno - " + aluno.getNome(),
                    JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]
            );

            if (escolha == null || escolha.startsWith("0")) {
                logado = false;
                continue;
            }

            try {
                if (escolha.startsWith("1")) {
                    List<Projeto> ativos = sistema.listarProjetosAtivos();
                    StringBuilder sb = new StringBuilder("=== Projetos Ativos ===\n");
                    for (int i = 0; i < ativos.size(); i++) {
                        sb.append(i).append(" - ").append(ativos.get(i)).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());

                } else if (escolha.startsWith("2")) {
                    List<Projeto> ativos = sistema.listarProjetosAtivos();
                    if (ativos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Não há projetos ativos.");
                        continue;
                    }
                    String[] projsNomes = ativos.stream().map(Projeto::getTitulo).toArray(String[]::new);
                    String selecionado = (String) JOptionPane.showInputDialog(null, "Escolha o projeto:", "Solicitar Vaga", JOptionPane.PLAIN_MESSAGE, null, projsNomes, projsNomes[0]);
                    
                    if (selecionado != null) {
                        Projeto p = ativos.stream().filter(proj -> proj.getTitulo().equals(selecionado)).findFirst().orElse(null);
                        aluno.solicitarParticipacao(p);
                        JOptionPane.showMessageDialog(null, "Inscrição realizada com sucesso no projeto " + p.getTitulo());
                    }

                } else if (escolha.startsWith("3")) {
                    List<Projeto> participando = aluno.getProjetosParticipando();
                    if (participando.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não está participando de nenhum projeto ativo.");
                        continue;
                    }
                    String[] projsNomes = participando.stream().map(Projeto::getTitulo).toArray(String[]::new);
                    String selecionado = (String) JOptionPane.showInputDialog(null, "Enviar relatório para qual projeto?", "Relatório", JOptionPane.PLAIN_MESSAGE, null, projsNomes, projsNomes[0]);
                    
                    if (selecionado != null) {
                        Projeto p = participando.stream().filter(proj -> proj.getTitulo().equals(selecionado)).findFirst().orElse(null);
                        String relatorioTexto = JOptionPane.showInputDialog(null, "Digite a descrição do relatório:");
                        if (relatorioTexto != null && !relatorioTexto.isEmpty()) {
                            aluno.enviarRelatorio(p, relatorioTexto);
                            JOptionPane.showMessageDialog(null, "Relatório enviado com sucesso!");
                        }
                    }

                } else if (escolha.startsWith("4")) {
                    List<String> notifs = aluno.getNotificacoes();
                    if (notifs.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nenhuma notificação.");
                    } else {
                        StringBuilder sb = new StringBuilder("=== Suas Notificações ===\n");
                        notifs.forEach(n -> sb.append("- ").append(n).append("\n"));
                        JOptionPane.showMessageDialog(null, sb.toString());
                        aluno.limparNotificacoes(); 
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro na Operação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void menuProfessor(Professor professor, SistemaProjetos sistema) {
        boolean logado = true;
        while (logado) {
            String[] opcoes = {
                "1 - Criar novo projeto",
                "2 - Visualizar inscritos por projeto",
                "3 - Visualizar relatórios recebidos",
                "4 - Ver notificações",
                "0 - Sair"
            };

            String escolha = (String) JOptionPane.showInputDialog(
                    null, "Selecione uma opção:", "Menu Professor - " + professor.getNome(),
                    JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]
            );

            if (escolha == null || escolha.startsWith("0")) {
                logado = false;
                continue;
            }

            try {
                if (escolha.startsWith("1")) {
                    String titulo = JOptionPane.showInputDialog("Título do Projeto:");
                    String area = JOptionPane.showInputDialog("Área de pesquisa:");
                    int vagas = Integer.parseInt(JOptionPane.showInputDialog("Máximo de participantes:"));
                    
                    Projeto novo = new Projeto(titulo, area, professor, LocalDate.now(), LocalDate.now().plusMonths(6), vagas);
                    sistema.cadastrarProjeto(novo);
                    JOptionPane.showMessageDialog(null, "Projeto '" + titulo + "' criado com sucesso!");

                } else if (escolha.startsWith("2")) {
                    List<Projeto> orientados = professor.getProjetosOrientados();
                    if (orientados.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não orienta nenhum projeto ainda.");
                        continue;
                    }
                    StringBuilder sb = new StringBuilder("=== Alunos Inscritos ===\n");
                    for (Projeto p : orientados) {
                        sb.append("[").append(p.getTitulo()).append("]:\n");
                        if (p.getParticipantes().isEmpty()) sb.append("  (Nenhum aluno cadastrado)\n");
                        for (Aluno a : p.getParticipantes()) {
                            sb.append("  - ").append(a.getNome()).append(" (").append(a.getEmail()).append(")\n");
                        }
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());

                } else if (escolha.startsWith("3")) {
                    List<Projeto> orientados = professor.getProjetosOrientados();
                    StringBuilder sb = new StringBuilder("=== Histórico de Relatórios ===\n");
                    for (Projeto p : orientados) {
                        sb.append("Projeto: ").append(p.getTitulo()).append("\n");
                        if (p.getRelatorios().isEmpty()) sb.append("  Sem relatórios enviados.\n");
                        p.getRelatorios().forEach(r -> sb.append("  ").append(r.toString()).append("\n"));
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());

                } else if (escolha.startsWith("4")) {
                    List<String> notifs = professor.getNotificacoes();
                    if (notifs.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nenhuma notificação.");
                    } else {
                        StringBuilder sb = new StringBuilder("=== Notificações do Professor ===\n");
                        notifs.forEach(n -> sb.append("- ").append(n).append("\n"));
                        JOptionPane.showMessageDialog(null, sb.toString());
                        professor.limparNotificacoes();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void menuCoordenador(Coordenador coordenador, SistemaProjetos sistema) {
        boolean logado = true;
        while (logado) {
            String[] opcoes = {
                "1 - Listar todos os projetos",
                "2 - Encerrar um projeto",
                "3 - Estatísticas gerais",
                "0 - Sair"
            };

            String escolha = (String) JOptionPane.showInputDialog(
                    null, "Selecione uma opção:", "Menu Coordenador",
                    JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]
            );

            if (escolha == null || escolha.startsWith("0")) {
                logado = false;
                continue;
            }

            if (escolha.startsWith("1")) {
                List<Projeto> todos = sistema.getProjetos();
                if (todos.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nenhum projeto no sistema.");
                } else {
                    StringBuilder sb = new StringBuilder("=== Projetos do Sistema ===\n");
                    todos.forEach(p -> sb.append(p).append("\n"));
                    JOptionPane.showMessageDialog(null, sb.toString());
                }

            } else if (escolha.startsWith("2")) {
                List<Projeto> ativos = sistema.listarProjetosAtivos();
                if (ativos.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Não há projetos ativos para encerrar.");
                    continue;
                }
                String[] projsNomes = ativos.stream().map(Projeto::getTitulo).toArray(String[]::new);
                String selecionado = (String) JOptionPane.showInputDialog(null, "Escolha o projeto para encerrar:", "Encerrar", JOptionPane.PLAIN_MESSAGE, null, projsNomes, projsNomes[0]);
                
                if (selecionado != null) {
                    Projeto p = ativos.stream().filter(proj -> proj.getTitulo().equals(selecionado)).findFirst().orElse(null);
                    coordenador.encerrarProjeto(p);
                    JOptionPane.showMessageDialog(null, "Projeto '" + p.getTitulo() + "' foi encerrado com sucesso.");
                }

            } else if (escolha.startsWith("3")) {
                List<Projeto> projetos = sistema.getProjetos();
                long ativos = projetos.stream().filter(Projeto::isAtivo).count();
                long encerrados = projetos.size() - ativos;
                long totalAlunos = sistema.getUsuarios().stream().filter(u -> u instanceof Aluno).count();

                String estatisticas = "=== ESTATÍSTICAS GERAIS ===\n\n" +
                        "Projetos Cadastrados: " + projetos.size() + "\n" +
                        "  - Ativos: " + ativos + "\n" +
                        "  - Encerrados: " + encerrados + "\n\n" +
                        "Total de Alunos no Sistema: " + totalAlunos;
                
                JOptionPane.showMessageDialog(null, estatisticas, "Relatório da Coordenação", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static void inicializarDados(SistemaProjetos sistema) {
        Coordenador coord = new Coordenador("Dr. Marcos", "coord@univ.com", "123");
        sistema.cadastrarUsuario(coord);

        Professor prof1 = new Professor("Dr. Alan Turing", "alan@univ.com", "123", "Computação");
        Professor prof2 = new Professor("Dra. Marie Curie", "marie@univ.com", "123", "Física");
        sistema.cadastrarUsuario(prof1);
        sistema.cadastrarUsuario(prof2);

        Aluno aluno1 = new Aluno("João Silva", "joao@estudante.com", "123");
        Aluno aluno2 = new Aluno("Maria Souza", "maria@estudante.com", "123");
        sistema.cadastrarUsuario(aluno1);
        sistema.cadastrarUsuario(aluno2);

        Projeto p1 = new Projeto("Inteligência Artificial na Saúde", "TI", prof1, LocalDate.now(), LocalDate.now().plusMonths(6), 2);
        Projeto p2 = new Projeto("Física Quântica Aplicada", "Exatas", prof2, LocalDate.now(), LocalDate.now().plusMonths(12), 3);
        
        sistema.cadastrarProjeto(p1);
        sistema.cadastrarProjeto(p2);
    }
}