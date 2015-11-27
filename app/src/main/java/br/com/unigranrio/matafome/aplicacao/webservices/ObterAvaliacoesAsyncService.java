package br.com.unigranrio.matafome.aplicacao.webservices;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.App;
import br.com.unigranrio.matafome.dominio.acoes.Mensagem;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Avaliacao;

/**
 * Created by Thiago on 24/11/2015.
 */
public class ObterAvaliacoesAsyncService extends AsyncTaskAbstrata<Long, Void, ResultadoAcao<List<Avaliacao>>> {
    @Override
    protected ResultadoAcao<List<Avaliacao>> doInBackground(Long... longs) {
        ResultadoAcao<List<Avaliacao>> resultado = new ResultadoAcao<>();

        long id = longs[0];

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String url = App.montarUrlRest(R.string.url_obter_avaliacoes_negocio, id);

        try {
            Avaliacao[] avaliacao = restTemplate.getForObject(url, Avaliacao[].class);
            resultado.setData(Arrays.asList(avaliacao));
        } catch (Exception e){
            List<Mensagem> erros = new ArrayList<>();
            erros.add(new Mensagem("Erro ao obter dados, verifique sua conexão com a internet"));
            resultado.adicionarMensagens(erros);
        }

        return resultado;
    }
}
