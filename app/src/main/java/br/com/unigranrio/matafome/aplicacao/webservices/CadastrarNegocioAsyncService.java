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
import br.com.unigranrio.matafome.dominio.modelo.Usuario;

/**
 * Created by WebFis33 on 16/10/2015.
 */
public class CadastrarNegocioAsyncService extends AsyncTaskAbstrata<Negocio, Void, ResultadoAcao<Negocio>> {

    @Override
    protected ResultadoAcao<Negocio> doInBackground(Negocio... negocios) {
        ResultadoAcao<Negocio> resultado = new ResultadoAcao<>();

        try {
            Negocio negocio = negocios[0];

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            String url = App.montarUrlRest(R.string.url_cadastrar_negocio);

            resultado = restTemplate.postForObject(url, negocio, ResultadoAcao.class);

            if(resultado.estaValido()){
                ObjectMapper mapper = new ObjectMapper();
                Negocio data =  mapper.convertValue(resultado.getData(), Negocio.class);
                resultado.setData(data);
            }
        } catch (Exception e) {
            List<Mensagem> erros = new ArrayList<>();
            erros.add(new Mensagem("Erro ao salvar os dados do negócio, verifique sua conexão com a internet."));

            resultado.adicionarMensagens(erros);

            e.printStackTrace();
        }

        return resultado;
    }
}
