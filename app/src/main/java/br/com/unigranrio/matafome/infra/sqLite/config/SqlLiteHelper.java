package br.com.unigranrio.matafome.infra.sqLite.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WebFis33 on 10/09/2015.
 */
public class SqlLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "piscina_miranda_vendas.db";
    public static final int DB_VERSION = 3;

    public SqlLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Usuario ( "
                + "id integer primary key autoincrement,"
                + "nome text not null,"
                + "senha text not null,"
                + "email text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Usuario");

        onCreate(sqLiteDatabase);
    }
}
