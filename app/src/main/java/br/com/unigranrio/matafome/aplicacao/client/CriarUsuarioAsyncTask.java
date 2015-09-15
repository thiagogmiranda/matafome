package br.com.unigranrio.matafome.aplicacao.client;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by WebFis33 on 15/09/2015.
 */
public class CriarUsuarioAsyncTask extends AsyncTask<Usuario, Void, Void> {
    @Override
    protected Void doInBackground(Usuario... params) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        restTemplate.postForObject("https://matafomeserver.herokuapp.com/usuario/criar", params[0], Usuario.class);

        return null;
    }
}
