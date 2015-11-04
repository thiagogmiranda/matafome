package br.com.unigranrio.matafome.aplicacao.webservices;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.App;
import br.com.unigranrio.matafome.dominio.modelo.Barraca;

/**
 * Created by WebFis33 on 15/10/2015.
 */
public class ObterBarracasUsuarioAsyncTask extends AsyncTaskAbstrata<String, Void, List<Barraca>> {
    @Override
    protected List<Barraca> doInBackground(String... params) {
        String email = params[0];

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String url = App.montarUrlRest(R.string.url_obter_pontos_venda_usuario, email);

        Barraca[] retorno = new Barraca[0];

        try {
            retorno = restTemplate.getForObject(url, Barraca[].class);
        } catch (Exception e){
            e.printStackTrace();
        }

        return Arrays.asList(retorno);
    }
}