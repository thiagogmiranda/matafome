package br.com.unigranrio.matafome.aplicacao.webservices;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import br.com.unigranrio.matafome.R;
import br.com.unigranrio.matafome.aplicacao.App;
import br.com.unigranrio.matafome.dominio.acoes.Mensagem;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by WebFis33 on 15/09/2015.
 */
public class CriarUsuarioAsyncTask extends AsyncTaskAbstrata<Usuario, Void, ResultadoAcao> {
    @Override
    protected ResultadoAcao doInBackground(Usuario... params) {
        ResultadoAcao resultado = new ResultadoAcao();

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            String url = App.montarUrlRest(R.string.url_criar_usuario);

            resultado = restTemplate.postForObject(url, params[0], ResultadoAcao.class);
        } catch (Exception exception) {
            List<Mensagem> erros = new ArrayList<>();
            erros.add(new Mensagem("Erro de conexão. Verifique se você está conectado à internet."));

            resultado.adicionarMensagens(erros);
        }

        return resultado;
    }
}
