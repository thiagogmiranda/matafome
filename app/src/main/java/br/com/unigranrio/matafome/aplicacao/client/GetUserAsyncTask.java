package br.com.unigranrio.matafome.aplicacao.client;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by Thiago on 15/09/2015.
 */
public class GetUserAsyncTask extends AsyncTask<String, Void, Usuario> {
    @Override
    protected Usuario doInBackground(String... voids) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Usuario usuario = restTemplate.getForObject("https://matafomeserver.herokuapp.com/usuario/obter-por-email?email=" + voids[0], Usuario.class);

            return usuario;
        } catch (Exception e){
            Log.e("Get User:", e.getMessage(), e);
        }
        return null;
    }
}
