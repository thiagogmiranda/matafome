package br.com.unigranrio.matafome.infra.sqLite.repositorios;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.unigranrio.matafome.infra.sqLite.config.SqlLiteHelper;

/**
 * Created by WebFis33 on 10/09/2015.
 */
public abstract class RepositorioSQLite<T> {

    private SqlLiteHelper sqlLiteHelper;
    protected SQLiteDatabase sqLiteDatabase;

    public RepositorioSQLite(SqlLiteHelper helper) {
        sqlLiteHelper = helper;
    }

    protected abstract T cursorParaObjeto(Cursor cursor);
    protected abstract String getTabela();
    protected abstract String[] getProjecao();

    protected void abrirConexaoComBanco(boolean leitura) {
        if (bancoEhNuloOuNaoEstaAberto()) {
            if (leitura) {
                sqLiteDatabase = sqlLiteHelper.getReadableDatabase();
            } else {
                sqLiteDatabase = sqlLiteHelper.getWritableDatabase();
            }
        }
    }

    protected void fecharConexaoComBanco() {
        if (bancoNaoEhNuloEEstaAberto()) {
            sqLiteDatabase.close();
        }
    }

    private boolean bancoEhNuloOuNaoEstaAberto() {
        return bancoEhNulo() || conexaoNaoEstaAberta();
    }

    private boolean bancoNaoEhNuloEEstaAberto() {
        return bancoNaoEhNulo() && conexaoEstaAberta();
    }

    private boolean bancoEhNulo() {
        return sqLiteDatabase == null;
    }

    private boolean bancoNaoEhNulo() {
        return bancoEhNulo() == false;
    }

    private boolean conexaoEstaAberta() {
        return sqLiteDatabase.isOpen();
    }

    private boolean conexaoNaoEstaAberta() {
        return conexaoEstaAberta() == false;
    }

    protected Date stringParaData(String strData) {
        Date date = null;

        try {
            date = obterFormatoDeData().parse(strData);
        } catch (ParseException pe) {
            pe.printStackTrace();
            Log.e("ERROR", pe.getMessage());
        }

        return date;
    }

    protected String dataParaString(Date data) {
        return obterFormatoDeData().format(data);
    }

    private DateFormat obterFormatoDeData() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm");
    }

    protected Cursor consulta(String condicoes, String... parametros){
        return sqLiteDatabase.query(getTabela(), getProjecao(), condicoes, parametros, null, null, null);
    }
}
