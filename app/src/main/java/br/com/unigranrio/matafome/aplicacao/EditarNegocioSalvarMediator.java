package br.com.unigranrio.matafome.aplicacao;

import android.app.ProgressDialog;
import android.content.Intent;

import br.com.unigranrio.matafome.aplicacao.webservices.EditarNegocioAsyncService;
import br.com.unigranrio.matafome.aplicacao.webservices.OnAsyncTaskExecutedListener;
import br.com.unigranrio.matafome.dominio.acoes.ResultadoAcao;
import br.com.unigranrio.matafome.dominio.modelo.Negocio;

/**
 * Created by Thiago on 26/11/2015.
 */
public class EditarNegocioSalvarMediator
    implements OnAsyncTaskExecutedListener<ResultadoAcao<Negocio>> {
    private EdicaoNegocioActivity context;

    public EditarNegocioSalvarMediator(EdicaoNegocioActivity context){
        this.context = context;
    }

    public void salvar(Negocio negocio){
        try {
            EditarNegocioAsyncService service = new EditarNegocioAsyncService();
            service.setOnExecutedListener(this);

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Salvando informações ...");

            service.setProgressDialog(progressDialog);

            service.execute(negocio);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAsyncTaskExecuted(ResultadoAcao<Negocio> resultadoAcao) {
        if(resultadoAcao.estaValido()){
            Intent intent = new Intent();
            intent.setClass(context, GerenciarNegocioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            context.startActivity(intent);
            context.finalizar();
        } else {
            App.exibirMensagensDeErro(context, resultadoAcao.getMensagens());
        }
    }
}
