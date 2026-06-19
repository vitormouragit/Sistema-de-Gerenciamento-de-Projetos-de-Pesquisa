package entidades;

import java.util.ArrayList;
import java.util.List;

public class SistemaProjetos {

    private List<Projeto> projetos;
    private List<Usuario> usuarios; 

    public SistemaProjetos() {
        this.projetos = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public void cadastrarProjeto(Projeto projeto) {
    if (projeto == null) {
        throw new IllegalArgumentException("Projeto não pode ser nulo.");
    }
    
    this.projetos.add(projeto);
    
    Professor orientador = projeto.getOrientador();
    if (orientador != null) {
        orientador.adicionarProjeto(projeto);
    }
}

    public void cadastrarUsuario(Usuario usuario) {
        if (usuario != null) {
            this.usuarios.add(usuario);
        }
    }

    public List<Projeto> getProjetos() {
        return new ArrayList<>(projetos);
    }

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public List<Projeto> listarProjetosAtivos() {
        List<Projeto> ativos = new ArrayList<>();
        for (Projeto p : projetos) {
            if (p.isAtivo()) {
                ativos.add(p);
            }
        }
        return ativos;
    }

    public void removerProjeto(Projeto projeto) {
        this.projetos.remove(projeto);
    }

    public void removerUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
    }

    public Usuario realizarLogin(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.autenticar(email, senha)) {
                return u;
            }
        }
        return null;
    }
}