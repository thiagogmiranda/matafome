package br.com.unigranrio.matafome.infra.sqLite.repositorios;

import android.content.ContentValues;
import android.database.Cursor;

import br.com.unigranrio.matafome.dominio.modelo.Usuario;
import br.com.unigranrio.matafome.dominio.repositorios.UsuarioRepositorio;
import br.com.unigranrio.matafome.infra.sqLite.config.SqlLiteHelper;

/**
 * Created by WebFis33 on 10/09/2015.
 */
public class UsuarioRepositorioSQLite extends RepositorioSQLite<Usuario> implements UsuarioRepositorio {
    public UsuarioRepositorioSQLite(SqlLiteHelper helper) {
        super(helper);
    }

    @Override
    protected Usuario cursorParaObjeto(Cursor cursor) {
        Usuario usuario = new Usuario();

        usuario.setId(cursor.getLong(0));
        usuario.setNome(cursor.getString(1));
        usuario.setEmail(cursor.getString(2));
        usuario.setSenha(cursor.getString(3));

        return usuario;
    }

    @Override
    protected String getTabela() {
        return "Usuario";
    }

    @Override
    protected String[] getProjecao() {
        return new String[]{"id", "nome", "email", "senha" };
    }

    @Override
    public void salvar(Usuario usuario) {
        abrirConexaoComBanco(false);

        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());

        usuario.setId(sqLiteDatabase.insert("Usuario", null, values));

        fecharConexaoComBanco();
    }

    @Override
    public Usuario obterPorLogin(String login) {
        abrirConexaoComBanco(false);

        Usuario usuario = null;

        Cursor cursor = consulta("email = ?", login);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            usuario = cursorParaObjeto(cursor);
            break;
        }
        cursor.close();

        fecharConexaoComBanco();

        return usuario;
    }
}
