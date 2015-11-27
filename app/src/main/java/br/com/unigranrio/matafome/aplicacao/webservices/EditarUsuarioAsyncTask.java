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
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by WebFis33 on 09/11/2015.
 */
public class EditarUsuarioAsyncTask extends AsyncTaskAbstrata<Usuario, Void, ResultadoAcao> {
    @Override
    protected ResultadoAcao doInBackground(Usuario... params) {
        ResultadoAcao resultado = new ResultadoAcao();

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            String url = App.montarUrlRest(R.string.url_editar_usuario);

            resultado = restTemplate.postForObject(url, params[0], ResultadoAcao.class);

            if(resultado.estaValido()){
                ObjectMapper mapper = new ObjectMapper();
                Usuario usuario =  mapper.convertValue(resultado.getData(), Usuario.class);
                resultado.setData(usuario);
            }
        } catch (Exception exception) {
            List<Mensagem> erros = new ArrayList<>();
            erros.add(new Mensagem("Erro de conexão. Verifique se você está conectado à internet."));

            resultado.adicionarMensagens(erros);

            exception.printStackTrace();
        }

        return resultado;
    }
}
