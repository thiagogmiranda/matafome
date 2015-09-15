package br.com.unigranrio.matafome.aplicacao.client;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import br.com.unigranrio.matafome.dominio.modelo.Negocio;
/**
 * Created by WebFis33 on 15/09/2015.
 */
public class ObterBarracasDentroDoRaioAsyncTask extends AsyncTask<Double, Void, List<Negocio>> {
    @Override
    protected List<Negocio> doInBackground(Double... params) {
        double raio = params[0];
        double lat = params[1];
        double lng = params[2];

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Negocio[] retorno = restTemplate.getForObject("https://matafomeserver.herokuapp.com/negocio/obter-todos-dentro-raio?raio=" + raio
                + "&lat=" + lat + "&lng=" + lng, Negocio[].class);

        return Arrays.asList(retorno);
    }
}
