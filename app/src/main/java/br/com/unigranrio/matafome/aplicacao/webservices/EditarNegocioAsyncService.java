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
import br.com.unigranrio.matafome.dominio.modelo.Negocio;

/**
 * Created by Thiago on 26/11/2015.
 */
public class EditarNegocioAsyncService extends AsyncTaskAbstrata<Negocio, Void, ResultadoAcao<Negocio>> {
    @Override
    protected ResultadoAcao<Negocio> doInBackground(Negocio... negocios) {
        ResultadoAcao<Negocio> resultado = new ResultadoAcao<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            String url = App.montarUrlRest(R.string.url_editar_negocio);

            resultado = restTemplate.postForObject(url, negocios[0], ResultadoAcao.class);

            if(resultado.estaValido()){
                ObjectMapper mapper = new ObjectMapper();
                Negocio negocio =  mapper.convertValue(resultado.getData(), Negocio.class);
                resultado.setData(negocio);
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
