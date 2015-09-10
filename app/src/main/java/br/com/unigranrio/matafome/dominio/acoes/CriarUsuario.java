package br.com.unigranrio.matafome.dominio.acoes;

import br.com.unigranrio.matafome.dominio.modelo.Usuario;
import br.com.unigranrio.matafome.dominio.repositorios.UsuarioRepositorio;

/**
 * Created by WebFis33 on 10/09/2015.
 */
public class CriarUsuario {
    private UsuarioRepositorio usuarioRepositorio;

    public CriarUsuario(UsuarioRepositorio usuarioRepositorio){
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public void executar(Usuario usuario){
        usuarioRepositorio.salvar(usuario);
    }
}
