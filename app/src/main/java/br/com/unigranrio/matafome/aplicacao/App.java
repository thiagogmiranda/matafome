package br.com.unigranrio.matafome.aplicacao;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import java.util.Locale;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by WebFis33 on 22/09/2015.
 */
public class App extends Application {
    private static Context currentContext;

    private static final String USUARIO_LOGADO = "usuarioEstaLogado";
    private static final String ID_USUARIO = "idUsuario";
    private static final String NOME_USUARIO = "nomeUsuario";
    private static final String EMAIL_USUARIO = "emailUsuario";
    private static final String SENHA_USUARIO = "senhaUsuario";
    private static final String TIPO_USUARIO = "tipoUsuario";
    private static final String DATA_CADASTRO_USUARIO = "dataCadastroUsuario";

    public void onCreate() {
        super.onCreate();
        currentContext = getApplicationContext();
    }

    public static Context getCurrentContext(){
        return currentContext;
    }

    public static String montarUrlRest(int urlId){
        Resources resources = currentContext.getResources();
        return  resources.getString(R.string.server_url) + resources.getString(urlId);
    }

    public static String montarUrlRest(int urlFormatId, Object... params){
        Resources resources = currentContext.getResources();
        return  resources.getString(R.string.server_url) + String.format(Locale.ENGLISH, resources.getString(urlFormatId), params);
    }

    public static void efetuarLogin(Usuario usuario) {
        SharedPreferences prefs = currentContext.getSharedPreferences("mata_fome", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(USUARIO_LOGADO, true);
        editor.putLong(ID_USUARIO, usuario.getId());
        editor.putString(NOME_USUARIO, usuario.getNome());
        editor.putString(EMAIL_USUARIO, usuario.getEmail());
        editor.putString(SENHA_USUARIO, usuario.getSenha());
        editor.putString(TIPO_USUARIO, usuario.getTipo());
        //editor.putString(DATA_CADASTRO_USUARIO, usuario.getDataCadastro().toString());

        editor.commit();
    }

    public static Usuario obterUsuarioLogado(){
        Usuario usuario = null;

        SharedPreferences prefs = currentContext.getSharedPreferences("matafome", 0);
        boolean usuarioLogado = prefs.getBoolean(USUARIO_LOGADO, false);

        if(usuarioLogado){
            usuario = new Usuario();
            usuario.setId(prefs.getLong(ID_USUARIO, 0));
            usuario.setNome(prefs.getString(NOME_USUARIO, null));
            usuario.setEmail(prefs.getString(EMAIL_USUARIO, null));
            usuario.setSenha(prefs.getString(SENHA_USUARIO, null));
            usuario.setTipo(prefs.getString(TIPO_USUARIO, null));
        }

        return usuario;
    }
}
