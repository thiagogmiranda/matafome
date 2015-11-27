package br.com.unigranrio.matafome.aplicacao.webservices;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.App;
import br.com.unigranrio.matafome.dominio.acoes.Mensagem;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Avaliacao;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by Thiago on 27/11/2015.
 */
public class EnviarAvaliacaoAsyncService extends AsyncTaskAbstrata<Avaliacao, Void, ResultadoAcao<Avaliacao>> {
    @Override
    protected ResultadoAcao<Avaliacao> doInBackground(Avaliacao... avaliacaos) {
        ResultadoAcao<Avaliacao> resultado = new ResultadoAcao<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            String url = App.montarUrlRest(R.string.url_enviar_avaliacao);

            resultado = restTemplate.postForObject(url, avaliacaos[0], ResultadoAcao.class);

            if(resultado.estaValido()){
                ObjectMapper mapper = new ObjectMapper();
                Avaliacao avaliacao =  mapper.convertValue(resultado.getData(), Avaliacao.class);
                resultado.setData(avaliacao);
            }
        } catch (Exception exception) {
            List<Mensagem> erros = new ArrayList<>();
            erros.add(new Mensagem("Erro de conexão. Verifique se você está conectado à internet."));

            resultado.adicionarMensagens(erros);
        }

        return resultado;
    }
}
