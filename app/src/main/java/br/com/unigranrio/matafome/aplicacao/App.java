package br.com.unigranrio.matafome.aplicacao;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.util.Locale;

import br.com.unigranrio.matafome.R;

/**
 * Created by WebFis33 on 22/09/2015.
 */
public class App extends Application {
    private static Context currentContext;

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
}
