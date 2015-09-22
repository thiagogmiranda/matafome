package br.com.unigranrio.matafome.aplicacao.client;

import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.App;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by Thiago on 15/09/2015.
 */
public class ObterUsuarioAsyncTask extends AsyncTaskAbstrata<String, Void, Usuario> {
    @Override
    protected Usuario doInBackground(String... voids) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            String email = voids[0];
            String url = App.montarUrlRest(R.string.url_obter_usuario_por_email, email);

            Usuario usuario = restTemplate.getForObject(url, Usuario.class);

            return usuario;
        } catch (Exception e){
            Log.e("Get User:", e.getMessage(), e);
        }
        return null;
    }
}
