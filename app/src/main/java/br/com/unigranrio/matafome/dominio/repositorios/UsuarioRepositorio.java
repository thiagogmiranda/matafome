package br.com.unigranrio.matafome.dominio.repositorios;

import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by WebFis33 on 10/09/2015.
 */
public interface UsuarioRepositorio {
    void salvar(Usuario usuario);

    Usuario obterPorLogin(String login);
}
