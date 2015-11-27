package br.com.unigranrio.matafome.aplicacao.webservices;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.App;
import br.com.unigranrio.matafome.dominio.acoes.Mensagem;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.DetalheNegocio;

/**
 * Created by Thiago on 26/11/2015.
 */
public class ObterNegocioPorLocalizacaoAsyncService extends AsyncTaskAbstrata<Long, Void, ResultadoAcao<DetalheNegocio>> {
    @Override
    protected ResultadoAcao<DetalheNegocio> doInBackground(Long... params) {
        ResultadoAcao<DetalheNegocio> resultado = new ResultadoAcao<>();

        long idNegocio = params[0];
        long idUsuario = params[1];

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String url = App.montarUrlRest(R.string.url_obter_negocio_localizacao, idNegocio, idUsuario);

        try {
            DetalheNegocio negocio = restTemplate.getForObject(url, DetalheNegocio.class);
            resultado.setData(negocio);
        } catch (Exception e){
            List<Mensagem> erros = new ArrayList<>();
            erros.add(new Mensagem("Erro ao carregar dados, verifique sua conexão com a internet."));

            resultado.adicionarMensagens(erros);
        }

        return resultado;
    }
}
