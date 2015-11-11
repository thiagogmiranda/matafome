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
import br.com.unigranrio.matafome.dominio.modelo.Negocio;

/**
 * Created by WebFis33 on 15/10/2015.
 */
public class ObterNegocioUsuarioAsyncService extends AsyncTaskAbstrata<String, Void, ResultadoAcao<Negocio>> {
    @Override
    protected ResultadoAcao<Negocio> doInBackground(String... params) {
        ResultadoAcao<Negocio> resultado = new ResultadoAcao<>();

        String email = params[0];

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String url = App.montarUrlRest(R.string.url_obter_pontos_venda_usuario, email);

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
