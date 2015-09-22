package br.com.unigranrio.matafome.aplicacao.client;

import android.content.res.Resources;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by WebFis33 on 15/09/2015.
 */
public class CriarUsuarioAsyncTask extends AsyncTaskAbstrata<Usuario, Void, ResultadoAcao> {
    @Override
    protected ResultadoAcao doInBackground(Usuario... params) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        //String url = Resources.getSystem().getString(R.string.url_criar_usuario);

        ResultadoAcao resultado = restTemplate
                .postForObject("https://matafomeserver.herokuapp.com/usuario/criar", params[0], ResultadoAcao.class);

        return resultado;
    }
}
