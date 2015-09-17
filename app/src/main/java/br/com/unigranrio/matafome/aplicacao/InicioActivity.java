package br.com.unigranrio.matafome.aplicacao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.unigranrio.matafome.R;

public class InicioActivity extends Activity {
    private Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inicio);

        btnEntrar = (Button)findViewById(R.id.dummy_button);
        btnEntrar.setVisibility(View.INVISIBLE);

        final Context btnContext = this;

        SharedPreferences prefs = getSharedPreferences("mata_fome", 0);
        boolean jaLogou = prefs.getBoolean("tem_usuario_logado", false);

        if(jaLogou){
            btnEntrar.setVisibility(View.INVISIBLE);

            iniciarActivityPrincipal();
        } else {
            btnEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(btnContext, LoginActivity.class);

                    startActivityForResult(intent, 1);
                }
            });
            btnEntrar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            iniciarActivityPrincipal();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void iniciarActivityPrincipal(){
        Intent intent = new Intent();
        intent.setClass(this, BuscarBarracaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }
}
