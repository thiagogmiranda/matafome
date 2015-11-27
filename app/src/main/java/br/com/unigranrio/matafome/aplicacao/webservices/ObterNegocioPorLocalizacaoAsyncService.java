package br.com.unigranrio.matafome.aplicacao.webservices;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.App;
import br.com.unigranrio.matafome.dominio.acoes.Mensagem;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;

/**
 * Created by Thiago on 26/11/2015.
 */
public class ObterNegocioPorLocalizacaoAsyncService extends AsyncTaskAbstrata<Double, Void, ResultadoAcao<Negocio>> {
    @Override
    protected ResultadoAcao<Negocio> doInBackground(Double... params) {
        ResultadoAcao<Negocio> resultado = new ResultadoAcao<>();

        double lat = params[0];
        double lng = params[1];

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String url = App.montarUrlRest(R.string.url_obter_negocio_localizacao, lat, lng);

        try {
            Negocio negocio = restTemplate.getForObject(url, Negocio.class);
            resultado.setData(negocio);
        } catch (Exception e){
            List<Mensagem> erros = new ArrayList<>();
            erros.add(new Mensagem("Erro ao carregar dados, verifique sua conexão com a internet."));

            resultado.adicionarMensagens(erros);
        }

        return resultado;
    }
}
