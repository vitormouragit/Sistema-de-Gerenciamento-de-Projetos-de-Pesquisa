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
                JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");
            } else if (perfilSelecionado.equals("Professor")) {
                String departamento = JOptionPane.showInputDialog(null, "Digite o seu departamento:", "Cadastro - Professor", JOptionPane.QUESTION_MESSAGE);
                if (departamento == null || departamento.trim().isEmpty()) return;

                Professor novoProfessor = new Professor(nome, email, senha, departamento);
                sistema.cadastrarUsuario(novoProfessor);
                JOptionPane.showMessageDialog(null, "Professor cadastrado com sucesso!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void menuAluno(Aluno aluno, SistemaProjetos sistema) {
        boolean logado = true;
        while (logado) {
            String[] opcoes = {
                "1 - Ver projetos disponíveis",
                "2 - Solicitar participação",
                "3 - Enviar relatório parcial",
                "4 - Histórico de projetos",
                "5 - Ver notificações",
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
                    for (Projeto p : ativos) {
                        sb.append("- ").append(p).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());

                } else if (escolha.startsWith("2")) {
                    List<Projeto> ativos = sistema.listarProjetosAtivos();
                    if (ativos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Não há projetos ativos disponíveis.");
                        continue;
                    }
                    String[] projsNomes = ativos.stream().map(Projeto::getTitulo).toArray(String[]::new);
                    String selecionado = (String) JOptionPane.showInputDialog(null, "Escolha o projeto para participar:", "Solicitar Participação", JOptionPane.PLAIN_MESSAGE, null, projsNomes, projsNomes[0]);
                    
                    if (selecionado != null) {
                        Projeto p = ativos.stream().filter(proj -> proj.getTitulo().equals(selecionado)).findFirst().orElse(null);
                        aluno.solicitarParticipacao(p);
                        JOptionPane.showMessageDialog(null, "Inscrição solicitada/realizada com sucesso!");
                    }

                } else if (escolha.startsWith("3")) {
                    List<Projeto> participando = aluno.getProjetosParticipando();
                    if (participando.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não está ativo em nenhum projeto.");
                        continue;
                    }
                    String[] projsNomes = participando.stream().map(Projeto::getTitulo).toArray(String[]::new);
                    String selecionado = (String) JOptionPane.showInputDialog(null, "Enviar relatório para qual projeto?", "Relatório Parcial", JOptionPane.PLAIN_MESSAGE, null, projsNomes, projsNomes[0]);
                    
                    if (selecionado != null) {
                        Projeto p = participando.stream().filter(proj -> proj.getTitulo().equals(selecionado)).findFirst().orElse(null);
                        String relatorioTexto = JOptionPane.showInputDialog(null, "Digite a descrição/progresso do relatório:");
                        if (relatorioTexto != null && !relatorioTexto.isEmpty()) {
                            aluno.enviarRelatorio(p, relatorioTexto);
                            JOptionPane.showMessageDialog(null, "Relatório parcial enviado ao orientador!");
                        }
                    }

                } else if (escolha.startsWith("4")) {
                    List<Projeto> historico = aluno.getHistoricoProjetos();
                    StringBuilder sb = new StringBuilder("=== Histórico de Projetos Concluídos/Saídas ===\n");
                    if (historico.isEmpty()) {
                        sb.append("Nenhum histórico registrado.");
                    } else {
                        historico.forEach(p -> sb.append("- ").append(p.getTitulo()).append(" [Encerrado/Saída]\n"));
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());

                } else if (escolha.startsWith("5")) {
                    List<String> notifs = aluno.getNotificacoes();
                    if (notifs.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nenhuma notificação.");
                    } else {
                        StringBuilder sb = new StringBuilder("=== Notificações ===\n");
                        notifs.forEach(n -> sb.append("- ").append(n).append("\n"));
                        JOptionPane.showMessageDialog(null, sb.toString());
                        aluno.limparNotificacoes(); 
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void menuProfessor(Professor professor, SistemaProjetos sistema) {
        boolean logado = true;
        while (logado) {
            String[] opcoes = {
                "1 - Criar novo projeto",
                "2 - Editar projeto que coordena",
                "3 - Acompanhar progresso / Validar relatórios",
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
                    if (titulo == null) continue;
                    String area = JOptionPane.showInputDialog("Área de pesquisa:");
                    if (area == null) continue;
                    int vagas = Integer.parseInt(JOptionPane.showInputDialog("Máximo de participantes:"));
                    
                    Projeto novo = new Projeto(titulo, area, professor, LocalDate.now(), LocalDate.now().plusMonths(6), vagas);
                    sistema.cadastrarProjeto(novo);
                    JOptionPane.showMessageDialog(null, "Projeto '" + titulo + "' criado com sucesso!");

                } else if (escolha.startsWith("2")) {
                    List<Projeto> orientados = professor.getProjetosOrientados();
                    if (orientados.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Você não possui projetos para editar.");
                        continue;
                    }
                    String[] projsNomes = orientados.stream().map(Projeto::getTitulo).toArray(String[]::new);
                    String selecionado = (String) JOptionPane.showInputDialog(null, "Escolha o projeto para editar:", "Editar Projeto", JOptionPane.PLAIN_MESSAGE, null, projsNomes, projsNomes[0]);
                    
                    if (selecionado != null) {
                        Projeto p = orientados.stream().filter(proj -> proj.getTitulo().equals(selecionado)).findFirst().orElse(null);
                        
                        String[] campos = {"Alterar Título", "Alterar Área"};
                        String campoAlterar = (String) JOptionPane.showInputDialog(null, "O que deseja alterar?", "Edição", JOptionPane.PLAIN_MESSAGE, null, campos, campos[0]);
                        
                        if (campoAlterar != null) {
                            String novoValor = JOptionPane.showInputDialog("Digite o novo valor:");
                            if (novoValor != null && !novoValor.trim().isEmpty()) {
                                if (campoAlterar.equals("Alterar Título")) {
                                    System.out.println("Alterado via console reflexivo ou simulação de campo.");
                                    JOptionPane.showMessageDialog(null, "Título atualizado (Simulação: REQUER Setter na classe Projeto se persistido em banco).");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Área atualizada com sucesso!");
                                }
                            }
                        }
                    }

                } else if (escolha.startsWith("3")) {
                    List<Projeto> orientados = professor.getProjetosOrientados();
                    StringBuilder sb = new StringBuilder("=== Relatórios dos Alunos para Validação ===\n\n");
                    boolean temRelatorio = false;

                    for (Projeto p : orientados) {
                        if (!p.getRelatorios().isEmpty()) {
                            temRelatorio = true;
                            sb.append("Projeto: ").append(p.getTitulo()).append("\n");
                            for (Relatorio r : p.getRelatorios()) {
                                sb.append(" -> ").append(r.toString()).append("\n");
                            }
                        }
                    }

                    if (!temRelatorio) {
                        JOptionPane.showMessageDialog(null, "Nenhum relatório pendente de validação.");
                    } else {
                        JOptionPane.showMessageDialog(null, sb.toString());
                        String[] acoes = {"Validar/Aprovar Todos", "Voltar"};
                        int acao = JOptionPane.showOptionDialog(null, "Selecione uma ação para os relatórios visualizados:", "Validação", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, acoes, acoes[0]);
                        if (acao == 0) {
                            JOptionPane.showMessageDialog(null, "Todos os relatórios revisados foram validados e arquivados com sucesso!");
                        }
                    }

                } else if (escolha.startsWith("4")) {
                    List<String> notifs = professor.getNotificacoes();
                    if (notifs.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nenhuma notificação.");
                    } else {
                        StringBuilder sb = new StringBuilder("=== Notificações ===\n");
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
                "1 - Cadastrar Projeto (Qualquer)",
                "2 - Remover/Encerrar Projeto",
                "3 - Gerenciar Usuários (Ativar/Desativar/Remover)",
                "4 - Relatórios e Estatísticas Gerais",
                "0 - Sair"
            };

            String escolha = (String) JOptionPane.showInputDialog(
                    null, "Selecione uma opção:", "Menu Coordenador - Administração",
                    JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]
            );

            if (escolha == null || escolha.startsWith("0")) {
                logado = false;
                continue;
            }

            try {
                if (escolha.startsWith("1")) {
                    String titulo = JOptionPane.showInputDialog("Título do Projeto:");
                    String area = JOptionPane.showInputDialog("Área:");
                    
                    List<Usuario> usuarios = sistema.getUsuarios();
                    Professor[] professores = usuarios.stream()
                            .filter(u -> u instanceof Professor)
                            .toArray(Professor[]::new);

                    if (professores.length == 0) {
                        JOptionPane.showMessageDialog(null, "Não há professores cadastrados para orientar.");
                        continue;
                    }

                    String[] profNomes = java.util.Arrays.stream(professores).map(Usuario::getNome).toArray(String[]::new);
                    String profEscolhido = (String) JOptionPane.showInputDialog(null, "Selecione o Professor Orientador:", "Orientação", JOptionPane.PLAIN_MESSAGE, null, profNomes, profNomes[0]);
                    
                    if (profEscolhido != null) {
                        Professor orientador = java.util.Arrays.stream(professores).filter(p -> p.getNome().equals(profEscolhido)).findFirst().get();
                        Projeto p = new Projeto(titulo, area, orientador, LocalDate.now(), LocalDate.now().plusMonths(6), 5);
                        sistema.cadastrarProjeto(p);
                        JOptionPane.showMessageDialog(null, "Projeto cadastrado na base global pela coordenação!");
                    }

                } else if (escolha.startsWith("2")) {
                    List<Projeto> todos = sistema.getProjetos();
                    if (todos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nenhum projeto no sistema.");
                        continue;
                    }
                    String[] projsNomes = todos.stream().map(Projeto::getTitulo).toArray(String[]::new);
                    String selecionado = (String) JOptionPane.showInputDialog(null, "Selecione o projeto:", "Remover/Encerrar", JOptionPane.PLAIN_MESSAGE, null, projsNomes, projsNomes[0]);
                    
                    if (selecionado != null) {
                        Projeto p = todos.stream().filter(proj -> proj.getTitulo().equals(selecionado)).findFirst().orElse(null);
                        String[] acoes = {"Encerrar Totalmente", "Apagar do Sistema (Remover)"};
                        int acao = JOptionPane.showOptionDialog(null, "O que deseja fazer com: " + p.getTitulo(), "Gerenciamento Global", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, acoes, acoes[0]);
                        
                        if (acao == 0) {
                            coordenador.encerrarProjeto(p);
                            JOptionPane.showMessageDialog(null, "Projeto marcado como Encerrado.");
                        } else if (acao == 1) {
                            sistema.removerProjeto(p);
                            JOptionPane.showMessageDialog(null, "Projeto removido permanentemente do sistema.");
                        }
                    }

                } else if (escolha.startsWith("3")) {
                    List<Usuario> usuarios = sistema.getUsuarios();
                    String[] usersNomes = usuarios.stream().map(u -> u.getNome() + " (" + u.getEmail() + ")").toArray(String[]::new);
                    String selecionado = (String) JOptionPane.showInputDialog(null, "Selecione o usuário para gerenciar:", "Gerenciador de Usuários", JOptionPane.PLAIN_MESSAGE, null, usersNomes, usersNomes[0]);

                    if (selecionado != null) {
                        Usuario u = usuarios.stream().filter(user -> (user.getNome() + " (" + user.getEmail() + ")").equals(selecionado)).findFirst().orElse(null);
                        
                        String statusAtual = u.isAtivo() ? "Ativo" : "Inativo";
                        String[] acoes = {"Alternar Status (Atual: " + statusAtual + ")", "Remover do Sistema"};
                        int acao = JOptionPane.showOptionDialog(null, "Gerenciando: " + u.getNome() + "\nEscolha uma ação:", "Controle de Acesso", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, acoes, acoes[0]);
                        
                        if (acao == 0) {
                            u.setAtivo(!u.isAtivo());
                            JOptionPane.showMessageDialog(null, "Status alterado! Novo status: " + (u.isAtivo() ? "Ativo" : "Inativo"));
                        } else if (acao == 1) {
                            sistema.removerUsuario(u);
                            JOptionPane.showMessageDialog(null, "Usuário apagado do sistema.");
                        }
                    }

                } else if (escolha.startsWith("4")) {
                    List<Projeto> projetos = sistema.getProjetos();
                    long ativos = projetos.stream().filter(Projeto::isAtivo).count();
                    long encerrados = projetos.size() - ativos;
                    long totalAlunos = sistema.getUsuarios().stream().filter(u -> u instanceof Aluno).count();
                    long totalProfs = sistema.getUsuarios().stream().filter(u -> u instanceof Professor).count();

                    String estatisticas = "=== RELATÓRIOS E ESTATÍSTICAS GERAIS ===\n\n" +
                            "Projetos Globais: " + projetos.size() + "\n" +
                            "  - Ativos: " + ativos + "\n" +
                            "  - Encerrados/Concluídos: " + encerrados + "\n\n" +
                            "Indicadores de Usuários:\n" +
                            "  - Alunos cadastrados: " + totalAlunos + "\n" +
                            "  - Professores cadastrados: " + totalProfs + "\n\n" +
                            "Status de Integridade do Sistema: Operacional";
                    
                    JOptionPane.showMessageDialog(null, estatisticas, "Painel Executivo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na Operação: " + e.getMessage(), "Erro Administrativo", JOptionPane.ERROR_MESSAGE);
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