package br.com.unigranrio.matafome.aplicacao.client;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import br.com.unigranrio.matafome.dominio.modelo.Barraca;
/**
 * Created by WebFis33 on 15/09/2015.
 */
public class ObterBarracasDentroDoRaioAsyncTask extends AsyncTaskAbstrata<Double, Void, List<Barraca>> {
    @Override
    protected List<Barraca> doInBackground(Double... params) {
        double raio = params[0];
        double lat = params[1];
        double lng = params[2];

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Barraca[] retorno = restTemplate.getForObject("https://matafomeserver.herokuapp.com/barraca/obter-todas-dentro-raio?raio=" + raio
                + "&lat=" + lat + "&lng=" + lng, Barraca[].class);

        return Arrays.asList(retorno);
    }
}
