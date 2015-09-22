package br.com.unigranrio.matafome.aplicacao.client;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by WebFis33 on 22/09/2015.
 */
public abstract class AsyncTaskAbstrata<TParametros, TProgresso, TResultado>
    extends AsyncTask<TParametros, TProgresso, TResultado> {

    protected ProgressDialog progressDialog;
    protected OnAsyncTaskExecutedListener<TResultado> taskExecutedListener;

    public void setProgressDialog(ProgressDialog progressDialog){
        this.progressDialog = progressDialog;
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setIndeterminate(true);
    }

    public void setOnExecutedListener(OnAsyncTaskExecutedListener<TResultado> taskExecutedListener){
        this.taskExecutedListener = taskExecutedListener;
    }

    @Override
    protected void onPreExecute() {
        if(progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(TResultado tResultado) {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }

        if(taskExecutedListener != null){
            taskExecutedListener.onAsyncTaskExecuted(tResultado);
        }
    }
}
